package ovh.homecitadel.uni.techbazar.Controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.ObjectNotFoundException;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.ProductQuantityUnavailableException;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.UnauthorizedAccessException;
import ovh.homecitadel.uni.techbazar.Helper.Model.OrderListModel;
import ovh.homecitadel.uni.techbazar.Helper.Model.ResponseModel;
import ovh.homecitadel.uni.techbazar.Helper.Model.UpdateOrderModel;
import ovh.homecitadel.uni.techbazar.Service.OrderService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@SecurityRequirement(name = "Keycloak")
@RequestMapping("/REST/user/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping
    @RequestMapping("/place/{address-id}")
    public ResponseEntity<ResponseModel> placeOrder(@AuthenticationPrincipal Jwt jwt, @PathVariable("address-id") Long addressId) {
        HttpStatus status = HttpStatus.CREATED;
        String message = "";
        String reason = "";
        String orderId = "";
        try {
            orderId = this.orderService.placeOrder(jwt.getClaim("cartId"), jwt.getSubject(), addressId);
        } catch (ObjectNotFoundException e) {
            reason = e.getMessage();
            status = HttpStatus.NOT_FOUND;
        } catch (ProductQuantityUnavailableException e) {
            reason = e.getMessage();
            status = HttpStatus.BAD_REQUEST;
        }

        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .message(message)
                        .reason(reason)
                        .status(status)
                        .statusCode(status.value())
                        .data(Map.of("order-id", orderId))
                        .build()
        );
    }

    @GetMapping
    @RequestMapping("/")
    public ResponseEntity<ResponseModel> getAllOrders(@AuthenticationPrincipal Jwt jwt) {
        HttpStatus status = HttpStatus.OK;
        String message = "";
        String reason = "";
        List<OrderListModel> orders = new ArrayList<>();
        try {
            orders = this.orderService.getOrders(jwt.getSubject());
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
                        .data(Map.of("orders", orders))
                        .build()
        );
    }

    @GetMapping
    @RequestMapping("/{order-id}")
    public ResponseEntity<ResponseModel> getOrder(@AuthenticationPrincipal Jwt jwt, @PathVariable("order-id") String orderId) {
        HttpStatus status = HttpStatus.OK;
        String message = "";
        String reason = "";
        OrderListModel order = null;
        try {
            order = this.orderService.getOrder(jwt.getSubject(), orderId);
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
                        .status(status)
                        .statusCode(status.value())
                        .data(Map.of("order", order == null ? "" : order))
                        .build()
        );
    }

    @GetMapping
    @RequestMapping("/store/")
    public ResponseEntity<ResponseModel> getAllStoreOrders(@AuthenticationPrincipal Jwt jwt) {
        HttpStatus status = HttpStatus.OK;
        String message = "";
        String reason = "";
        List<OrderListModel> order = new ArrayList<>();
        try {
            order = this.orderService.getStoreOrders(jwt.getSubject());
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
                        .data(Map.of("orders", order))
                        .build()
        );
    }

    @GetMapping
    @RequestMapping("/store/{order-id}")
    public ResponseEntity<ResponseModel> getStoreOrder(@AuthenticationPrincipal Jwt jwt, @PathVariable("order-id") String orderId) {
        HttpStatus status = HttpStatus.OK;
        String message = "";
        String reason = "";
        OrderListModel order = null;
        try {
            order = this.orderService.getStoreOrder(jwt.getSubject(), orderId);
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
                        .status(status)
                        .statusCode(status.value())
                        .data(Map.of("order", order == null ? "" : order))
                        .build()
        );
    }

    @PutMapping
    @RequestMapping("/update/{order-id}")
    public ResponseEntity<ResponseModel> updateOrder(@AuthenticationPrincipal Jwt jwt, @PathVariable("order-id") String orderId, @RequestBody @Valid UpdateOrderModel updateOrder) {
        HttpStatus status = HttpStatus.OK;
        String message = "";
        String reason = "";
        boolean response = false;
        try {
            response = this.orderService.updateOrderInfo(jwt.getSubject(), orderId, updateOrder);
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
                        .status(status)
                        .statusCode(status.value())
                        .data(Map.of("updated", response))
                        .build()
        );
    }
}
