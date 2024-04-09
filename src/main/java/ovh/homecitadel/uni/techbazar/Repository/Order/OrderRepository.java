package ovh.homecitadel.uni.techbazar.Repository.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ovh.homecitadel.uni.techbazar.Entity.Order.OrderEntity;

import java.util.Collection;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, String> {

    Collection<OrderEntity> findOrderEntitiesByUserId(String userId);
}
