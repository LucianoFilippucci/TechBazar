package ovh.homecitadel.uni.techbazar.Repository.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ovh.homecitadel.uni.techbazar.Entity.Product.ProductEntity;
import ovh.homecitadel.uni.techbazar.Entity.Product.ProductModelEntity;

import java.util.Optional;

@Repository
public interface ProductModelRepository extends JpaRepository<ProductModelEntity, Long> {

    Optional<ProductModelEntity> findByProductEntity(ProductEntity product);
}
