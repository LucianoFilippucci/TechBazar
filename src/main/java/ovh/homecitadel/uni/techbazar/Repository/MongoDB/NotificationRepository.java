package ovh.homecitadel.uni.techbazar.Repository.MongoDB;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ovh.homecitadel.uni.techbazar.Entity.NotificationEntity;

import java.util.Optional;

@Repository
public interface NotificationRepository extends MongoRepository<NotificationEntity, ObjectId> {

    Optional<NotificationEntity> findNotificationEntitiesByUserId(String userId);

}
