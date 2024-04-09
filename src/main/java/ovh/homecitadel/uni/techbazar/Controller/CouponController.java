package ovh.homecitadel.uni.techbazar.Controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ovh.homecitadel.uni.techbazar.Entity.CouponEntity;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.CouponException;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.ObjectNotFoundException;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.UnauthorizedAccessException;
import ovh.homecitadel.uni.techbazar.Helper.Model.CouponRequest;
import ovh.homecitadel.uni.techbazar.Helper.Model.ResponseModel;
import ovh.homecitadel.uni.techbazar.Service.CouponService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/REST/coupon")
@SecurityRequirement(name = "Keycloak")
public class CouponController {

    private final CouponService couponService;


    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @GetMapping
    @RequestMapping("/store/all")
    public ResponseEntity<ResponseModel> getAllStoreCoupons(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .message("Coupon List")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .data(Map.of("coupons", this.couponService.getAllStoreCoupons(jwt.getSubject())))
                        .build()
        );
    }

    @PostMapping
    @RequestMapping("/new")
    public ResponseEntity<ResponseModel> newCoupon(@AuthenticationPrincipal Jwt jwt, @RequestBody @Valid CouponRequest request) {
        String message = "";
        String reason = "";
        HttpStatus status = HttpStatus.CREATED;
        CouponEntity coupon = null;
        try {
            coupon = this.couponService.newCoupon(jwt.getSubject(), request);
        } catch (ObjectNotFoundException e) {
            reason = e.getMessage();
            status = HttpStatus.NOT_FOUND;
        }

        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .message(message)
                        .reason(reason)
                        .statusCode(status.value())
                        .status(status)
                        .data(Map.of("coupon", coupon == null ? "" : coupon))
                        .build()
        );
    }

    @DeleteMapping
    @RequestMapping("/delete/{coupon-id}")
    public ResponseEntity<ResponseModel> deleteCoupon(@AuthenticationPrincipal Jwt jwt, @PathVariable("coupon-id") String coupon) {
        String message = "";
        String reason = "";
        HttpStatus status = HttpStatus.OK;
        boolean deleted = false;
        try {
            deleted = this.couponService.deleteCoupon(jwt.getSubject(), coupon);
        } catch (ObjectNotFoundException e) {
            reason = e.getMessage();
            status = HttpStatus.NOT_FOUND;
        } catch (UnauthorizedAccessException e) {
            reason = e.getMessage();
            status = HttpStatus.UNAUTHORIZED;
        }

        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .message(message)
                        .reason(reason)
                        .statusCode(status.value())
                        .status(status)
                        .data(Map.of("deleted", deleted))
                        .build()
        );
    }

    @PutMapping
    @RequestMapping("/edit")
    public ResponseEntity<ResponseModel> editCoupon(@AuthenticationPrincipal Jwt jwt, @RequestBody @Valid CouponRequest request) {
        String message = "";
        String reason = "";
        HttpStatus status = HttpStatus.OK;
        CouponEntity coupon = null;
        try {
            coupon = this.couponService.editCoupon(jwt.getSubject(), request);
        } catch (ObjectNotFoundException e) {
            reason = e.getMessage();
            status = HttpStatus.NOT_FOUND;
        } catch (UnauthorizedAccessException e) {
            reason = e.getMessage();
            status = HttpStatus.UNAUTHORIZED;
        }

        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .message(message)
                        .reason(reason)
                        .statusCode(status.value())
                        .status(status)
                        .data(Map.of("coupon", coupon == null ? "" : coupon))
                        .build()
        );
    }

    @PostMapping
    @RequestMapping("/use/{coupon-id}")
    public ResponseEntity<ResponseModel> useCoupon(@AuthenticationPrincipal Jwt jwt,@PathVariable("coupon-id") String coupon) {
        String message = "";
        String reason = "";
        HttpStatus status = HttpStatus.OK;
        boolean used = false;
        try {
            used = this.couponService.useCoupon(jwt.getClaim("cartId"), coupon);
        } catch (ObjectNotFoundException e) {
            reason = e.getMessage();
            status = HttpStatus.NOT_FOUND;
        } catch (CouponException e) {
            reason = e.getMessage();
            status = HttpStatus.BAD_REQUEST;
        }

        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .message(message)
                        .reason(reason)
                        .statusCode(status.value())
                        .status(status)
                        .data(Map.of("used", used))
                        .build()
        );
    }

    @PutMapping
    @RequestMapping("/remove/{coupon-id}")
    public ResponseEntity<ResponseModel> removeCoupon(@AuthenticationPrincipal Jwt jwt, @PathVariable("coupon-id") String coupon) {
        String message = "";
        String reason = "";
        HttpStatus status = HttpStatus.OK;
        boolean removed = false;
        try {
            removed = this.couponService.removeCoupon(jwt.getClaim("cartId"), coupon);
        } catch (ObjectNotFoundException e) {
            reason = e.getMessage();
            status = HttpStatus.NOT_FOUND;
        }

        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .message(message)
                        .reason(reason)
                        .statusCode(status.value())
                        .status(status)
                        .data(Map.of("removed", removed))
                        .build()
        );
    }

}
