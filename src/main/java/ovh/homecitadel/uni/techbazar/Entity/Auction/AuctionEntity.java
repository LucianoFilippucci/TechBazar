package ovh.homecitadel.uni.techbazar.Entity.Auction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import ovh.homecitadel.uni.techbazar.Entity.Product.ProductModelEntity;
import ovh.homecitadel.uni.techbazar.Helper.Model.Auction.AuctionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "auction")
public class AuctionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auction_id", nullable = false)
    private Long auctionId;

    @Basic
    @Column(name = "start_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal startPrice;

    @Basic
    @Column(name = "final_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal finalPrice;

    @Basic
    @Column(name = "store_id", nullable = false)
    private String storeId;

    @Basic
    @Column(name = "winner")
    private String winnerId;

    @Basic
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Basic
    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "auction_status", nullable = false)
    private AuctionStatus auctionStatus;

    @Version
    @Column(name = "version")
    private Long version;

    @ManyToOne
    @JoinColumn(name = "product_model")
    @JsonIgnore
    private ProductModelEntity model;
}
