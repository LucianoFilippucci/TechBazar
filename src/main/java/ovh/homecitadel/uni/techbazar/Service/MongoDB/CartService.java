package ovh.homecitadel.uni.techbazar.Service.MongoDB;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ovh.homecitadel.uni.techbazar.Entity.CouponEntity;
import ovh.homecitadel.uni.techbazar.Entity.Product.ProductCategoryEntity;
import ovh.homecitadel.uni.techbazar.Entity.Product.ProductEntity;
import ovh.homecitadel.uni.techbazar.Entity.Product.ProductModelEntity;
import ovh.homecitadel.uni.techbazar.Entity.User.MongoDB.CartEntity;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.ObjectNotFoundException;
import ovh.homecitadel.uni.techbazar.Helper.Model.Cart.CartResponse;
import ovh.homecitadel.uni.techbazar.Helper.Model.Cart.ProductInCart;
import ovh.homecitadel.uni.techbazar.Repository.CouponRepository;
import ovh.homecitadel.uni.techbazar.Repository.Product.ProductCategoryRepository;
import ovh.homecitadel.uni.techbazar.Repository.Product.ProductModelRepository;
import ovh.homecitadel.uni.techbazar.Repository.Product.ProductRepository;
import ovh.homecitadel.uni.techbazar.Repository.User.MongoDB.CartRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final ProductModelRepository productModelRepository;
    private final CouponRepository couponRepository;
    private final ProductCategoryRepository productCategoryRepository;

    public CartService(CartRepository cartRepository, ProductRepository productRepository, ProductModelRepository productModelRepository, CouponRepository couponRepository, ProductCategoryRepository productCategoryRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.productModelRepository = productModelRepository;
        this.couponRepository = couponRepository;
        this.productCategoryRepository = productCategoryRepository;
    }

    @Transactional
    public CartResponse getCart(String cartId) throws ObjectNotFoundException {
        Optional<CartEntity> tmp = this.cartRepository.findByCartId(cartId);
        if(tmp.isEmpty()) throw new ObjectNotFoundException("Cart Not found.");
        CartEntity cart = tmp.get();

        List<String> tmpCoupons = cart.getCoupons();
        List<CouponEntity> coupons = new ArrayList<>();

        if(tmpCoupons == null)
            tmpCoupons = new ArrayList<>();
        else {
            if(tmpCoupons.size() > 0) {
                for(String cp : tmpCoupons) {
                    Optional<CouponEntity> tmp2 = this.couponRepository.findById(cp);
                    tmp2.ifPresent(coupons::add);
                }
            }
        }


        BigDecimal cartTotal = BigDecimal.ZERO;
        BigDecimal cartTax = BigDecimal.ZERO;
        BigDecimal discountedPrice = BigDecimal.ZERO;
        int cartElems = 0;

        for (ProductInCart pic : cart.getProducts()) {
            BigDecimal price = pic.getProductPrice().multiply(BigDecimal.valueOf(pic.getQty()));
            float iva = (float)pic.getIva() / 100;
            BigDecimal tax = price.multiply(BigDecimal.valueOf(iva));
            cartTotal = cartTotal.add(price);
            cartTax = cartTax.add(tax);
            cartElems++;

            for(CouponEntity coupon : coupons) {
                Optional<ProductCategoryEntity> tmp3 = this.productCategoryRepository.findByCategoryId(coupon.getCategory().getCategoryId());
                if(tmp3.isPresent()) {
                    ProductCategoryEntity category = tmp3.get();
                    Optional<ProductEntity> tmp4 = this.productRepository.findByProductId(pic.getProductId());
                    if(tmp4.isPresent()) {
                        if(tmp4.get().getProductCategory().equals(category) && coupon.getStoreId().equals(tmp4.get().getStoreId())) {

                            float dc = (float) coupon.getDiscount() / 100;
                            BigDecimal discount = pic.getProductPrice().multiply(BigDecimal.valueOf(dc));
                            discountedPrice = discountedPrice.add(discount.multiply(BigDecimal.valueOf(pic.getQty())));
                        }
                    }
                }
            }

        }

        BigDecimal totalAfterCoupons = cartTotal.subtract(discountedPrice);
        return new CartResponse(cartTotal, cartTax, cart.getProducts(), cartElems, totalAfterCoupons, tmpCoupons);
    }

    @Transactional
    public boolean addElement(String cartId, Long productId, Long modelId, int qty) throws ObjectNotFoundException {

        Optional<ProductEntity> tmp = this.productRepository.findByProductId(productId);
        if(tmp.isEmpty()) throw new ObjectNotFoundException("Product Not Found.");
        ProductEntity product = tmp.get();

        Optional<CartEntity> tmp2 = this.cartRepository.findByCartId(cartId);
        if(tmp2.isEmpty()) throw new ObjectNotFoundException("Cart Not Found");
        CartEntity cart = tmp2.get();

        Optional<ProductModelEntity> tmp3 = this.productModelRepository.findById(modelId);
        if(tmp3.isEmpty()) throw new ObjectNotFoundException("Product Model Not Found");
        ProductModelEntity model = tmp3.get();

        ProductInCart pic = new ProductInCart(product.getProductId(), model.getProductModelId(), qty, model.getProductPrice(), product.getProductIva());

        cart.getProducts().add(pic);
        cart.setUpdatedAt(LocalDateTime.now());
        this.cartRepository.save(cart);
        return true;
    }

    @Transactional
    public boolean updateElement(String cartId, Long productId, Long modelId, int qty) throws ObjectNotFoundException{
        Optional<CartEntity> tmp = this.cartRepository.findByCartId(cartId);
        if(tmp.isEmpty()) throw new ObjectNotFoundException("Cart not found.");
        CartEntity cart = tmp.get();

        for(ProductInCart pic : cart.getProducts()) {
            if(pic.getProductId().equals(productId) && pic.getProductModelId().equals(modelId)) {
                pic.setQty(qty);
                this.cartRepository.save(cart);
                return true;
            }
        }
        return false;
    }

    @Transactional
    public boolean clearCart(String cartId) throws ObjectNotFoundException {
        Optional<CartEntity> tmp = this.cartRepository.findByCartId(cartId);
        if(tmp.isEmpty()) throw new ObjectNotFoundException("Cart not found.");
        CartEntity cart = tmp.get();

        cart.getProducts().clear();
        LocalDateTime now = LocalDateTime.now();
        cart.setUpdatedAt(now);
        return this.cartRepository.save(cart).getUpdatedAt().equals(now);
    }
}
