package ovh.homecitadel.uni.techbazar.Helper.Model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ProductInCartResponse {

    private Long productId;
    private Long modelId;
    private String modelConfiguration;
    private String productName;
    private String productColor;
    private int qty;
    private BigDecimal productPrice;
    private int iva;
    private String storeId;

    public ProductInCartResponse(Long productId, Long modelId, String modelConfiguration, String productName, String productColor, int qty, BigDecimal productPrice, int iva, String storeId) {
        this.productId = productId;
        this.modelId = modelId;
        this.modelConfiguration = modelConfiguration;
        this.productName = productName;
        this.productColor = productColor;
        this.qty = qty;
        this.productPrice = productPrice;
        this.iva = iva;
        this.storeId = storeId;

    }
}
