package ovh.homecitadel.uni.techbazar.Helper.Model.Request;

import lombok.Getter;
import lombok.Setter;
import ovh.homecitadel.uni.techbazar.Entity.Product.ProductModelEntity;

import java.math.BigDecimal;
import java.util.Collection;

@Getter
@Setter
public class NewProductRequest {
    protected String productName;
    protected String productDescription;
    protected Long productCategory;
    protected String storeId;
    protected int productIva;
    protected String productBrand;
    protected String productCPU;
    protected String productAdditionalStorage;
    protected String productOS;
    protected String productMainCamera;
    protected String productFrontCamera;
    protected String productAlternativeCamera;
    protected String productDisplay;
    protected String productSize;
    protected boolean hasGPS;
    protected boolean hasCellular;
    protected BigDecimal productPrice;
    protected int productQuantity;
}
