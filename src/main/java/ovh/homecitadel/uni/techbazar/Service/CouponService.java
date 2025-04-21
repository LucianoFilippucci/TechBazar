package ovh.homecitadel.uni.techbazar.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ovh.homecitadel.uni.techbazar.Entity.CouponEntity;
import ovh.homecitadel.uni.techbazar.Entity.Product.ProductCategoryEntity;
import ovh.homecitadel.uni.techbazar.Entity.User.MongoDB.CartEntity;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.CouponException;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.ObjectNotFoundException;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.UnauthorizedAccessException;
import ovh.homecitadel.uni.techbazar.Helper.Model.CouponRequest;
import ovh.homecitadel.uni.techbazar.Repository.CouponRepository;
import ovh.homecitadel.uni.techbazar.Repository.Product.ProductCategoryRepository;
import ovh.homecitadel.uni.techbazar.Repository.User.MongoDB.CartRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final ProductCategoryRepository productCategoryRepository;

    private final CartRepository cartRepository;


    public CouponService(CouponRepository couponRepository, ProductCategoryRepository productCategoryRepository, CartRepository cartRepository) {
        this.couponRepository = couponRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.cartRepository = cartRepository;
    }

    @Transactional
    public Collection<CouponEntity> getAllStoreCoupons(String storeId) {
        return this.couponRepository.findByStoreId(storeId);
    }

    @Transactional
    public CouponEntity newCoupon(String storeId, CouponRequest request) throws ObjectNotFoundException {
        Optional<ProductCategoryEntity> tmp = this.productCategoryRepository.findByCategoryId(request.getCategoryId());
        if(tmp.isEmpty()) throw new ObjectNotFoundException("Category not Found");
        ProductCategoryEntity category = tmp.get();

        CouponEntity coupon = new CouponEntity();
        coupon.setCouponId(request.getCouponCode());
        coupon.setDiscount(request.getDiscount());
        coupon.setExpiration(request.getExpiration());
        coupon.setMaxUse(request.getMaxUse());
        coupon.setStoreId(storeId);
        coupon.setCategory(category);

        return this.couponRepository.save(coupon);
    }

    @Transactional
    public boolean deleteCoupon(String storeId, String couponCode) throws UnauthorizedAccessException, ObjectNotFoundException {
        Optional<CouponEntity> tmp = this.couponRepository.findById(couponCode);
        if(tmp.isEmpty()) throw new ObjectNotFoundException("Coupon Not Found");
        CouponEntity coupon = tmp.get();

        if(!coupon.getStoreId().equals(storeId)) throw new UnauthorizedAccessException("This coupon is not yours.");

        this.couponRepository.delete(coupon);
        return true;
    }

    @Transactional
    public CouponEntity editCoupon(String storeId, CouponRequest request) throws UnauthorizedAccessException, ObjectNotFoundException {
        Optional<CouponEntity> tmp = this.couponRepository.findById(request.getCouponCode());
        if(tmp.isEmpty()) throw new ObjectNotFoundException("Coupon not Found");
        CouponEntity coupon = tmp.get();

        if(!coupon.getStoreId().equals(storeId)) throw new UnauthorizedAccessException("This coupon is not yours");

        if(!request.getCategoryId().equals(coupon.getCategory().getCategoryId())) {
            Optional<ProductCategoryEntity> tmp2 = this.productCategoryRepository.findByCategoryId(request.getCategoryId());
            if(tmp2.isEmpty()) throw new ObjectNotFoundException("Category Not Found");
            coupon.setCategory(tmp2.get());
        }

        coupon.setDiscount(request.getDiscount());
        coupon.setExpiration(request.getExpiration());
        coupon.setMaxUse(request.getMaxUse());

        return this.couponRepository.save(coupon);

    }

    @Transactional
    public boolean useCoupon(String cartId, String couponCode) throws ObjectNotFoundException, CouponException {
        Optional<CartEntity> tmp = this.cartRepository.findByCartId(cartId);
        if(tmp.isEmpty()) throw new ObjectNotFoundException("Cart not Found");
        CartEntity cart = tmp.get();

        Optional<CouponEntity> tmp2 = this.couponRepository.findById(couponCode);
        if(tmp2.isEmpty()) throw new ObjectNotFoundException("Coupon Not Found");
        CouponEntity coupon = tmp2.get();

        if(coupon.getExpiration().isBefore(LocalDateTime.now())) throw new CouponException("Coupon Expired");
        if(coupon.getMaxUse() > 0 && coupon.getTimesUsed() >= coupon.getMaxUse()) throw new CouponException("Coupon Reached Max Use");

        if(cart.getCoupons() == null)
            cart.setCoupons(new ArrayList<>());

        if(cart.getCoupons().contains(couponCode)) throw new CouponException("Coupon Already Used.");

        coupon.setTimesUsed(coupon.getTimesUsed() + 1);
        cart.getCoupons().add(couponCode);

        this.couponRepository.save(coupon);
        this.cartRepository.save(cart);

        return true;
    }

    @Transactional
    public boolean removeCoupon(String cartId, String couponCode) throws ObjectNotFoundException {
        Optional<CouponEntity> tmp = this.couponRepository.findById(couponCode);
        if(tmp.isEmpty()) throw new ObjectNotFoundException("Coupon Not Found");
        CouponEntity coupon = tmp.get();

        Optional<CartEntity> tmp2 = this.cartRepository.findByCartId(cartId);
        if(tmp2.isEmpty()) throw new ObjectNotFoundException("Cart Not Found");
        CartEntity cart = tmp2.get();

        int index = cart.getCoupons().indexOf(couponCode);
        cart.getCoupons().remove(index);

        coupon.setTimesUsed(coupon.getTimesUsed() - 1);
        this.cartRepository.save(cart);
        this.couponRepository.save(coupon);

        return true;
    }

    @Transactional
    public boolean validateCoupon(String coupon) throws CouponException {
        Optional<CouponEntity> tmp = this.couponRepository.findById(coupon);
        if(tmp.isEmpty())
            throw new CouponException("Coupon Not Found");
        if(tmp.get().getTimesUsed() == tmp.get().getMaxUse())
            throw new CouponException("Coupon Reached Max Use");
        if(tmp.get().getExpiration().isBefore(LocalDateTime.now()))
            throw new CouponException("Coupon Expired");

        return true;
    }
}
