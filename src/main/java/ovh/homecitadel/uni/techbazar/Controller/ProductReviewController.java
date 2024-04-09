package ovh.homecitadel.uni.techbazar.Controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ovh.homecitadel.uni.techbazar.Entity.Product.ProductReviewEntity;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.ObjectNotFoundException;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.UnauthorizedAccessException;
import ovh.homecitadel.uni.techbazar.Helper.Model.NewReviewRequest;
import ovh.homecitadel.uni.techbazar.Helper.Model.ResponseModel;
import ovh.homecitadel.uni.techbazar.Service.Product.ProductReviewService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/REST/product/review")
@SecurityRequirement(name = "Keycloak")
public class ProductReviewController {
    private final ProductReviewService productReviewService;

    public ProductReviewController(ProductReviewService productReviewService) {
        this.productReviewService = productReviewService;
    }

    @GetMapping
    @RequestMapping("/all/{product-id}")
    public ResponseEntity<ResponseModel> getProductReviews(@PathVariable("product-id") Long productId) {
        String message = "";
        String reason = "";
        HttpStatus status = HttpStatus.OK;
        Collection<ProductReviewEntity> response = new ArrayList<>();

        try {
            response = this.productReviewService.getProductReviews(productId);
            message = "Product Reviews";
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
                        .data(Map.of("reviews", status == HttpStatus.OK ? response : ""))
                        .build()
        );
    }

    @GetMapping
    @RequestMapping("/{review-id}")
    public ResponseEntity<ResponseModel> getProductReview(@PathVariable("review-id") Long reviewId) {
        String message = "";
        String reason = "";
        HttpStatus status = HttpStatus.OK;
        ProductReviewEntity response = null;

        try {
            response = this.productReviewService.getProductReview(reviewId);
            message = "Product Review";
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
                        .data(Map.of("reviews", status == HttpStatus.OK ? response : ""))
                        .build()
        );
    }

    @PostMapping
    @RequestMapping("/new/{product-id}")
    public ResponseEntity<ResponseModel> newReview(@AuthenticationPrincipal Jwt jwt, @PathVariable("product-id") Long productId, @RequestBody @Valid NewReviewRequest reviewRequest) {
        String message = "";
        String reason = "";
        HttpStatus status = HttpStatus.CREATED;
        ProductReviewEntity response = null;

        try {
            response = this.productReviewService.newReview(jwt.getSubject(), productId, reviewRequest);
            message = "Review Submitted.";
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
                        .data(Map.of("reviews", status == HttpStatus.CREATED ? response : ""))
                        .build()
        );
    }

    @DeleteMapping
    @RequestMapping("/delete/{review-id}")
    public ResponseEntity<ResponseModel> deleteReview(@AuthenticationPrincipal Jwt jwt, @PathVariable("review-id") Long reviewId) {
        String message = "";
        String reason = "";
        HttpStatus status = HttpStatus.OK;
        boolean response = false;

        try {
            response = this.productReviewService.deleteReview(jwt.getSubject(), reviewId);
            message = "Review Deleted.";
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
                        .data(Map.of("reviews", status == HttpStatus.OK ? response : ""))
                        .build()
        );
    }

    @PostMapping
    @RequestMapping("/{review-id}/like")
    public ResponseEntity<ResponseModel> likeReview(@AuthenticationPrincipal Jwt jwt, @PathVariable("review-id") Long reviewId) {


        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .message("")
                        .reason("Not Implemented")
                        .status(HttpStatus.NOT_IMPLEMENTED)
                        .statusCode(HttpStatus.NOT_IMPLEMENTED.value())
                        .data(Map.of("data", false))
                        .build()
        );
    }
}
