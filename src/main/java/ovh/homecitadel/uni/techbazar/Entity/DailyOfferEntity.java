package ovh.homecitadel.uni.techbazar.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import ovh.homecitadel.uni.techbazar.Entity.Product.ProductEntity;
import ovh.homecitadel.uni.techbazar.Entity.Product.ProductModelEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "daily_offer", schema = "techbazar")
public class DailyOfferEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "daily_offer_id", nullable = false)
    private Long dailyOfferId;

    @Basic
    @Column(name = "discount", nullable = false)
    private int discount;

    @Basic
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Version
    @Column(name = "version")
    private Long version;

    @Basic
    @Column(name = "store_id", nullable = false)
    private String storeId;


    // if only the product is set -> the discount is for all the models. if only the model is set -> the discount is valid only for that model.

    @ManyToOne
    @JoinColumn(name = "product_id")
    ProductEntity product;

    @ManyToOne
    @JoinColumn(name = "product_model_id")
    ProductModelEntity model;
}
