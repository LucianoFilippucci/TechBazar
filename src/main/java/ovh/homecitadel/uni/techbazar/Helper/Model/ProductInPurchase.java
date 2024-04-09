package ovh.homecitadel.uni.techbazar.Helper.Model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductInPurchase {
    private String productName;
    private int productId;
    private int productModelId;
    private int quantity;
    private BigDecimal unitaryPrice;
    private BigDecimal totalPrice;
}
