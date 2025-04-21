package ovh.homecitadel.uni.techbazar.Service;

import org.bson.types.ObjectId;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ovh.homecitadel.uni.techbazar.Entity.ChatEntity;
import ovh.homecitadel.uni.techbazar.Entity.NotificationEntity;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.ObjectNotFoundException;
import ovh.homecitadel.uni.techbazar.Helper.Notification.MailerService;
import ovh.homecitadel.uni.techbazar.Helper.Notification.MessageModel;
import ovh.homecitadel.uni.techbazar.Helper.Notification.NotificationDetails;
import ovh.homecitadel.uni.techbazar.Repository.MongoDB.ChatEntityRepository;
import ovh.homecitadel.uni.techbazar.Repository.MongoDB.NotificationRepository;
import ovh.homecitadel.uni.techbazar.Security.KeycloakSecurityUtil;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationSystem {
    private final NotificationRepository notificationRepository;
    private final MailerService mailerService;
    private final KeycloakSecurityUtil keycloakSecurityUtil;
    private final ChatService chatService;

    @Value("${spring.mail.username}")
    String senderEmail;

    @Value("${realm}")
    String realm;

    public NotificationSystem(NotificationRepository notificationRepository, MailerService mailerService, KeycloakSecurityUtil keycloakSecurityUtil, ChatService chatService) {
        this.notificationRepository = notificationRepository;
        this.mailerService = mailerService;
        this.keycloakSecurityUtil = keycloakSecurityUtil;
        this.chatService = chatService;
    }

    public List<MessageModel> getUserNotifications(String userId) throws ObjectNotFoundException {
        Optional<NotificationEntity> tmp = this.notificationRepository.findByUserId(userId);
        if(tmp.isEmpty()) throw new ObjectNotFoundException("Can't find a Notification List for the given user {" + userId + "}");
        NotificationEntity notification = tmp.get();
        return notification.getNotificationList().stream().toList();
    }


    @Transactional
    public int getUnreadNotifications(String userId) throws ObjectNotFoundException{
        Optional<NotificationEntity> tmp = this.notificationRepository.findNotificationEntitiesByUserId(userId);
        if(tmp.isEmpty()) {
            // It shouldn't be empty... What happened?
            throw new ObjectNotFoundException("An Error occurred. Try again later, or contact the assistance");
        }
        NotificationEntity notification = tmp.get();
        int unreadNotification = 0;
        for(MessageModel msg : notification.getNotificationList()) {
            if(!msg.isRead())
                unreadNotification++;
        }

        return unreadNotification;
    }

    @Transactional
    public void createNotification(MessageModel message) throws ObjectNotFoundException {
        NotificationEntity notification;
        Optional<NotificationEntity> tmp = this.notificationRepository.findNotificationEntitiesByUserId(message.getReceiverId());
        if(tmp.isEmpty()) {
            NotificationEntity not = new NotificationEntity();
            not.setNotificationList(new ArrayList<>());
            not.setUserId(message.getReceiverId());
            notification = this.notificationRepository.save(not);
            // throw new ObjectNotFoundException("An Error occurred. Try again later");
        } else
            notification = tmp.get();
        message.setCreatedAt(LocalDateTime.now());
        message.setRead(false);
        notification.getNotificationList().add(message);


        switch(message.getMessageType()) {
            case CHAT -> this.newEmail(this.chatService.newChatMessage(message));
            case SAVED -> newSavedNotification(message);
            case CART -> newCartNotification(message);
            case EMAIL -> newEmail(message);
            case AUCTION -> newAuctionNotification(message);
        }
        this.notificationRepository.save(notification);
    }



    private void newSavedNotification(MessageModel message) {

    }

    private void newCartNotification(MessageModel message) {

    }

    private void newEmail(MessageModel message) {
        String receiverEmail = this.getUserMail(message.getReceiverId());
        if(message.getSenderId() != null)
            this.senderEmail = this.getUserMail(message.getSenderId());

        message.setSenderId(senderEmail);
        message.setReceiverId(receiverEmail);
        message.setAttachments("");
        this.mailerService.sendSimpleMail(message);
    }

    private void newAuctionNotification(MessageModel message) {
        // Well we just need to send an email, since we already set notification in the main method, ah and we should implement RabbitMQ here too
        this.newEmail(message);
    }

    private String getUserMail(String userId) {
        Keycloak keycloak = this.keycloakSecurityUtil.getKeycloakInstance();

        UserResource userR = keycloak.realm(realm).users().get(userId);

        return userR.toRepresentation().getEmail();
    }
}
