package ovh.homecitadel.uni.techbazar.Repository.Auction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ovh.homecitadel.uni.techbazar.Entity.Auction.AuctionEntity;
import ovh.homecitadel.uni.techbazar.Helper.Model.Auction.AuctionStatus;

import java.util.List;

@Repository
public interface AuctionRepository extends JpaRepository<AuctionEntity, Long> {
    List<AuctionEntity> findByAuctionStatus(AuctionStatus status);
    List<AuctionEntity> findByWinnerId(String winnerId);
}
