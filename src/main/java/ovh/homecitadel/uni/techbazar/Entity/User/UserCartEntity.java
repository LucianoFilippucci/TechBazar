package ovh.homecitadel.uni.techbazar.Entity.User;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_cart", schema = "techbazar")
public class UserCartEntity {

    @Id
    @Column(name = "cart_id")
    private String cartId;

    @Basic
    @Column(name = "user_id")
    private String userId;
}
