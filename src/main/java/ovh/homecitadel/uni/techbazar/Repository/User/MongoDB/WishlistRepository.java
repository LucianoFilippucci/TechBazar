package ovh.homecitadel.uni.techbazar.Repository.User.MongoDB;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ovh.homecitadel.uni.techbazar.Entity.User.MongoDB.WishlistEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends MongoRepository<WishlistEntity, ObjectId> {

    Optional<WishlistEntity> findByUserId(String userId);

    Collection<WishlistEntity> findAllByAuctionSavedContains(Long auctionSaved);
}
