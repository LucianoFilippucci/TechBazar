package ovh.homecitadel.uni.techbazar.Service.Product;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ovh.homecitadel.uni.techbazar.Entity.Product.ProductCategoryEntity;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.ObjectNotFoundException;
import ovh.homecitadel.uni.techbazar.Repository.Product.ProductCategoryRepository;

import java.util.Optional;

@Service
public class ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;

    public ProductCategoryService(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }


    @Transactional(readOnly = true)
    public ProductCategoryEntity getCategory(Long categoryId) throws ObjectNotFoundException {
        Optional<ProductCategoryEntity> tmp = productCategoryRepository.findByCategoryId(categoryId);

        if(tmp.isPresent())
            return tmp.get();
        throw new ObjectNotFoundException("Category Not Found");
    }
}
