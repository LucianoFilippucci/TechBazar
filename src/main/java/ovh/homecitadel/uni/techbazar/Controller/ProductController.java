package ovh.homecitadel.uni.techbazar.Controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.ws.rs.Path;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ovh.homecitadel.uni.techbazar.Entity.Product.ProductEntity;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.ObjectNotFoundException;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.UnauthorizedAccessException;
import ovh.homecitadel.uni.techbazar.Helper.Model.Request.NewProductModelRequest;
import ovh.homecitadel.uni.techbazar.Helper.Model.Request.NewProductRequest;
import ovh.homecitadel.uni.techbazar.Helper.Model.ResponseModel;
import ovh.homecitadel.uni.techbazar.Service.Product.ProductModelService;
import ovh.homecitadel.uni.techbazar.Service.Product.ProductService;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/REST/product")
@SecurityRequirement(name = "Keycloak")
public class ProductController {

    private final ProductService productService;
    private final ProductModelService productModelService;

    public ProductController(ProductService productService, ProductModelService productModelService) {
        this.productService = productService;
        this.productModelService = productModelService;
    }

    @PostMapping
    @RequestMapping("/new")
    public ResponseEntity<ResponseModel> newProduct(@Valid NewProductRequest productRequest) {
        try {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .timeStamp(LocalDateTime.now())
                            .message("Product Created")
                            .status(HttpStatus.CREATED)
                            .statusCode(HttpStatus.CREATED.value())
                            .data(Map.of("product", this.productService.newProduct(productRequest)))
                            .build()
            );

        } catch (ObjectNotFoundException e) {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .timeStamp(LocalDateTime.now())
                            .message("Error")
                            .status(HttpStatus.NOT_FOUND)
                            .statusCode(HttpStatus.NOT_FOUND.value())
                            .reason(e.getMessage())
                            .build()
            );
        }
    }

    @PostMapping
    @RequestMapping("/new-model")
    public ResponseEntity<ResponseModel> newProductModel(@RequestBody @Valid NewProductModelRequest productModelRequest) {

        try {
            return ResponseEntity.ok(
              ResponseModel.builder()
                      .timeStamp(LocalDateTime.now())
                      .message("Product Model Created")
                      .status(HttpStatus.CREATED)
                      .statusCode(HttpStatus.CREATED.value())
                      .data(Map.of("model", this.productModelService.newProductModel(productModelRequest)))
                      .build()
            );
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.ok(
              ResponseModel.builder()
                      .timeStamp(LocalDateTime.now())
                      .status(HttpStatus.NOT_FOUND)
                      .statusCode(HttpStatus.NOT_FOUND.value())
                      .reason(e.getMessage())
                      .build()
            );
        }
    }

    @DeleteMapping
    @RequestMapping("/delete-model/{model-id}")
    public ResponseEntity<ResponseModel> deleteProductModel(@PathVariable("model-id") Long modelId, @AuthenticationPrincipal Jwt jwt) {

        String reason;
        HttpStatus status;
        try {
            this.productModelService.deleteProductModel(modelId, jwt.getSubject());
            status = HttpStatus.OK;
            reason = "Model Deleted";
        } catch (ObjectNotFoundException | UnauthorizedAccessException e) {
            reason = e.getMessage();
            status = e instanceof ObjectNotFoundException ? HttpStatus.NOT_FOUND : HttpStatus.UNAUTHORIZED;
        }

        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .status(status)
                        .statusCode(status.value())
                        .reason(reason)
                        .message(reason)
                        .build()
        );
    }

    @DeleteMapping
    @RequestMapping("/delete/{product-id}")
    public ResponseEntity<ResponseModel> deleteProduct(@PathVariable("product-id") Long productId, @AuthenticationPrincipal Jwt jwt) {

        String reason;
        HttpStatus status;
        try {
            this.productService.deleteProduct(productId, jwt.getSubject());
            status = HttpStatus.OK;
            reason = "Product Deleted";
        } catch (ObjectNotFoundException | UnauthorizedAccessException e) {
            reason = e.getMessage();
            status = e instanceof ObjectNotFoundException ? HttpStatus.NOT_FOUND : HttpStatus.UNAUTHORIZED;
        }

        return ResponseEntity.ok(
          ResponseModel.builder()
                  .timeStamp(LocalDateTime.now())
                  .status(status)
                  .statusCode(status.value())
                  .reason(reason)
                  .message(reason)
                  .build()
        );
    }


    @GetMapping
    @RequestMapping("/{product-id}")
    public ResponseEntity<ResponseModel> getProductById(@PathVariable("product-id") Long productId) {
        HttpStatus status = HttpStatus.OK;
        String responseString = "";
        ProductEntity product = null;

        try {
            product = this.productService.getById(productId);
        } catch (ObjectNotFoundException e) {
            responseString = e.getMessage();
            status = HttpStatus.NOT_FOUND;
        }

        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .status(status)
                        .message(responseString)
                        .reason(responseString)
                        .statusCode(status.value())
                        .data(Map.of("response", status == HttpStatus.OK ? product : null))
                        .build()
        );

    }

    @GetMapping
    @RequestMapping("/find/product/{keyword}")
    public ResponseEntity<ResponseModel> getProductsByKeyword(@PathVariable("keyword") String keyword) {




        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .message("")
                        .reason("METHOD NOT IMPLEMENTED.")
                        .statusCode(HttpStatus.NOT_IMPLEMENTED.value())
                        .status(HttpStatus.NOT_IMPLEMENTED)
                        .data(null)
                        .build()
        );
    }

}
