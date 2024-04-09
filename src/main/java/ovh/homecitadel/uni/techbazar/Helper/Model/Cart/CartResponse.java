package ovh.homecitadel.uni.techbazar.Helper.Model.Cart;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {
    private BigDecimal cartTotal;
    private BigDecimal taxTotal;
    private List<ProductInCart> products;
    private int cartElements;
    private BigDecimal cartTotalAfterCoupons;
    private List<String> coupons;
}
