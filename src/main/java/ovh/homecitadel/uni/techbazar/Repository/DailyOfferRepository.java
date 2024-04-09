package ovh.homecitadel.uni.techbazar.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ovh.homecitadel.uni.techbazar.Entity.DailyOfferEntity;
import ovh.homecitadel.uni.techbazar.Entity.Product.ProductEntity;
import ovh.homecitadel.uni.techbazar.Entity.Product.ProductModelEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface DailyOfferRepository extends JpaRepository<DailyOfferEntity, Long> {

    Collection<DailyOfferEntity> findAllByProductAndDateIsAfter(ProductEntity product, LocalDate date);
    Collection<DailyOfferEntity> findAllByModelAndDateIsAfter(ProductModelEntity model, LocalDate date);

    Collection<DailyOfferEntity> findAllByStoreIdAndDateIsAfter(String storeId, LocalDate date);
    Collection<DailyOfferEntity> findAllByDate(LocalDate date);

    Collection<DailyOfferEntity> findAllByDateIsAfter(LocalDate date);
}
