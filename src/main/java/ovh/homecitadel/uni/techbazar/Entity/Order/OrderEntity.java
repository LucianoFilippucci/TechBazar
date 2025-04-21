package ovh.homecitadel.uni.techbazar.Entity.Order;

import jakarta.persistence.*;
import lombok.*;
import ovh.homecitadel.uni.techbazar.Helper.OrderStatusEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_order", schema = "techbazar")
public class OrderEntity {

    @Id
    @Column(name = "order_id", nullable = false, length = 255)
    private String orderId;

    @Basic
    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Basic
    @Column(name = "order_updated_at", nullable = false)
    private LocalDateTime orderUpdatedAt;

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatusEnum orderStatus;

    @Basic
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Basic
    @Column(name = "shipping_address", nullable = false)
    private String shippingAddress;

    @Basic
    @Column(name = "order_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal orderTotal;

    @Basic
    @Column(name = "contact_info")
    private String contactInfo;

    @Basic
    @Column(name = "note")
    private String note;

    @Basic
    @Column(name = "tracking_code")
    private String trackingCode;

    @Basic
    @Column(name = "express_courier")
    private String express;


}
