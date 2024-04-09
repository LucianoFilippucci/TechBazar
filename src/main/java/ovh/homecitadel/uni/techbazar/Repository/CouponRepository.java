package ovh.homecitadel.uni.techbazar.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ovh.homecitadel.uni.techbazar.Entity.CouponEntity;

import java.util.Collection;

@Repository
public interface CouponRepository extends JpaRepository<CouponEntity, String> {

    Collection<CouponEntity> findByStoreId(String storeId);
}
