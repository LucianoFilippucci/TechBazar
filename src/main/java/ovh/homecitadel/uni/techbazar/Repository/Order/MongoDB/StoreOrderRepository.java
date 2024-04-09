package ovh.homecitadel.uni.techbazar.Repository.Order.MongoDB;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ovh.homecitadel.uni.techbazar.Entity.Order.MongoDB.StoreOrderEntity;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface StoreOrderRepository extends MongoRepository<StoreOrderEntity, ObjectId> {

    Collection<StoreOrderEntity> findAllByStoreId(String storeId);

    Optional<StoreOrderEntity> findByOrderId(String orderId);
}
