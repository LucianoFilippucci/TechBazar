package ovh.homecitadel.uni.techbazar.Entity.Product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import ovh.homecitadel.uni.techbazar.Entity.CouponEntity;

import java.util.Collection;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product_category", schema = "techbazar")
public class ProductCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Basic
    @Column(name = "category_name", nullable = false)
    private String categoryName;

    @Basic
    @Column(name = "category_description")
    private String categoryDescription;

    @OneToMany(mappedBy = "productCategory")
    @JsonIgnore
    private Collection<ProductEntity> productEntities;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private Collection<CouponEntity> coupons;
}
