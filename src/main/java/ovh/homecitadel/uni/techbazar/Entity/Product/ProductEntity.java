package ovh.homecitadel.uni.techbazar.Entity.Product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import ovh.homecitadel.uni.techbazar.Entity.DailyOfferEntity;

import java.math.BigDecimal;
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

    @Basic
    @Column(name = "product_cpu")
    private String productCpu;

    @Basic
    @Column(name = "product_additional_storage")
    private String productAdditionalStorage;


    @Basic
    @Column(name = "product_os")
    private String productOs;

    @Basic
    @Column(name = "product_main_camera")
    private String productMainCamera;

    @Basic
    @Column(name = "product_front_camera")
    private String productFrontCamera;

    @Basic
    @Column(name = "product_alternative_camera")
    private String productAlternativeCamera;

    @Basic
    @Column(name = "product_display")
    private String productDisplay;

    @Basic
    @Column(name = "product_size")
    private String productSize;

    @Basic
    @Column(name = "product_gps")
    private boolean productHasGps;

    @Basic
    @Column(name = "product_cellular")
    private boolean productHasCellular;

    @Basic
    @Column(name = "product_sold_qty")
    private int productTotalSold;

    @Basic
    @Column(name = "product_quantity")
    private int productQuantity;

    @Basic
    @Column(name = "product_price")
    private BigDecimal productPrice;

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
