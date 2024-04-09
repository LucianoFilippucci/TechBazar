package ovh.homecitadel.uni.techbazar.Repository.Order.MongoDB;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import ovh.homecitadel.uni.techbazar.Entity.Order.MongoDB.OrderDetailsEntity;

import java.util.Optional;

@Repository
public interface OrderDetailsRepository extends MongoRepository<OrderDetailsEntity, ObjectId> {

    @Query("{'orderId': ?0}")
    Optional<OrderDetailsEntity> findByOrderId(String orderId);
}
