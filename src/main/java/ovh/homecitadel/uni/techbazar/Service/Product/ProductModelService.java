package ovh.homecitadel.uni.techbazar.Service.Product;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ovh.homecitadel.uni.techbazar.Entity.Product.ProductEntity;
import ovh.homecitadel.uni.techbazar.Entity.Product.ProductModelEntity;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.ObjectNotFoundException;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.UnauthorizedAccessException;
import ovh.homecitadel.uni.techbazar.Helper.Model.Model;
import ovh.homecitadel.uni.techbazar.Helper.Model.Request.NewProductModelRequest;
import ovh.homecitadel.uni.techbazar.Helper.UnifiedServiceAccess;
import ovh.homecitadel.uni.techbazar.Repository.Product.ProductModelRepository;
import ovh.homecitadel.uni.techbazar.Repository.Product.ProductRepository;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class ProductModelService {

    private final ProductModelRepository productModelRepository;
    private final ProductRepository productRepository;

    public ProductModelService(ProductModelRepository productModelRepository, ProductRepository productRepository) {
        this.productModelRepository = productModelRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public ArrayList<ProductModelEntity> newProductModel(NewProductModelRequest models) throws ObjectNotFoundException {
        ArrayList<ProductModelEntity> productModels = new ArrayList<>();
        Optional<ProductEntity> tmp = this.productRepository.findByProductId(models.getProductId());
        if(tmp.isEmpty()) throw new ObjectNotFoundException("Product Not found");
        for(Model model : models.getModels()) {
            ProductModelEntity productModel = new ProductModelEntity();
            productModel.setProductColor(model.getProductColor());
            productModel.setProductCPU(model.getProductCPU());
            productModel.setProductDisplay(model.getProductDisplay());
            productModel.setProductOs(model.getProductOS());
            productModel.setProductAdditionalStorage(model.getProductAdditionalStorage());
            productModel.setProductMainStorage(model.getProductMainStorage());
            productModel.setProductMainCamera(model.getProductMainCamera());
            productModel.setProductFrontCamera(model.getProductFrontCamera());
            productModel.setProductAlternativeCamera(model.getProductAlternativeCamera());
            productModel.setProductHasCellular(model.isHasCellular());
            productModel.setProductHasGps(model.isHasGPS());
            productModel.setProductPrice(model.getProductPrice());
            productModel.setProductQuantity(model.getProductQuantity());
            productModel.setProductSize(model.getProductSize());
            productModel.setProductRam(model.getProductRam());



            productModel.setProductEntity(tmp.get());
            productModels.add(this.productModelRepository.save(productModel));
        }

        return  productModels;
    }

    @Transactional
    public void deleteProductModel(Long modelId, String storeId) throws UnauthorizedAccessException, ObjectNotFoundException {
        Optional<ProductModelEntity> tmp = this.productModelRepository.findById(modelId);

        if(tmp.isEmpty()) throw new ObjectNotFoundException("Model Not found.");
        ProductModelEntity model = tmp.get();

        ProductEntity product = model.getProductEntity();
        if(product.getStoreId().equals(storeId))
            this.productModelRepository.delete(model);
        else
            throw new UnauthorizedAccessException("This model is not yours.");
    }
}
