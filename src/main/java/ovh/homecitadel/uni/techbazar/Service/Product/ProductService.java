package ovh.homecitadel.uni.techbazar.Service.Product;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ovh.homecitadel.uni.techbazar.Entity.Product.ProductCategoryEntity;
import ovh.homecitadel.uni.techbazar.Entity.Product.ProductEntity;
import ovh.homecitadel.uni.techbazar.Entity.Product.ProductModelEntity;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.ObjectNotFoundException;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.UnauthorizedAccessException;
import ovh.homecitadel.uni.techbazar.Helper.Model.Request.NewProductRequest;
import ovh.homecitadel.uni.techbazar.Helper.Model.ResponseModel;
import ovh.homecitadel.uni.techbazar.Helper.UnifiedServiceAccess;
import ovh.homecitadel.uni.techbazar.Repository.Product.ProductCategoryRepository;
import ovh.homecitadel.uni.techbazar.Repository.Product.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductCategoryRepository productCategoryRepository;



    public ProductService(ProductRepository productRepository, ProductCategoryRepository productCategoryRepository) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
    }


    @Transactional(readOnly = true)
    public ProductEntity getById(Long id) throws ObjectNotFoundException {
        Optional<ProductEntity> productEntity = productRepository.findByProductId(id);
        if (productEntity.isPresent())
            return productEntity.get();
        throw new ObjectNotFoundException("Product Not Found");
    }

    @Transactional
    public ProductEntity newProduct(NewProductRequest productRequest) throws ObjectNotFoundException {

        Optional<ProductCategoryEntity> tmp = this.productCategoryRepository.findByCategoryId(productRequest.getProductCategory());
        if(tmp.isEmpty()) throw new ObjectNotFoundException("Category not found");

        ProductEntity productEntity = new ProductEntity();

        productEntity.setProductBrand(productRequest.getProductBrand());
        productEntity.setProductDescription(productRequest.getProductDescription());
        productEntity.setProductCategory(tmp.get());
        productEntity.setProductName(productRequest.getProductName());
        productEntity.setProductIva(productRequest.getProductIva());
        productEntity.setStoreId(productRequest.getStoreId());

        return this.productRepository.save(productEntity);
    }

    @Transactional
    public void deleteProduct(Long productId, String storeId) throws ObjectNotFoundException, UnauthorizedAccessException {
        Optional<ProductEntity> tmp = this.productRepository.findByProductId(productId);
        if(tmp.isEmpty()) throw new ObjectNotFoundException("Product Not Found");

        ProductEntity product = tmp.get();
        if(product.getStoreId().equals(storeId))
            this.productRepository.delete(product);
        else
            throw new UnauthorizedAccessException("This product is not yours.");
    }
}
