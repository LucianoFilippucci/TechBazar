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
@Data
public class MessageModel {
    private String receiverId;
    private String message;
    private String subject;
    private String attachments;
    private String senderId;
    private MessageTypeEnum messageType;
    private LocalDateTime createdAt;
    private String chatId;
    private boolean read;
}
