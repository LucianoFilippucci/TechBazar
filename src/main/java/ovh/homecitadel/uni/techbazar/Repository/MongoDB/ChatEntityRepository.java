package ovh.homecitadel.uni.techbazar.Repository.MongoDB;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ovh.homecitadel.uni.techbazar.Entity.ChatEntity;

import java.util.Optional;

@Repository
public interface ChatEntityRepository extends MongoRepository<ChatEntity, ObjectId> {
    Optional<ChatEntity> findChatEntityByChatId(ObjectId chatId);
    Optional<ChatEntity> findChatEntityByUsersContains(String user);

}
