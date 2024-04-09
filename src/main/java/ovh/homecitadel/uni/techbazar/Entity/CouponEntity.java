package ovh.homecitadel.uni.techbazar.Entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ovh.homecitadel.uni.techbazar.Entity.Product.ProductCategoryEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "coupon", schema = "techbazar")
public class CouponEntity {

    @Id
    @Column(name = "code", nullable = false)
    private String couponId;

    @Basic
    @Column(name = "discount", nullable = false)
    private int discount;

    @Basic
    @Column(name = "store_id", nullable = false)
    private String storeId;

    @Basic
    @Column(name = "times_used", nullable = false)
    private int timesUsed;

    @Basic
    @Column(name = "expiration_date")
    private LocalDateTime expiration; // null = no expiration

    @Basic
    @Column(name = "max_use")
    private int maxUse; // 0 = infinite

    @ManyToOne
    @JoinColumn(name = "category")
    private ProductCategoryEntity category;

    @Version
    @Column(name = "version")
    private Long version;

}
