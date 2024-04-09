package ovh.homecitadel.uni.techbazar.Entity.Order.MongoDB;

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
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Document(collection = "order_details")
public class OrderDetailsEntity {

    @MongoId
    private ObjectId orderDetailsId;

    private List<ProductInCart> products;
    private BigDecimal total;
    private String orderId;
}
