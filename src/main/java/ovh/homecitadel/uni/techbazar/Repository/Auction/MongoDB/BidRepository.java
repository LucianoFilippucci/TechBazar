package ovh.homecitadel.uni.techbazar.Repository.Auction.MongoDB;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ovh.homecitadel.uni.techbazar.Entity.Auction.MongoDB.BidEntity;

import java.util.Optional;

@Repository
public interface BidRepository extends MongoRepository<BidEntity, ObjectId> {
    Optional<BidEntity> findByAuctionId(Long auctionId);
}
