package ovh.homecitadel.uni.techbazar.Service.User;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ovh.homecitadel.uni.techbazar.Entity.Auction.AuctionEntity;
import ovh.homecitadel.uni.techbazar.Entity.Product.ProductEntity;
import ovh.homecitadel.uni.techbazar.Entity.User.MongoDB.WishlistEntity;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.ObjectNotFoundException;
import ovh.homecitadel.uni.techbazar.Helper.Model.WishlistResponse;
import ovh.homecitadel.uni.techbazar.Helper.Notification.MessageModel;
import ovh.homecitadel.uni.techbazar.Repository.Auction.AuctionRepository;
import ovh.homecitadel.uni.techbazar.Repository.Product.ProductRepository;
import ovh.homecitadel.uni.techbazar.Repository.User.MongoDB.WishlistRepository;
import ovh.homecitadel.uni.techbazar.Service.NotificationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;
    private final AuctionRepository auctionRepository;

    private final NotificationService notificationService;

    public WishlistService(WishlistRepository wishlistRepository, ProductRepository productRepository, AuctionRepository auctionRepository, NotificationService notificationService) {
        this.wishlistRepository = wishlistRepository;
        this.productRepository = productRepository;
        this.auctionRepository = auctionRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public WishlistResponse getWishlist(String userId) throws ObjectNotFoundException {
        WishlistEntity wishlist = findOrCreate(userId);
        WishlistResponse response = new WishlistResponse();
        response.setWishlistId(wishlist.getWishlistId());
        response.setUserId(userId);
        List<ProductEntity> products = new ArrayList<>();
        List<AuctionEntity> auctions = new ArrayList<>();

        for(Long productId : wishlist.getProducts()) {
            Optional<ProductEntity> tmp2 = this.productRepository.findByProductId(productId);
            if(tmp2.isEmpty()) {
                wishlist.getProducts().remove(productId);
                MessageModel messageModel = new MessageModel();
                messageModel.setSender(null);
                messageModel.setRecipient(userId);
                messageModel.setSubject("Product No Longer available.");
                messageModel.setMsgBody("One product you saved is not available anymore.");
                this.notificationService.sendMessage(messageModel);
            }
            else {
                products.add(tmp2.get());
            }
        }

        for(Long auctionId : wishlist.getAuctionSaved()) {
            Optional<AuctionEntity> tmp3 = this.auctionRepository.findById(auctionId);
            if(tmp3.isEmpty()) {
                wishlist.getAuctionSaved().remove(auctionId);
                MessageModel messageModel = new MessageModel();
                messageModel.setSender(null);
                messageModel.setRecipient(userId);
                messageModel.setSubject("Auction No Longer Available.");
                messageModel.setMsgBody("Auction with ID [" + auctionId + "] No Longer Available");
                this.notificationService.sendMessage(messageModel);
            } else {
                auctions.add(tmp3.get());
            }
        }

        response.setProducts(products);
        response.setAuctions(auctions);
        return response;
    }

    @Transactional
    public boolean removeFromWishlist(String userId, Long productId) throws ObjectNotFoundException {
        WishlistEntity wishlist = findOrCreate(userId);

        Optional<ProductEntity> tmp2 = this.productRepository.findByProductId(productId);
        if(tmp2.isEmpty()) throw new ObjectNotFoundException("Product Not Found");
        wishlist.getProducts().remove(tmp2.get().getProductId());
        this.wishlistRepository.save(wishlist);
        return true;
    }

    @Transactional
    public boolean addToWishlist(String userId, Long productId) throws ObjectNotFoundException {
        WishlistEntity wishlist = findOrCreate(userId);

        Optional<ProductEntity> tmp2 = this.productRepository.findByProductId(productId);
        if(tmp2.isEmpty()) throw new ObjectNotFoundException("Product Not Found");
        wishlist.getProducts().add(tmp2.get().getProductId());
        this.wishlistRepository.save(wishlist);
        return true;
    }


    private WishlistEntity findOrCreate(String userId) {
        Optional<WishlistEntity> tmp = this.wishlistRepository.findByUserId(userId);
        WishlistEntity wishlist;
        if(tmp.isEmpty()) {
            wishlist = new WishlistEntity();
            wishlist.setUserId(userId);
            wishlist.setProducts(new ArrayList<>());
        } else {
            wishlist = tmp.get();
        }
        return wishlist;
    }
    @Transactional
    public boolean saveAuction(String userId, Long auctionId) throws ObjectNotFoundException {
        Optional<WishlistEntity> tmp = this.wishlistRepository.findByUserId(userId);
        if(tmp.isEmpty()) throw new ObjectNotFoundException("Wishlist Not Found");

        Optional<AuctionEntity> tmp2 = this.auctionRepository.findById(auctionId);
        if(tmp2.isEmpty()) throw new ObjectNotFoundException("Auction not Found");

        tmp.get().getAuctionSaved().add(auctionId);
        this.wishlistRepository.save(tmp.get());
        return true;
    }

}
