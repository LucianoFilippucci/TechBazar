package ovh.homecitadel.uni.techbazar.Helper.Model.Cart;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
public class ProductInCart {
    private Long productId;
    private Long productModelId;
    private int qty;
    private BigDecimal productPrice;
    private int iva;
    private String storeId;


    public ProductInCart(Long productId, Long productModelId, int qty, BigDecimal productPrice, int iva, String storeId) {
        this.productId = productId;
        this.productModelId = productModelId;
        this.qty = qty;
        this.iva = iva;
        this.productPrice = productPrice;
        this.storeId = storeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductInCart that = (ProductInCart) o;
        return qty == that.qty && iva == that.iva && Objects.equals(productId, that.productId) && Objects.equals(productModelId, that.productModelId) && Objects.equals(productPrice, that.productPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, qty, productPrice, iva, productModelId);
    }

    public ProductInCart() {
    }
}
