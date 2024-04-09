package ovh.homecitadel.uni.techbazar.Helper.Notification;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessageModel {
    private String recipient;
    private String msgBody;
    private String subject;
    private String attachments;
    private String sender;
}
