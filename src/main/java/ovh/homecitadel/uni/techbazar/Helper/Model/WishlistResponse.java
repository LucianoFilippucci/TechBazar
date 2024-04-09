package ovh.homecitadel.uni.techbazar.Helper.Model;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import ovh.homecitadel.uni.techbazar.Entity.Auction.AuctionEntity;
import ovh.homecitadel.uni.techbazar.Entity.Product.ProductEntity;

import java.util.List;

@Getter
@Setter
public class WishlistResponse {
    private ObjectId wishlistId;
    private String userId;
    private List<ProductEntity> products;
    private List<AuctionEntity> auctions;
}
