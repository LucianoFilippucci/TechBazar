package ovh.homecitadel.uni.techbazar.Repository.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ovh.homecitadel.uni.techbazar.Entity.Product.ProductCategoryEntity;

import java.util.Optional;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategoryEntity, Long> {

    Optional<ProductCategoryEntity> findByCategoryId(Long id);
    Optional<ProductCategoryEntity> findByCategoryName(String name);
}
