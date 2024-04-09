package ovh.homecitadel.uni.techbazar.Repository.User.MongoDB;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ovh.homecitadel.uni.techbazar.Entity.User.MongoDB.CartEntity;

import java.util.Optional;

@Repository
public interface CartRepository extends MongoRepository<CartEntity, String> {

    Optional<CartEntity> findByCartId(String cartId);

}
