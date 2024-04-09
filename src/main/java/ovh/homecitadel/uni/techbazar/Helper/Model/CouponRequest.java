package ovh.homecitadel.uni.techbazar.Helper.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import ovh.homecitadel.uni.techbazar.Entity.Product.ProductCategoryEntity;

import java.time.LocalDateTime;

@Getter
@Setter
public class CouponRequest {
    private String couponCode;
    private int discount;
    private String storeId;
    private int timesUsed;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expiration;
    private int maxUse;
    private Long categoryId;
}
