package ovh.homecitadel.uni.techbazar.Service.Auction;

import org.keycloak.admin.client.Keycloak;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ovh.homecitadel.uni.techbazar.Entity.Auction.AuctionEntity;
import ovh.homecitadel.uni.techbazar.Entity.Auction.MongoDB.BidEntity;
import ovh.homecitadel.uni.techbazar.Entity.Product.ProductModelEntity;
import ovh.homecitadel.uni.techbazar.Entity.User.MongoDB.WishlistEntity;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.AuctionException;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.ObjectNotFoundException;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.UnauthorizedAccessException;
import ovh.homecitadel.uni.techbazar.Helper.Model.Auction.AuctionRequest;
import ovh.homecitadel.uni.techbazar.Helper.Model.Auction.AuctionStatus;
import ovh.homecitadel.uni.techbazar.Helper.Model.Auction.BidModel;
import ovh.homecitadel.uni.techbazar.Helper.Notification.MessageModel;
import ovh.homecitadel.uni.techbazar.Repository.Auction.AuctionRepository;
import ovh.homecitadel.uni.techbazar.Repository.Auction.MongoDB.BidRepository;
import ovh.homecitadel.uni.techbazar.Repository.Product.ProductModelRepository;
import ovh.homecitadel.uni.techbazar.Repository.User.MongoDB.WishlistRepository;
import ovh.homecitadel.uni.techbazar.Security.KeycloakSecurityUtil;
import ovh.homecitadel.uni.techbazar.Service.NotificationService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class AuctionService {
    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;
    private final ProductModelRepository productModelRepository;

    private final NotificationService notificationService;
    private final KeycloakSecurityUtil keycloakSecurityUtil;

    private final WishlistRepository wishlistRepository;

    @Value("${realm}")
    private String realm;

    public AuctionService(AuctionRepository auctionRepository, BidRepository bidRepository, ProductModelRepository productModelRepository, NotificationService notificationService, KeycloakSecurityUtil keycloakSecurityUtil, WishlistRepository wishlistRepository) {
        this.auctionRepository = auctionRepository;
        this.bidRepository = bidRepository;
        this.productModelRepository = productModelRepository;
        this.notificationService = notificationService;
        this.keycloakSecurityUtil = keycloakSecurityUtil;
        this.wishlistRepository = wishlistRepository;
    }

    public AuctionEntity getAuctionById(Long auctionId) throws ObjectNotFoundException {
        Optional<AuctionEntity> tmp = this.auctionRepository.findById(auctionId);
        if(tmp.isEmpty()) throw new ObjectNotFoundException("Auction Not Found");
        return tmp.get();
    }

    public Collection<AuctionEntity> getAll() {
        return this.auctionRepository.findAll();
    }




    @Transactional
    public AuctionEntity newAuction(String storeId, AuctionRequest request) throws ObjectNotFoundException {
        Optional<ProductModelEntity> tmp = this.productModelRepository.findById(request.getModelId());
        if(tmp.isEmpty()) throw new ObjectNotFoundException("Model Not Found");
        ProductModelEntity model = tmp.get();

        AuctionEntity auction = new AuctionEntity();
        auction.setModel(model);
        auction.setEndDate(request.getEndDate());
        auction.setStartDate(request.getStartDate());
        auction.setAuctionStatus(AuctionStatus.NOT_STARTED);
        auction.setStoreId(storeId);
        auction.setStartPrice(request.getStartPrice());
        auction.setFinalPrice(BigDecimal.ZERO);
        AuctionEntity saved = this.auctionRepository.save(auction);
        BidEntity bidEntity = new BidEntity();
        bidEntity.setAuctionId(saved.getAuctionId());
        bidEntity.setAuctionBids(new ArrayList<>());

        this.bidRepository.save(bidEntity);

        return saved;
    }

    @Transactional
    public AuctionEntity editAuction(String storeId, AuctionRequest request, Long auctionId) throws ObjectNotFoundException, UnauthorizedAccessException, AuctionException {
        Optional<AuctionEntity> tmp = this.auctionRepository.findById(auctionId);
        if(tmp.isEmpty()) throw new ObjectNotFoundException("Auction not Found");
        AuctionEntity auction = tmp.get();

        if(!auction.getStoreId().equals(storeId)) throw new UnauthorizedAccessException("This Auction is not yours.");

        if(auction.getAuctionStatus() == AuctionStatus.NOT_STARTED) {
            if(!auction.getModel().getProductModelId().equals(request.getModelId())) {
                Optional<ProductModelEntity> tmp2 = this.productModelRepository.findById(request.getModelId());
                if(tmp2.isEmpty()) throw new ObjectNotFoundException("Model Not Found");
                auction.setModel(tmp2.get());
            }
            if(!auction.getEndDate().equals(request.getEndDate()))
                auction.setEndDate(request.getEndDate());
            if(!auction.getStartDate().equals(request.getStartDate()))
                auction.setStartDate(request.getStartDate());
            if(!auction.getStartPrice().equals(request.getStartPrice()))
                auction.setStartPrice(request.getStartPrice());

            return this.auctionRepository.save(auction);
        } else throw new AuctionException("Cannot Modify Auction. Auction Status = " + auction.getAuctionStatus().toString());
    }

    @Transactional
    public boolean placeBid(String userId, BidModel bidModel, Long auctionId) throws ObjectNotFoundException, AuctionException {
        Optional<AuctionEntity> tmp = this.auctionRepository.findById(auctionId);
        if(tmp.isEmpty()) throw new ObjectNotFoundException("Auction with id [" + auctionId + "] Not Found");
        AuctionEntity auction = tmp.get();

        if(auction.getAuctionStatus().equals(AuctionStatus.ACTIVE)) {
            if(LocalDateTime.now().isAfter(auction.getEndDate())) {
                auction.setAuctionStatus(AuctionStatus.CLOSED);
                setWinnerAndNotify(auction);
                throw new AuctionException("Auction Closed");
            }
            // Auction Still going. save the bid.
            if(auction.getFinalPrice().compareTo(bidModel.getPrice()) > 0 || auction.getFinalPrice().compareTo(bidModel.getPrice()) == 0) throw new AuctionException("Price should be greater then [" + auction.getFinalPrice() + "]");
            Optional<BidEntity> tmp2 = this.bidRepository.findByAuctionId(auction.getAuctionId());
            BidEntity bidEntity;
            if(tmp2.isEmpty()) {
                // it's weird that there's no BidEntity since its created when the Auction is. So if for some weird reason this happens, lets just create it
                bidEntity = new BidEntity();
                bidEntity.setAuctionId(auction.getAuctionId());
                bidEntity.setAuctionBids(new ArrayList<>());
            } else {
                bidEntity = tmp2.get();
            }
            BigDecimal lastBidPrice = bidEntity.getAuctionBids().size() > 0 ? bidEntity.getAuctionBids().get(bidEntity.getAuctionBids().size() - 1).getPrice() : BigDecimal.ZERO;
            if(lastBidPrice.compareTo(bidModel.getPrice()) < 0) {
                bidEntity.getAuctionBids().add(new BidModel(userId, bidModel.getTimestamp(), bidModel.getPrice()));
                auction.setFinalPrice(bidModel.getPrice());
                this.bidRepository.save(bidEntity);
                this.auctionRepository.save(auction);
                return true;
            }
            throw new AuctionException("Price should be grater then [" + lastBidPrice + "]");

        } else {
            throw new AuctionException("The Auction is: " + auction.getAuctionStatus().toString());
        }
    }


    @Scheduled(fixedRate = 60000)
    protected void checkAuctions() throws ObjectNotFoundException {
        List<AuctionEntity> activeAuctions = this.auctionRepository.findByAuctionStatus(AuctionStatus.ACTIVE);
        List<AuctionEntity> notStartedAuctions = this.auctionRepository.findByAuctionStatus(AuctionStatus.NOT_STARTED);

        if(!activeAuctions.isEmpty()) {
            for(AuctionEntity auction : activeAuctions) {
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime endDate = auction.getEndDate();

                if(now.isAfter(endDate)) {
                    setWinnerAndNotify(auction);
                }
            }
        }

        if(!notStartedAuctions.isEmpty()) {
            for(AuctionEntity auction : notStartedAuctions) {
                if(LocalDateTime.now().isAfter(auction.getStartDate())) {
                    auction.setAuctionStatus(AuctionStatus.ACTIVE);
                    this.auctionRepository.save(auction);
                    Collection<WishlistEntity> tmp = this.wishlistRepository.findAllByAuctionSavedContains( auction.getAuctionId());

                    tmp.forEach(wishlist -> {
                        MessageModel model = new MessageModel();
                        model.setSubject("Saved Auction has been Started");
                        model.setMsgBody("Auction with ID [" + auction.getAuctionId() + "] has started and will end " + auction.getEndDate());
                        model.setSender(null);
                        model.setRecipient(wishlist.getUserId());
                        try {

                            this.notificationService.sendMessage(model);
                        } catch (ObjectNotFoundException e) {
                            // TODO: wtf we do there?
                        }
                    });
                }
            }
        }
    }

    private void setWinnerAndNotify(AuctionEntity auction) throws ObjectNotFoundException {
        Optional<BidEntity> tmp = this.bidRepository.findByAuctionId(auction.getAuctionId());
        if(tmp.isEmpty()) throw new ObjectNotFoundException("Bids Not found");
        BidEntity bidEntity = tmp.get();
        Keycloak keycloak = this.keycloakSecurityUtil.getKeycloakInstance();

        if(bidEntity.getAuctionBids().isEmpty()) {
            // No one placed a bid, there's no winner.
            //String storeEmail = keycloak.realm(realm).users().get(auction.getStoreId()).toRepresentation().getEmail();
            MessageModel message = new MessageModel();
            message.setRecipient(auction.getStoreId());
            message.setSender(null);
            message.setSubject("Auction Closed without winner.");
            message.setMsgBody("The auction terminated without having a winner.");
            this.notificationService.sendMessage(message);
            auction.setAuctionStatus(AuctionStatus.CLOSED);
            auction.setWinnerId("");
        } else {
            // the last one who placed the bid is the winner.
            String winnerId = bidEntity.getAuctionBids().get(bidEntity.getAuctionBids().size() - 1).getUserId();
            auction.setWinnerId(winnerId);
            auction.setAuctionStatus(AuctionStatus.CLOSED);

            for(BidModel model : bidEntity.getAuctionBids()) {
                String userEmail = keycloak.realm(this.realm).users().get(model.getUserId()).toRepresentation().getEmail();
                if(model.getUserId().equals(winnerId)) {
                    // Notify winner
                    MessageModel message = new MessageModel();
                    message.setSender(null);
                    message.setRecipient(model.getUserId());
                    message.setSubject("YOU ARE THE WINNER");
                    message.setMsgBody("CONGRATULATIONS YOU WON THE AUCTION WITH ID [" + bidEntity.getAuctionId() + "]");
                    this.notificationService.sendMessage(message);
                } else {
                    // Notify everyone else
                    MessageModel message = new MessageModel();
                    message.setSender(null);
                    message.setRecipient(model.getUserId());
                    message.setSubject("YOU LOST THE AUCTION");
                    message.setMsgBody("SORRY BUT YOU LOST THE AUCTION WITH ID [" + bidEntity.getAuctionId() + "]");
                    this.notificationService.sendMessage(message);
                }
            }


        }
        this.auctionRepository.save(auction);
        this.bidRepository.save(bidEntity);
    }
}
