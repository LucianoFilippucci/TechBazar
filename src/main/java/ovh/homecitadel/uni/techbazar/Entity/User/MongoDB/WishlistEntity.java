package ovh.homecitadel.uni.techbazar.Entity.User.MongoDB;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Document(collection = "user_wishlist")
public class WishlistEntity {

    @MongoId
    private ObjectId wishlistId;
    private String userId;
    private List<Long> products;
    private List<Long> auctionSaved;
}
