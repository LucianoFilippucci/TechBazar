package ovh.homecitadel.uni.techbazar.Controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.ws.rs.Path;
import org.keycloak.authorization.client.util.Http;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ovh.homecitadel.uni.techbazar.Entity.Product.ProductEntity;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.ObjectNotFoundException;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.UnauthorizedAccessException;
import ovh.homecitadel.uni.techbazar.Helper.Model.EditProductRequestModel;
import ovh.homecitadel.uni.techbazar.Helper.Model.Model;
import ovh.homecitadel.uni.techbazar.Helper.Model.ProductResponse;
import ovh.homecitadel.uni.techbazar.Helper.Model.Request.NewProductModelRequest;
import ovh.homecitadel.uni.techbazar.Helper.Model.Request.NewProductRequest;
import ovh.homecitadel.uni.techbazar.Helper.Model.ResponseModel;
import ovh.homecitadel.uni.techbazar.Service.Product.ProductCategoryService;
import ovh.homecitadel.uni.techbazar.Service.Product.ProductModelService;
import ovh.homecitadel.uni.techbazar.Service.Product.ProductService;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

@RestController
@RequestMapping("/REST/product")
@SecurityRequirement(name = "Keycloak")
public class ProductController {

    private final ProductService productService;
    private final ProductModelService productModelService;

    private final ProductCategoryService productCategoryService;

    public ProductController(ProductService productService, ProductModelService productModelService, ProductCategoryService productCategoryService) {
        this.productService = productService;
        this.productModelService = productModelService;
        this.productCategoryService = productCategoryService;
    }

    @PostMapping
    @RequestMapping("/new")
    public ResponseEntity<ResponseModel> newProduct(@AuthenticationPrincipal Jwt jwt, @RequestBody @Valid NewProductRequest productRequest) {
        HttpStatus status = HttpStatus.CREATED;
        String message = "";
        ProductResponse product = null;


        try {
            product = productService.newProduct(productRequest, jwt.getSubject());
            message = "Product Created";
        } catch (ObjectNotFoundException e) {
            message = e.getMessage();
            status = HttpStatus.NOT_FOUND;
        }

        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .message(message)
                        .status(status)
                        .statusCode(status.value())
                        .data(Map.of("product", product != null ? product : ""))
                        .build()
        );
    }

    @PostMapping
    @RequestMapping("/new-model")
    public ResponseEntity<ResponseModel> newProductModel(@RequestBody @Valid NewProductModelRequest productModelRequest) {
        HttpStatus status = HttpStatus.CREATED;
        String message = "";
        ArrayList<Model> models = new ArrayList<>();


        try {
            models = this.productModelService.newProductModel(productModelRequest);
            message = "Product Model created";
        } catch (ObjectNotFoundException e) {
            message = e.getMessage();
            status = HttpStatus.NOT_FOUND;
        }

        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .message(message)
                        .status(status)
                        .statusCode(status.value())
                        .data(Map.of("model", models))
                        .build()
        );
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
        ProductResponse product = null;

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
    @RequestMapping("/search/{keyword}")
    public ResponseEntity<ResponseModel> getProductsByKeyword(@PathVariable("keyword") String keyword, @PageableDefault(size = 10)Pageable pageable) {
        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .message("")
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .data(Map.of("response", this.productService.searchBox(keyword, pageable)))
                        .build()
        );
    }

    @GetMapping
    @RequestMapping("/store/{store-id}")
    public ResponseEntity<ResponseModel> getAllStoreProducts(@AuthenticationPrincipal Jwt jwt, @PathVariable("store-id") String storeId) {

        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .message("Store Products")
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .reason("")
                        .data(Map.of("products", this.productService.getAllStoreProducts(storeId)))
                        .build()
        );
    }

    @GetMapping
    @RequestMapping("/category")
    public ResponseEntity<ResponseModel> getAllCategories() {
        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .message("OK")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .data(Map.of("categories", this.productCategoryService.getAll()))
                        .build()
        );
    }


    @PostMapping
    @RequestMapping("/edit")
    public ResponseEntity<ResponseModel> editProduct(@AuthenticationPrincipal Jwt jwt, @RequestBody @Valid EditProductRequestModel request) {
        HttpStatus status = HttpStatus.OK;
        String message = "";
        ProductResponse product = null;
        try {
            product = this.productService.editProduct(jwt.getSubject(), request);
            message = "Product Modified.";
        }catch (ObjectNotFoundException e) {
            message = e.getMessage();
            status = HttpStatus.NOT_FOUND;
        } catch (UnauthorizedAccessException e) {
            message = e.getMessage();
            status = HttpStatus.UNAUTHORIZED;
        } catch (NoSuchMethodException | IllegalAccessException e) {
            message = e.getMessage();
            status = HttpStatus.BAD_REQUEST;
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            message = e.getMessage();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            e.printStackTrace();

        }

        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .message(message)
                        .reason(message)
                        .statusCode(status.value())
                        .status(status)
                        .data(Map.of("product", product != null ? product : ""))
                        .build()
        );
    }


}
