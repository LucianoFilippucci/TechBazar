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
    public ArrayList<Model> newProductModel(NewProductModelRequest models) throws ObjectNotFoundException {
        ArrayList<Model> productModels = new ArrayList<>();
        Optional<ProductEntity> tmp = this.productRepository.findByProductId(models.getProductId());
        if(tmp.isEmpty()) throw new ObjectNotFoundException("Product Not found");
        for(Model model : models.getModels()) {
            ProductModelEntity productConfig = getProductConfig(model, tmp.get());
            productModels.add(fromEntityToModel(this.productModelRepository.save(productConfig)));
            tmp.get().setProductQuantity(tmp.get().getProductQuantity() + model.getConfigQty());
        }

        return  productModels;
    }

    public static Model fromEntityToModel(ProductModelEntity productModel) {
        Model model = new Model();
        model.setModelId(productModel.getProductModelId());
        model.setConfigPrice(productModel.getConfigPrice());
        model.setConfiguration(productModel.getConfiguration());
        model.setConfigQty(productModel.getConfigQty());
        model.setConfigColor(productModel.getConfigColor());
        model.setConfigSoldQty(productModel.getConfigSoldQty());
        return model;
    }

    private ProductModelEntity getProductConfig(Model model, ProductEntity tmp) {
        ProductModelEntity productConfig = new ProductModelEntity();
        productConfig.setProductEntity(tmp);
        productConfig.setConfigColor(model.getConfigColor());
        productConfig.setConfigPrice(model.getConfigPrice());
        productConfig.setConfigSoldQty(model.getConfigSoldQty());
        productConfig.setConfiguration(model.getConfiguration());
        productConfig.setConfigQty(model.getConfigQty());
        productConfig.setAuctions(new ArrayList<>());
        productConfig.setDailyOffer(new ArrayList<>());
        return productConfig;
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

    @Transactional
    public Model getModel(Long modelId) throws ObjectNotFoundException {
        if(this.productModelRepository.findById(modelId).isPresent())
            return fromEntityToModel(this.productModelRepository.findById(modelId).get());
        else throw new ObjectNotFoundException("Model Not found.");
    }
}
