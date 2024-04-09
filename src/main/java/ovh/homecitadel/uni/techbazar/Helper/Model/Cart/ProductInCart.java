package ovh.homecitadel.uni.techbazar.Helper.Model.Cart;

import java.math.BigDecimal;
import java.util.Objects;

public class ProductInCart {
    private Long productId;
    private Long productModelId;
    private int qty;
    private BigDecimal productPrice;
    private int iva;


    public ProductInCart(Long productId, Long productModelId, int qty, BigDecimal productPrice, int iva) {
        this.productId = productId;
        this.productModelId = productModelId;
        this.qty = qty;
        this.iva = iva;
        this.productPrice = productPrice;
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

    public int getIva() {
        return iva;
    }

    public void setIva(int iva) {
        this.iva = iva;
    }

    public ProductInCart() {
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public Long getProductModelId() {
        return productModelId;
    }

    public void setProductModelId(Long productModelId) {
        this.productModelId = productModelId;
    }
}
