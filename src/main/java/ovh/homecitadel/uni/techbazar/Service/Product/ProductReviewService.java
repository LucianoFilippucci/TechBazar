package ovh.homecitadel.uni.techbazar.Service.Product;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ovh.homecitadel.uni.techbazar.Entity.Product.ProductEntity;
import ovh.homecitadel.uni.techbazar.Entity.Product.ProductReviewEntity;
import ovh.homecitadel.uni.techbazar.Entity.UserLiked;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.ObjectNotFoundException;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.UnauthorizedAccessException;
import ovh.homecitadel.uni.techbazar.Helper.Model.NewReviewRequest;
import ovh.homecitadel.uni.techbazar.Repository.MongoDB.UserLikedRepository;
import ovh.homecitadel.uni.techbazar.Repository.Product.ProductRepository;
import ovh.homecitadel.uni.techbazar.Repository.Product.ProductReviewRepository;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Service
public class ProductReviewService {

    private final ProductRepository productRepository;
    private final ProductReviewRepository productReviewRepository;
    private final UserLikedRepository userLikedRepository;


    public ProductReviewService(ProductReviewRepository productReviewRepository, ProductRepository productRepository, UserLikedRepository userLikedRepository) {
        this.productRepository = productRepository;
        this.productReviewRepository = productReviewRepository;
        this.userLikedRepository = userLikedRepository;
    }

    @Transactional
    public Collection<ProductReviewEntity> getProductReviews(Long productId) throws ObjectNotFoundException {
        Optional<ProductEntity> tmp = this.productRepository.findByProductId(productId);
        if(tmp.isEmpty()) throw new ObjectNotFoundException("Product Not Found");
        ProductEntity product = tmp.get();

        return this.productReviewRepository.findByProduct(product);
    }

    @Transactional
    public ProductReviewEntity getProductReview(Long reviewId) throws ObjectNotFoundException {
        Optional<ProductReviewEntity> tmp = this.productReviewRepository.findById(reviewId);
        if(tmp.isEmpty()) throw new ObjectNotFoundException("Review Not Found");

        return tmp.get();
    }

    @Transactional
    public ProductReviewEntity newReview(String userId, Long productId, NewReviewRequest reviewRequest) throws ObjectNotFoundException {
        Optional<ProductEntity> tmp = this.productRepository.findById(productId);
        if(tmp.isEmpty()) throw new ObjectNotFoundException("Product Not Found");
        ProductEntity product = tmp.get();

        ProductReviewEntity review = new ProductReviewEntity();
        review.setProduct(product);
        review.setTitle(reviewRequest.getTitle());
        review.setBody(reviewRequest.getBody());
        review.setRating(reviewRequest.getRating());
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());
        review.setUserId(userId);

        return this.productReviewRepository.save(review);
    }

    @Transactional
    public boolean deleteReview(String userId, Long reviewId) throws ObjectNotFoundException, UnauthorizedAccessException {
        Optional<ProductReviewEntity> tmp = this.productReviewRepository.findById(reviewId);
        if(tmp.isEmpty()) throw new ObjectNotFoundException("Review Not Found");
        ProductReviewEntity review = tmp.get();

        if(!review.getUserId().equals(userId)) throw new UnauthorizedAccessException("That Review is not yours.");

        this.productReviewRepository.delete(review);
        return true;
    }

    @Transactional
    public boolean likedReview(String userId, Long reviewId) throws ObjectNotFoundException {
        Optional<ProductReviewEntity> tmp = this.productReviewRepository.findById(reviewId);
        if(tmp.isEmpty()) throw new ObjectNotFoundException("Review Not Found");
        ProductReviewEntity review = tmp.get();

        Optional<UserLiked> tmp2 = this.userLikedRepository.findByUserId(userId);
        if(tmp2.isEmpty()) throw new ObjectNotFoundException("User Liked Not Found");
        UserLiked userLiked = tmp2.get();
        return userLiked.getLikedReviews().contains(reviewId);
    }

    @Transactional
    public boolean likeReview(String userId, Long reviewId) throws ObjectNotFoundException {
        Optional<ProductReviewEntity> tmp = this.productReviewRepository.findById(reviewId);
        if(tmp.isEmpty()) throw new ObjectNotFoundException("Review Not Found");
        ProductReviewEntity review = tmp.get();
        Optional<UserLiked> tmp2 = this.userLikedRepository.findByUserId(userId);
        if(tmp2.isEmpty()) throw new ObjectNotFoundException("User Liked Not Found");
        UserLiked userLiked = tmp2.get();
        if(!userLiked.getLikedReviews().contains(reviewId))
            userLiked.getLikedReviews().add(reviewId);
        else
            userLiked.getLikedReviews().removeIf(p -> p.equals(reviewId));

        review.setLikes(review.getLikes() + 1);

        this.userLikedRepository.save(userLiked);
        this.productReviewRepository.save(review);

        return true;
    }
}
