package ovh.homecitadel.uni.techbazar.Repository.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ovh.homecitadel.uni.techbazar.Entity.Product.ProductEntity;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    Collection<ProductEntity> findAllByProductNameContaining(String keyword);

    Optional<ProductEntity> findByProductId(Long id);

    Collection<ProductEntity> findByStoreId(String storeId);
}
