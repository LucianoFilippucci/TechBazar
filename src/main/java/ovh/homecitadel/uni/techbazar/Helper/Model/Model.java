package ovh.homecitadel.uni.techbazar.Helper.Model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Model {
    private Long modelId;
    private String configColor;
    private int configQty;
    private BigDecimal configPrice;
    private String configuration; // RAM | ROM
    private int configSoldQty;
}
