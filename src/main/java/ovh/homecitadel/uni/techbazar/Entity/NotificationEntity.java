package ovh.homecitadel.uni.techbazar.Entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import ovh.homecitadel.uni.techbazar.Helper.Notification.NotificationDetails;

import java.util.Collection;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "notifications")
public class NotificationEntity {
    @MongoId
    private ObjectId notificationId;
    private String userId;

    Collection<NotificationDetails> notificationList;
}
