package ovh.homecitadel.uni.techbazar.Helper.Notification;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Objects;

@Service
public class MailerService implements EmailService{
    private final JavaMailSender javaMailSender;

    public MailerService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public boolean sendSimpleMail(MessageModel messageModel) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom(messageModel.getSender());
            mailMessage.setTo(messageModel.getRecipient());
            mailMessage.setText(messageModel.getMsgBody());
            mailMessage.setSubject(messageModel.getSubject());

            this.javaMailSender.send(mailMessage);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String sendMailWithAttachments(MessageModel messageModel) {
        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        //TODO: edit the attachment to be firstly in a tmp folder then sent.
        try {
            // Set multipart to true for attachment to be sent
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(messageModel.getSender());
            mimeMessageHelper.setTo(messageModel.getRecipient());
            mimeMessageHelper.setText(messageModel.getMsgBody());
            mimeMessageHelper.setSubject(messageModel.getSubject());

            FileSystemResource file = new FileSystemResource(new File(messageModel.getAttachments()));
            mimeMessageHelper.addAttachment(Objects.requireNonNull(file.getFilename()), file);
            this.javaMailSender.send(mimeMessage);
            return "Mail Sent";
        } catch (MessagingException e) {
            return "Error";
        }
    }
}
