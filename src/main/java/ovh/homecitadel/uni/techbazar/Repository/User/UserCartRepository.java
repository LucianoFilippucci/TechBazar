package ovh.homecitadel.uni.techbazar.Repository.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ovh.homecitadel.uni.techbazar.Entity.User.UserCartEntity;

@Repository
public interface UserCartRepository extends JpaRepository<UserCartEntity, String> {
}
