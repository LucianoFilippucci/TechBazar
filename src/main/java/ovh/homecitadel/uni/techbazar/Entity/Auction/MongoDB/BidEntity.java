package ovh.homecitadel.uni.techbazar.Entity.Auction.MongoDB;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import ovh.homecitadel.uni.techbazar.Helper.Model.Auction.BidModel;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Document(collection = "auction_bids")
public class BidEntity {

    @MongoId
    private ObjectId auctionBidsId;

    private Long auctionId;
    List<BidModel> auctionBids;
}
