package ovh.homecitadel.uni.techbazar.Entity.Product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import ovh.homecitadel.uni.techbazar.Entity.DailyOfferEntity;

import java.util.Collection;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product", schema = "techbazar")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Basic
    @Column(name = "product_name", nullable = false)
    private String productName;

    @Basic
    @Column(name = "product_description", nullable = false)
    private String productDescription;

    @ManyToOne
    @JoinColumn(name = "product_category")
    private ProductCategoryEntity productCategory;

    @Basic
    @Column(name = "product_iva", nullable = false)
    private int productIva;

    @Basic
    @Column(name = "product_brand", nullable = false)
    private String productBrand;

    @Basic
    @Column(name = "store_id", nullable = false)
    private String storeId;

    @Version
    @Column(name = "version_id")
    private Long version;

    @OneToMany(mappedBy = "productEntity", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<ProductModelEntity> productModels;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<ProductReviewEntity> productReviews;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<DailyOfferEntity> dailyOffer;
}
