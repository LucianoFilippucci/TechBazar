package ovh.homecitadel.uni.techbazar.Repository.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ovh.homecitadel.uni.techbazar.Entity.User.UserAddressEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddressEntity, Long> {

    List<UserAddressEntity> findByUserId(String id);
    Optional<UserAddressEntity> findByIsDefault(Boolean isDefault);
}
