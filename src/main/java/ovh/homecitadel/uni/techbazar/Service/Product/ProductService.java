package ovh.homecitadel.uni.techbazar.Service.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ovh.homecitadel.uni.techbazar.Entity.Product.ProductCategoryEntity;
import ovh.homecitadel.uni.techbazar.Entity.Product.ProductEntity;
import ovh.homecitadel.uni.techbazar.Entity.Product.ProductModelEntity;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.ObjectNotFoundException;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.UnauthorizedAccessException;
import ovh.homecitadel.uni.techbazar.Helper.Model.EditProductRequestModel;
import ovh.homecitadel.uni.techbazar.Helper.Model.Model;
import ovh.homecitadel.uni.techbazar.Helper.Model.ProductResponse;
import ovh.homecitadel.uni.techbazar.Helper.Model.Request.NewProductRequest;
import ovh.homecitadel.uni.techbazar.Helper.Model.ResponseModel;
import ovh.homecitadel.uni.techbazar.Helper.UnifiedServiceAccess;
import ovh.homecitadel.uni.techbazar.Repository.Product.ProductCategoryRepository;
import ovh.homecitadel.uni.techbazar.Repository.Product.ProductModelRepository;
import ovh.homecitadel.uni.techbazar.Repository.Product.ProductRepository;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductCategoryRepository productCategoryRepository;
    private final ProductModelRepository productModelRepository;


    public ProductService(ProductRepository productRepository, ProductCategoryRepository productCategoryRepository, ProductModelRepository productModelRepository) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.productModelRepository = productModelRepository;
    }


    @Transactional(readOnly = true)
    public ProductResponse getById(Long id) throws ObjectNotFoundException {
        Optional<ProductEntity> tmp = productRepository.findByProductId(id);
        if (tmp.isPresent()) {
            ProductEntity product = tmp.get();
            ProductResponse response = makeProductResponse(product);
            response.setModels(this.fromEntityToModel(productModelRepository.findByProductEntity(product)));
            return response;
        }
        throw new ObjectNotFoundException("Product Not Found");
    }

    private ArrayList<Model> fromEntityToModel(ArrayList<ProductModelEntity> modelEntities) {
        ArrayList<Model> models = new ArrayList<>();
        for(ProductModelEntity modelEntity : modelEntities) {
            Model model = new Model();
            model.setConfiguration(modelEntity.getConfiguration());
            model.setConfigQty(modelEntity.getConfigQty());
            model.setConfigPrice(modelEntity.getConfigPrice());
            model.setConfigColor(modelEntity.getConfigColor());
            model.setConfigSoldQty(modelEntity.getConfigSoldQty());
            model.setModelId(modelEntity.getProductModelId());
            models.add(model);
        }

        return models;
    }

    @Transactional
    public Page<ProductEntity> searchBox(String keyword, Pageable pageable) {
        return this.productRepository.findProductEntitiesByProductNameContaining(keyword, pageable);

    }

    @Transactional
    public ProductResponse newProduct(NewProductRequest productRequest, String storeId) throws ObjectNotFoundException {

        Optional<ProductCategoryEntity> tmp = this.productCategoryRepository.findByCategoryId(productRequest.getProductCategory());
        if(tmp.isEmpty()) throw new ObjectNotFoundException("Category not found");

        ProductEntity productEntity = new ProductEntity();

        productEntity.setProductDescription(productRequest.getProductDescription());
        productEntity.setProductCategory(tmp.get());
        productEntity.setProductName(productRequest.getProductName());
        productEntity.setProductIva(productRequest.getProductIva());
        productEntity.setStoreId(storeId);
        productEntity.setProductCpu(productRequest.getProductCPU());
        productEntity.setProductOs(productRequest.getProductOS());
        productEntity.setProductPrice(productRequest.getProductPrice());
        productEntity.setProductQuantity(productRequest.getProductQuantity());
        productEntity.setProductBrand(productRequest.getProductBrand());
        productEntity.setProductDescription(productRequest.getProductDescription());
        productEntity.setProductIva(productRequest.getProductIva());
        productEntity.setProductAdditionalStorage(productRequest.getProductAdditionalStorage());
        productEntity.setProductAlternativeCamera(productRequest.getProductAlternativeCamera());
        productEntity.setProductFrontCamera(productRequest.getProductFrontCamera());
        productEntity.setProductHasCellular(productRequest.isHasCellular());
        productEntity.setProductHasGps(productRequest.isHasGPS());
        productEntity.setProductDisplay(productRequest.getProductDisplay());
        productEntity.setProductName(productRequest.getProductName());

        productEntity.setProductModels(new ArrayList<>());
        return makeProductResponse(this.productRepository.save(productEntity));
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

    @Transactional
    public List<ProductResponse> getAllStoreProducts(String storeId) {
        List<ProductResponse> response = new ArrayList<>();
        Collection<ProductEntity> products = this.productRepository.findByStoreId(storeId);
        for(ProductEntity product : products) {
            ArrayList<ProductModelEntity> productModels = this.productModelRepository.findByProductEntity(product);
            ProductResponse pp = makeProductResponse(product);
            pp.setModels(this.fromEntityToModel(productModels));
            response.add(pp);
        }

        return response;
    }

    public static ProductResponse makeProductResponse(ProductEntity product) {
        ProductResponse pp = new ProductResponse();
        pp.setProductId(product.getProductId());
        pp.setProductName(product.getProductName());
        pp.setProductDescription(product.getProductDescription());
        pp.setIva(product.getProductIva());
        pp.setStore(product.getStoreId());
        pp.setProductBrand(product.getProductBrand());
        pp.setCategoryEntity(product.getProductCategory());
        pp.setProductCPU(product.getProductCpu());
        pp.setProductAdditionalStorage(product.getProductAdditionalStorage());
        pp.setProductOS(product.getProductOs());
        pp.setProductMainCamera(product.getProductMainCamera());
        pp.setProductFrontCamera(product.getProductFrontCamera());
        pp.setProductAlternativeCamera(product.getProductAlternativeCamera());
        pp.setProductDisplay(product.getProductDisplay());
        pp.setProductSize(product.getProductSize());
        pp.setHasCellular(product.isProductHasGps());
        pp.setHasGPS(product.isProductHasGps());
        pp.setProductPrice(product.getProductPrice());
        pp.setProductQuantity(product.getProductQuantity());
        return pp;
    }

    @Transactional
    public ProductResponse editProduct(String storeId, EditProductRequestModel request) throws ObjectNotFoundException, UnauthorizedAccessException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Optional<ProductEntity> tmp = this.productRepository.findByProductId(request.getProductId());
        if(tmp.isEmpty()) throw new ObjectNotFoundException("Product Not Found");
        ProductEntity product = tmp.get();
        if(product.getStoreId().equals(storeId)) {

            if(request.getProperty().equals("productCategory")) {
                Optional<ProductCategoryEntity> tmp1 = this.productCategoryRepository.findByCategoryId(Long.valueOf(request.getValue()));
                if(tmp1.isEmpty()) throw new ObjectNotFoundException("Category not found");
                product.setProductCategory(tmp1.get());
            } else {
                Method setter = product.getClass().getMethod("setProduct" + capitalize(request.getProperty()), String.class);
                setter.invoke(product, request.getValue());
            }
        } else {
            throw new UnauthorizedAccessException("This product is not yours.");
        }
        return this.makeProductResponse(this.productRepository.save(product));
    }

    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
