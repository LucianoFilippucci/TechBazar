package ovh.homecitadel.uni.techbazar.Helper.Notification;

import lombok.*;
import ovh.homecitadel.uni.techbazar.Helper.MessageTypeEnum;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDetails {
    private String from; // StoreId-UserId
    private String receiverId;
    private String subject;
    private String message;
    private boolean isRead;
    private MessageTypeEnum messageType;
    private LocalDateTime sentTime;
}
