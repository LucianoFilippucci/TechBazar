package ovh.homecitadel.uni.techbazar.Service;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ovh.homecitadel.uni.techbazar.Entity.ChatEntity;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.ObjectNotFoundException;
import ovh.homecitadel.uni.techbazar.Helper.Notification.MessageModel;
import ovh.homecitadel.uni.techbazar.Repository.MongoDB.ChatEntityRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ChatService {
    private final ChatEntityRepository chatEntityRepository;

    public ChatService(ChatEntityRepository chatEntityRepository) {
        this.chatEntityRepository = chatEntityRepository;
    }

    @Transactional
    public ChatEntity getChat(String chatId) throws ObjectNotFoundException {
        ObjectId chatIdObj = new ObjectId(chatId);
        Optional<ChatEntity> tmp = chatEntityRepository.findById(chatIdObj);
        if(tmp.isEmpty()) throw new ObjectNotFoundException("Chat Not Found");
        return tmp.get();
    }

    @Transactional
    public MessageModel newChatMessage(MessageModel message) throws ObjectNotFoundException {
        ChatEntity chat;
        if(message.getChatId() == null) {
            // it's a new Chat
            chat = new ChatEntity();
            chat.setCreatedAt(LocalDateTime.now());
            chat.setUsers(List.of(message.getSenderId(), message.getReceiverId()));
            chat.setUpdatedAt(LocalDateTime.now());
            chat.getMessages().add(message);
            this.chatEntityRepository.save(chat);
        } else {
            ObjectId id = new ObjectId(message.getChatId());
            Optional<ChatEntity> tmp = this.chatEntityRepository.findChatEntityByChatId(id);
            if(tmp.isEmpty()) {
                throw new ObjectNotFoundException("Chat should exists, but wel.... it doesn't");
            }
            chat = tmp.get();
        }
        chat.setUpdatedAt(LocalDateTime.now());
        chat.getMessages().add(message);
        this.chatEntityRepository.save(chat);
        // New chat message saved, we should notify the user.
        // The notification can happen in two ways:
        // 1) TODO: RabbitMQ for notifying angular
        // 2) Email
        return message;
    }
}
