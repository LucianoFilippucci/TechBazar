package ovh.homecitadel.uni.techbazar.Entity.User.MongoDB;


import org.springframework.cglib.core.Local;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import ovh.homecitadel.uni.techbazar.Entity.CouponEntity;
import ovh.homecitadel.uni.techbazar.Helper.Model.Cart.ProductInCart;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Document(collection = "cart")
public class CartEntity {

    @MongoId
    private String cartId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<ProductInCart> products;

    private List<String> coupons;

    public void setCoupons(List<String> coupons) {
        this.coupons = coupons;
    }

    public List<String> getCoupons() {
        return this.coupons;
    }

    public String getCartId() { return this.cartId; }
    public LocalDateTime getCreatedAt() { return this.createdAt; }
    public LocalDateTime getUpdatedAt() { return this.updatedAt; }

    public List<ProductInCart> getProducts() { return this.products; }

    public void setCartId(String cartId) { this.cartId = cartId; }
    public void setProducts(List<ProductInCart> products) { this.products = products; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public CartEntity(String cartId, LocalDateTime createdAt, LocalDateTime updatedAt, List<ProductInCart> products) {
        this.cartId = cartId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.products = products;
    }

    public CartEntity() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartEntity that = (CartEntity) o;
        return Objects.equals(cartId, that.cartId) && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt) && Objects.equals(products, that.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cartId, createdAt, updatedAt, products);
    }

    @Override
    public String toString() {
        return "CartEntity{" +
                "cartId='" + cartId + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", products=" + products +
                '}';
    }
}
