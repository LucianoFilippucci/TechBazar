package ovh.homecitadel.uni.techbazar.Helper;

import org.springframework.stereotype.Component;
import ovh.homecitadel.uni.techbazar.Entity.Product.ProductCategoryEntity;
import ovh.homecitadel.uni.techbazar.Entity.Product.ProductEntity;
import ovh.homecitadel.uni.techbazar.Entity.Product.ProductModelEntity;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.ObjectNotFoundException;
import ovh.homecitadel.uni.techbazar.Helper.Model.Model;
import ovh.homecitadel.uni.techbazar.Helper.Model.ProductResponse;
import ovh.homecitadel.uni.techbazar.Repository.Product.ProductCategoryRepository;
import ovh.homecitadel.uni.techbazar.Service.Product.ProductCategoryService;
import ovh.homecitadel.uni.techbazar.Service.Product.ProductModelService;
import ovh.homecitadel.uni.techbazar.Service.Product.ProductService;

@Component
public class UnifiedServiceAccess {

    private final ProductCategoryService productCategoryService;

    private final ProductModelService productModelService;
    private final ProductService productService;

    public UnifiedServiceAccess(ProductCategoryService productCategoryService, ProductModelService productModelService, ProductService productService) {
        this.productCategoryService = productCategoryService;
        this.productModelService = productModelService;
        this.productService = productService;
    }

    public ProductCategoryEntity getCategoryById(Long categoryId) throws ObjectNotFoundException {
        return this.productCategoryService.getCategory(categoryId);
    }


    public ProductResponse getProductEntity(Long productId) throws ObjectNotFoundException {
        return this.productService.getById(productId);
    }

    public Model getModelEntity(Long modelId) throws ObjectNotFoundException {
        return this.productModelService.getModel(modelId);
    }


}
