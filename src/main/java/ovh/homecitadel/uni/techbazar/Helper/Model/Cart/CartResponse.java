package ovh.homecitadel.uni.techbazar.Helper.Model.Cart;

import lombok.*;
import ovh.homecitadel.uni.techbazar.Helper.Model.ProductInCartResponse;

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
    private List<ProductInCartResponse> products;
    private int cartElements;
    private BigDecimal cartTotalAfterCoupons;
    private List<String> coupons;
}
