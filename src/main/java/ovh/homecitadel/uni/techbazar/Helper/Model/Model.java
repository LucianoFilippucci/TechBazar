package ovh.homecitadel.uni.techbazar.Helper.Model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Model {
    private String productRam;
    private String productCPU;
    private String productMainStorage;
    private String productAdditionalStorage;
    private String productOS;
    private String productMainCamera;
    private String productFrontCamera;
    private String productAlternativeCamera;
    private String productColor;
    private String productDisplay;
    private String productSize;
    private boolean hasGPS;
    private boolean hasCellular;
    private int productQuantity;
    private BigDecimal productPrice;

}
