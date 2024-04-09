package ovh.homecitadel.uni.techbazar.Entity.Product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import ovh.homecitadel.uni.techbazar.Entity.Auction.AuctionEntity;
import ovh.homecitadel.uni.techbazar.Entity.DailyOfferEntity;

import java.math.BigDecimal;
import java.util.Collection;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_model", schema = "techbazar")
public class ProductModelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_info_id", nullable = false)
    private Long productModelId;

    @Basic
    @Column(name = "product_ram")
    private String productRam;

    @Basic
    @Column(name = "product_cpu")
    private String productCPU;

    @Basic
    @Column(name = "product_main_storage")
    private String productMainStorage;

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
    @Column(name = "product_color")
    private String productColor;

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
    @Column(name = "product_total_sold")
    private int productTotalSold;

    @Basic
    @Column(name = "product_quantity")
    private int productQuantity;

    @Basic
    @Column(name = "product_price")
    private BigDecimal productPrice;

    @Version
    @Column(name = "version_id")
    private Long version;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity productEntity;

    @OneToMany(mappedBy = "model", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<AuctionEntity> auctions;

    @OneToMany(mappedBy = "model", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<DailyOfferEntity> dailyOffer;
}
