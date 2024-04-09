package ovh.homecitadel.uni.techbazar.Service;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ovh.homecitadel.uni.techbazar.Entity.NotificationEntity;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.ObjectNotFoundException;
import ovh.homecitadel.uni.techbazar.Helper.Model.User.User;
import ovh.homecitadel.uni.techbazar.Helper.Notification.MessageModel;
import ovh.homecitadel.uni.techbazar.Helper.Notification.MailerService;
import ovh.homecitadel.uni.techbazar.Helper.Notification.NotificationDetails;
import ovh.homecitadel.uni.techbazar.Repository.MongoDB.NotificationRepository;
import ovh.homecitadel.uni.techbazar.Security.KeycloakSecurityUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final MailerService mailerService;
    private final KeycloakSecurityUtil keycloakSecurityUtil;

    @Value("${spring.mail.username}")
    String from;

    @Value("${realm}")
    String realm;

    String to;

    public NotificationService(NotificationRepository notificationRepository, MailerService mailerService, KeycloakSecurityUtil keycloakSecurityUtil) {
        this.notificationRepository = notificationRepository;
        this.mailerService = mailerService;
        this.keycloakSecurityUtil = keycloakSecurityUtil;
    }

    public int totalUnreadNotifications(String userId) {
        Optional<NotificationEntity> tmp = this.notificationRepository.findNotificationEntitiesByUserId(userId);
        //if(tmp.isEmpty()) throw new ObjectNotFoundException("Notifications Not Found");
        // TODO: we can't just throw an error... we should understand why it's empty
        if(tmp.isEmpty()) {
            NotificationEntity notification = new NotificationEntity();
            notification.setNotificationList(new ArrayList<>());
            notification.setUserId(userId);
            this.notificationRepository.save(notification);
            return 0;
        }
        Collection<NotificationDetails> detailsList = tmp.get().getNotificationList();

        AtomicInteger total = new AtomicInteger(); // since vars in lambda should be final we just use Atomic
        detailsList.forEach(det -> {
            if(!det.isRead()) total.getAndIncrement();
        });

        return total.get();
    }



    @Transactional
    public boolean sendMessage(MessageModel messageModel) throws ObjectNotFoundException {
        NotificationDetails details = new NotificationDetails(messageModel.getSender(), messageModel.getSubject(), messageModel.getMsgBody(), false);

        if(messageModel.getSender() != null) {
            // from a user or store
            from = this.getUserMail(messageModel.getSender());
            //from = this.getUserMail(messageModel.getSender());
        } // else from the system. nothing changed.


        to = this.getUserMail(messageModel.getRecipient());
        Optional<NotificationEntity> tmp = this.notificationRepository.findNotificationEntitiesByUserId(messageModel.getRecipient());
        if(tmp.isEmpty()) throw new ObjectNotFoundException("Notification Repo Not Found");
        tmp.get().getNotificationList().add(details);
        this.notificationRepository.save(tmp.get());
        //TODO: Send rabbitMQ notification to angular
        messageModel.setAttachments("");
        messageModel.setRecipient(to);
        messageModel.setSender(from);
        return this.mailerService.sendSimpleMail(messageModel);
    }


    private String getUserMail(String userId) {
        Keycloak keycloak = this.keycloakSecurityUtil.getKeycloakInstance();

        UserResource userR = keycloak.realm(realm).users().get(userId);

        return userR.toRepresentation().getEmail();
    }
}
