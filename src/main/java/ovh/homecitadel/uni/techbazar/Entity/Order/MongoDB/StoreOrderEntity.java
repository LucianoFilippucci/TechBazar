package ovh.homecitadel.uni.techbazar.Entity.Order.MongoDB;

import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import ovh.homecitadel.uni.techbazar.Helper.Model.Cart.ProductInCart;
import ovh.homecitadel.uni.techbazar.Helper.Model.ProductInPurchase;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Document(collection = "store_order_details")
public class StoreOrderEntity {

    @MongoId
    private ObjectId storeOrderId;

    private String orderId;
    private List<ProductInCart> products;
    private BigDecimal total;

    private String storeId;
    private LocalDateTime orderDate;
    private String userAddress;
}
