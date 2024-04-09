package ovh.homecitadel.uni.techbazar.Helper.Notification;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDetails {
    private String from; // StoreId-UserId
    private String subject;
    private String message;
    private boolean isRead;
}
