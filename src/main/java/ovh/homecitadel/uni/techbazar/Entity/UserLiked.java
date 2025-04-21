package ovh.homecitadel.uni.techbazar.Entity;

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
@Document(collection = "user_liked")
public class UserLiked {

    @MongoId
    private ObjectId id;

    private String userId;
    private List<Long> likedReviews;
    private List<String> storeLiked;
}
