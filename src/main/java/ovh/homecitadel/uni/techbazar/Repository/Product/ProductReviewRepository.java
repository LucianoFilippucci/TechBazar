package ovh.homecitadel.uni.techbazar.Repository.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ovh.homecitadel.uni.techbazar.Entity.Product.ProductEntity;
import ovh.homecitadel.uni.techbazar.Entity.Product.ProductReviewEntity;

import java.util.Collection;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReviewEntity, Long> {

    Collection<ProductReviewEntity> findByUserId(String userId);
    Collection<ProductReviewEntity> findByProduct(ProductEntity product);
}
