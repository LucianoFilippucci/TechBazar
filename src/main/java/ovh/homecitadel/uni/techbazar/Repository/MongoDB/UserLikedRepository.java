package ovh.homecitadel.uni.techbazar.Repository.MongoDB;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import ovh.homecitadel.uni.techbazar.Entity.UserLiked;

import java.util.List;
import java.util.Optional;

public interface UserLikedRepository extends MongoRepository<UserLiked, ObjectId> {

    Optional<UserLiked> findByUserId(String userId);
}
