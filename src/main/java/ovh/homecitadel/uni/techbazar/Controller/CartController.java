package ovh.homecitadel.uni.techbazar.Controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.ws.rs.Path;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.ObjectNotFoundException;
import ovh.homecitadel.uni.techbazar.Helper.Model.Cart.CartResponse;
import ovh.homecitadel.uni.techbazar.Helper.Model.Cart.ProductInCart;
import ovh.homecitadel.uni.techbazar.Helper.Model.ResponseModel;
import ovh.homecitadel.uni.techbazar.Service.MongoDB.CartService;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@SecurityRequirement(name = "Keycloak")
@RequestMapping("/REST/user/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }


    @GetMapping
    public ResponseEntity<ResponseModel> getUserCart(@AuthenticationPrincipal Jwt jwt) {
        String message = "";
        String reason = "";
        HttpStatus status = HttpStatus.OK;
        CartResponse response = null;


        try {
            response = this.cartService.getCart(jwt.getClaim("cartId").toString());
            message = "Cart";
        } catch (ObjectNotFoundException e) {
            reason = e.getMessage();
            status = HttpStatus.NOT_FOUND;
        }

        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .message(message)
                        .reason(reason)
                        .status(status)
                        .statusCode(status.value())
                        .data(Map.of("cart", status == HttpStatus.OK ? response : ""))
                        .build()
        );
    }

    @PostMapping
    @RequestMapping("/add/{product-id}/{model-id}/{qty}")
    public ResponseEntity<ResponseModel> addElement(@AuthenticationPrincipal Jwt jwt, @PathVariable("product-id") Long productId, @PathVariable("qty") int qty, @PathVariable("model-id") Long modelId) {
        String message = "";
        String reason = "";
        HttpStatus status = HttpStatus.OK;
        boolean isAddedd = false;

        try {
            isAddedd = this.cartService.addElement(jwt.getClaim("cartId"), productId, modelId, qty);
        } catch (ObjectNotFoundException e) {
            reason = e.getMessage();
            status = HttpStatus.NOT_FOUND;
        }

        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .status(status)
                        .statusCode(status.value())
                        .reason(reason)
                        .message(message)
                        .data(Map.of("added", isAddedd))
                        .build()
        );
    }


    @PutMapping
    @RequestMapping("/update/{product-id}/{model-id}/{qty}")
    public ResponseEntity<ResponseModel> removeElement(@AuthenticationPrincipal Jwt jwt, @PathVariable("product-id") Long productId, @PathVariable("model-id") Long modelId, @PathVariable("qty") int qty) {
        String message = "";
        String reason = "";
        HttpStatus status = HttpStatus.OK;
        boolean updated = false;

        try {
            updated = this.cartService.updateElement(jwt.getClaim("cartId"), productId, modelId, qty);
            message = "Updated.";
        } catch (ObjectNotFoundException e) {
            reason = "Product Not Found.";
            status = HttpStatus.NOT_FOUND;
        }

        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .status(status)
                        .statusCode(status.value())
                        .message(message)
                        .reason(reason)
                        .data(Map.of("updated", updated))
                        .build()
        );
    }

    @PostMapping
    @RequestMapping("/clear")
    public ResponseEntity<ResponseModel> clearCart(@AuthenticationPrincipal Jwt jwt) {
        String message = "";
        String reason = "";
        HttpStatus status = HttpStatus.OK;
        boolean updated = false;

        try {
            updated = this.cartService.clearCart(jwt.getClaim("cartId"));
            message = "Updated.";
        } catch (ObjectNotFoundException e) {
            reason = "Product Not Found.";
            status = HttpStatus.NOT_FOUND;
        }

        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .status(status)
                        .statusCode(status.value())
                        .message(message)
                        .reason(reason)
                        .data(Map.of("updated", updated))
                        .build()
        );
    }


}
