package ovh.homecitadel.uni.techbazar.Helper.Model.Request;

import lombok.Getter;
import lombok.Setter;
import ovh.homecitadel.uni.techbazar.Entity.Product.ProductModelEntity;

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
}
