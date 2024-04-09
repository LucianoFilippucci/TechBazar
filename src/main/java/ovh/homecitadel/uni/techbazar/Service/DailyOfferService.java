package ovh.homecitadel.uni.techbazar.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ovh.homecitadel.uni.techbazar.Entity.DailyOfferEntity;
import ovh.homecitadel.uni.techbazar.Entity.Product.ProductEntity;
import ovh.homecitadel.uni.techbazar.Entity.Product.ProductModelEntity;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.DateException;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.ObjectNotFoundException;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.UnauthorizedAccessException;
import ovh.homecitadel.uni.techbazar.Helper.Model.DailyOfferRequest;
import ovh.homecitadel.uni.techbazar.Repository.DailyOfferRepository;
import ovh.homecitadel.uni.techbazar.Repository.Product.ProductModelRepository;
import ovh.homecitadel.uni.techbazar.Repository.Product.ProductRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collector;

@Service
public class DailyOfferService {

    private final DailyOfferRepository dailyOfferRepository;
    private final ProductRepository productRepository;
    private final ProductModelRepository productModelRepository;

    public DailyOfferService(DailyOfferRepository dailyOfferRepository, ProductRepository productRepository, ProductModelRepository productModelRepository) {
        this.dailyOfferRepository = dailyOfferRepository;
        this.productRepository = productRepository;
        this.productModelRepository = productModelRepository;
    }

    @Transactional
    public Collection<DailyOfferEntity> getTodayOffer() {
        return this.dailyOfferRepository.findAllByDate(LocalDate.now());
    }

    @Transactional
    public Collection<DailyOfferEntity> getFutureDaily() {
        return this.dailyOfferRepository.findAllByDateIsAfter(LocalDate.now());
    }

    @Transactional
    public Collection<DailyOfferEntity> findAllByStoreId(String storeId) {
        return this.dailyOfferRepository.findAllByStoreIdAndDateIsAfter(storeId, LocalDate.now());
    }

    @Transactional
    public Collection<DailyOfferEntity> findAllByModel(Long modelId) throws ObjectNotFoundException {
        Optional<ProductModelEntity> tmp = this.productModelRepository.findById(modelId);
        if(tmp.isEmpty()) throw new ObjectNotFoundException("Model Not Found");
        return this.dailyOfferRepository.findAllByModelAndDateIsAfter(tmp.get(), LocalDate.now());
    }

    @Transactional
    public Collection<DailyOfferEntity> findAllByProduct(Long productId) throws ObjectNotFoundException {
        Optional<ProductEntity> tmp = this.productRepository.findById(productId);
        if(tmp.isEmpty()) throw new ObjectNotFoundException("Product Not Found");
        return this.dailyOfferRepository.findAllByProductAndDateIsAfter(tmp.get(), LocalDate.now());
    }


    @Transactional
    public DailyOfferEntity getDaily(Long dailyId) throws ObjectNotFoundException {
        Optional<DailyOfferEntity> tmp =  this.dailyOfferRepository.findById(dailyId);
        if(tmp.isEmpty()) throw new ObjectNotFoundException("Daily Offer Not Found");
        return tmp.get();
    }

    @Transactional
    public DailyOfferEntity newDailyOffer(String storeId, DailyOfferRequest request) throws ObjectNotFoundException, UnauthorizedAccessException {
        DailyOfferEntity daily = new DailyOfferEntity();

        if(request.getProductId() != 0) {
            // The offer is for all the models.
            Optional<ProductEntity> tmp = this.productRepository.findById(request.getProductId());
            if(tmp.isEmpty()) throw new ObjectNotFoundException("Product Not Found.");
            if(!tmp.get().getStoreId().equals(storeId)) throw new UnauthorizedAccessException("This Object is not yours");
            daily.setDate(request.getDate());
            daily.setDiscount(request.getDiscount());
            daily.setStoreId(storeId);
            daily.setProduct(tmp.get());
        } else {
            // The offer is only for the specific model
            Optional<ProductModelEntity> tmp2 = this.productModelRepository.findById(request.getModelId());
            if(tmp2.isEmpty()) throw new ObjectNotFoundException("Model not Found");
            Optional<ProductEntity> tmp3 = this.productRepository.findById(tmp2.get().getProductEntity().getProductId());
            if(tmp3.isEmpty()) throw new ObjectNotFoundException("Product Not Found");
            if(!tmp3.get().getStoreId().equals(storeId)) throw new UnauthorizedAccessException("This Model/Product is not yours.");

            daily.setModel(tmp2.get());
            daily.setDate(request.getDate());
            daily.setDiscount(request.getDiscount());
            daily.setStoreId(storeId);
        }

        return this.dailyOfferRepository.save(daily);
    }

    @Transactional
    public DailyOfferEntity editDaily(String storeId, DailyOfferRequest request) throws ObjectNotFoundException, UnauthorizedAccessException, DateException {
        Optional<DailyOfferEntity> tmp = this.dailyOfferRepository.findById(request.getDailyId());
        if(tmp.isEmpty()) throw new ObjectNotFoundException("Daily Offer not found");
        if(!tmp.get().getStoreId().equals(storeId)) throw new UnauthorizedAccessException("this daily is not yours");
        DailyOfferEntity daily = tmp.get();

        if(!daily.getDate().equals(request.getDate())) {
            if(LocalDate.now().isAfter(request.getDate())) throw new DateException("The new Date is Before Today.");
            daily.setDate(request.getDate());
        }

        if(daily.getDiscount() != request.getDiscount())
            daily.setDiscount(request.getDiscount());

        if(daily.getProduct() != null) {
            // the offer is for the product
            if(request.getProductId() == 0) {
                // the offer is modified to be for a model
                Optional<ProductModelEntity> tmp2 = this.productModelRepository.findById(request.getModelId());
                if(tmp2.isEmpty()) throw new ObjectNotFoundException("Model Not Found");
                daily.setProduct(null);
                daily.setModel(tmp2.get());
            } else {
                // the offer still be about the product
                if(!daily.getProduct().getProductId().equals(request.getProductId())) {
                    // the product is different
                    Optional<ProductEntity> tmp3 = this.productRepository.findById(request.getProductId());
                    if(tmp3.isEmpty()) throw new ObjectNotFoundException("Product Not Found");
                    daily.setProduct(tmp3.get());
                    daily.setModel(null);
                }
                // else the product still the same. nothing changed.
            }
        } else {
            // the offer is for the model
            if(request.getModelId() == 0) {
                //the offer is modified to be for a product.
                Optional<ProductEntity> tmp4 = this.productRepository.findByProductId(request.getProductId());
                if(tmp4.isEmpty()) throw new ObjectNotFoundException("Product Not Found");
                daily.setModel(null);
                daily.setProduct(tmp4.get());
            } else {
                // the offer still about the model
                if(!daily.getModel().getProductModelId().equals(request.getModelId())) {
                    // different model
                    Optional<ProductModelEntity> tmp5 = this.productModelRepository.findById(request.getModelId());
                    if(tmp5.isEmpty()) throw new ObjectNotFoundException("Model Not Found");
                    daily.setModel(tmp5.get());
                }
                // nothing changed.
            }
        }

        return this.dailyOfferRepository.save(daily);
    }


}
