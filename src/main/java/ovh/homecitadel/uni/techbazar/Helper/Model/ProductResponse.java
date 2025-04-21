package ovh.homecitadel.uni.techbazar.Helper.Model;

import lombok.Getter;
import lombok.Setter;
import ovh.homecitadel.uni.techbazar.Entity.Product.ProductCategoryEntity;
import ovh.homecitadel.uni.techbazar.Entity.Product.ProductEntity;
import ovh.homecitadel.uni.techbazar.Entity.Product.ProductModelEntity;

import java.math.BigDecimal;
import java.util.ArrayList;

@Getter
@Setter
public class ProductResponse {
    private Long productId;
    private String productName;
    private String productDescription;
    private ProductCategoryEntity categoryEntity;
    private int iva;
    private String store;
    private ArrayList<Model> models;

    private String productBrand;
    private String productCPU;
    private String productAdditionalStorage;
    private String productOS;
    private String productMainCamera;
    private String productFrontCamera;
    private String productAlternativeCamera;
    private String productDisplay;
    private String productSize;
    private boolean hasGPS;
    private boolean hasCellular;
    private BigDecimal productPrice;
    private int productQuantity;


    public static ProductResponse fromProduct(ProductEntity productEntity) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setProductId(productEntity.getProductId());
        productResponse.setProductName(productEntity.getProductName());
        productResponse.setProductDescription(productEntity.getProductDescription());
        productResponse.setCategoryEntity(productEntity.getProductCategory());
        productResponse.setIva(productResponse.getIva());
        productResponse.setStore(productResponse.getStore());
        productResponse.setModels(productResponse.getModels());
        return productResponse;
    }
}
