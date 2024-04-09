package ovh.homecitadel.uni.techbazar.Helper.Notification;

public interface EmailService {
    boolean sendSimpleMail(MessageModel messageModel);
    String sendMailWithAttachments(MessageModel messageModel);
}
