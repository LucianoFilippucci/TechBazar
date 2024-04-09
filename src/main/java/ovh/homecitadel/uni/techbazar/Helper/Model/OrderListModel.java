package ovh.homecitadel.uni.techbazar.Helper.Model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ovh.homecitadel.uni.techbazar.Entity.User.UserAddressEntity;
import ovh.homecitadel.uni.techbazar.Helper.Model.Cart.ProductInCart;
import ovh.homecitadel.uni.techbazar.Helper.OrderStatusEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class OrderListModel {

    private String orderId;
    private LocalDateTime orderDate;
    private LocalDateTime lastUpdate;
    private OrderStatusEnum orderStatus;
    private String userId;
    private String shippingAddr;
    private BigDecimal orderTotal;
    private String contactInfo;
    private String note;
    private String trackingCode;
    private String express;
    private List<ProductInCart> products;

}
