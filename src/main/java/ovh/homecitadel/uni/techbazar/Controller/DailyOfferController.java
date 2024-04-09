package ovh.homecitadel.uni.techbazar.Controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.ws.rs.Path;
import org.keycloak.authorization.client.util.Http;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ovh.homecitadel.uni.techbazar.Entity.DailyOfferEntity;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.DateException;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.ObjectNotFoundException;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.UnauthorizedAccessException;
import ovh.homecitadel.uni.techbazar.Helper.Model.DailyOfferRequest;
import ovh.homecitadel.uni.techbazar.Helper.Model.ResponseModel;
import ovh.homecitadel.uni.techbazar.Service.DailyOfferService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/REST/daily")
@SecurityRequirement(name = "Keycloak")
public class DailyOfferController {

    private final DailyOfferService dailyOfferService;


    public DailyOfferController(DailyOfferService dailyOfferService) {
        this.dailyOfferService = dailyOfferService;
    }

    @GetMapping
    @RequestMapping("/today")
    public ResponseEntity<ResponseModel> getToday() {
        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .message("Today's Offer")
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .data(Map.of("response", this.dailyOfferService.getTodayOffer()))
                        .build()
        );
    }

    @GetMapping
    @RequestMapping("/{daily-id}")
    public ResponseEntity<ResponseModel> getDaily(@PathVariable("daily-id") Long dailyId) {
        String message = "";
        String reason = "";
        HttpStatus status = HttpStatus.OK;
        DailyOfferEntity daily = null;

        try {
            daily = this.dailyOfferService.getDaily(dailyId);
        } catch(ObjectNotFoundException e) {
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
                        .data(Map.of("response", daily == null ? "": daily))
                        .build()
        );
    }

    @PostMapping
    @RequestMapping("/new")
    public ResponseEntity<ResponseModel> newDaily(@AuthenticationPrincipal Jwt jwt, @RequestBody @Valid DailyOfferRequest request) {
        String message = "";
        String reason = "";
        HttpStatus status = HttpStatus.CREATED;
        DailyOfferEntity daily = null;

        try {
            daily = this.dailyOfferService.newDailyOffer(jwt.getSubject(), request);
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
                        .reason(reason)
                        .message(message)
                        .status(status)
                        .statusCode(status.value())
                        .data(Map.of("response", daily == null ? "" : daily))
                        .build()

        );
    }

    @PutMapping
    @RequestMapping("/edit")
    public ResponseEntity<ResponseModel> editDaily(@AuthenticationPrincipal Jwt jwt, @RequestBody @Valid DailyOfferRequest request) {
        String message = "";
        String reason = "";
        HttpStatus status = HttpStatus.OK;
        DailyOfferEntity daily = null;

        try {
            daily = this.dailyOfferService.editDaily(jwt.getSubject(), request);
        } catch (ObjectNotFoundException e) {
            reason = e.getMessage();
            status = HttpStatus.NOT_FOUND;
        } catch (UnauthorizedAccessException e) {
            reason = e.getMessage();
            status = HttpStatus.UNAUTHORIZED;
        } catch (DateException e) {
            reason = e.getMessage();
            status = HttpStatus.BAD_REQUEST;
        }

        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .reason(reason)
                        .message(message)
                        .status(status)
                        .statusCode(status.value())
                        .data(Map.of("response", daily == null ? "" : daily))
                        .build()
        );

    }

    @GetMapping
    @RequestMapping("/future")
    public ResponseEntity<ResponseModel> getFuture() {
        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .message("next daily")
                        .reason("")
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .data(Map.of("response", this.dailyOfferService.getFutureDaily()))
                        .build()
        );
    }

    @GetMapping
    @RequestMapping("/store/{store-id}")
    public ResponseEntity<ResponseModel> getByStore(@PathVariable("store-id") String storeId) {
        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("Store Daily")
                        .reason("")
                        .data(Map.of("response", this.dailyOfferService.findAllByStoreId(storeId)))
                        .build()
        );
    }

    @GetMapping
    @RequestMapping("/model/{model-id}")
    public ResponseEntity<ResponseModel> getByModelId(@PathVariable("model-id") Long modelId) {
        String message = "";
        String reason = "";
        HttpStatus status = HttpStatus.OK;
        Collection<DailyOfferEntity> daily = null;

        try {
            daily = this.dailyOfferService.findAllByModel(modelId);
        } catch (ObjectNotFoundException e ){
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
                        .data(Map.of("response", daily == null ? "" : daily))
                        .build()
        );
    }

    @GetMapping
    @RequestMapping("/product/{product-id}")
    public ResponseEntity<ResponseModel> getByProductId(@PathVariable("product-id") Long productId) {
        String message = "";
        String reason = "";
        HttpStatus status = HttpStatus.OK;
        Collection<DailyOfferEntity> daily = null;

        try {
            daily = this.dailyOfferService.findAllByProduct(productId);
        } catch (ObjectNotFoundException e) {
            reason = e.getMessage();
            status = HttpStatus.NOT_FOUND;
        }

        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .reason(reason)
                        .message(message)
                        .status(status)
                        .statusCode(status.value())
                        .data(Map.of("response", daily == null ? "" : daily))
                        .build()
        );
    }
}
