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
    @Column(name = "config_color", nullable = false, length = 6)
    private String configColor;

    @Basic
    @Column(name = "config_qty", nullable = false)
    private int configQty;

    @Basic
    @Column(name = "config_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal configPrice;

    @Basic
    @Column(name = "configuration", length = 10, nullable = false)
    private String configuration;

    @Basic
    @Column(name = "config_sold_qty", nullable = false)
    private int configSoldQty;

    @Version
    @Column(name = "version_id")
    private Long version;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private ProductEntity productEntity;

    @OneToMany(mappedBy = "model", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<AuctionEntity> auctions;

    @OneToMany(mappedBy = "model", fetch = FetchType.LAZY)
    @JsonIgnore
    private Collection<DailyOfferEntity> dailyOffer;
}
