package ovh.homecitadel.uni.techbazar.Controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ovh.homecitadel.uni.techbazar.Entity.User.UserAddressEntity;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.NotNullException;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.ObjectNotFoundException;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.UnauthorizedAccessException;
import ovh.homecitadel.uni.techbazar.Helper.Model.ResponseModel;
import ovh.homecitadel.uni.techbazar.Helper.Model.User.User;
import ovh.homecitadel.uni.techbazar.Helper.Model.User.UserAddress;
import ovh.homecitadel.uni.techbazar.Helper.Model.WishlistResponse;
import ovh.homecitadel.uni.techbazar.Helper.Notification.MessageModel;
import ovh.homecitadel.uni.techbazar.Service.NotificationSystem;
import ovh.homecitadel.uni.techbazar.Service.User.UserService;
import ovh.homecitadel.uni.techbazar.Service.User.WishlistService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/REST/user")
@SecurityRequirement(name = "Keycloak")
public class UserController {

    private final UserService userService;
    private final WishlistService wishlistService;
    private final NotificationSystem notificationSystem;

    public UserController(UserService userService, WishlistService wishlistService, NotificationSystem notificationSystem) {
        this.userService = userService;
        this.wishlistService = wishlistService;
        this.notificationSystem = notificationSystem;
    }


    @PostMapping
    @RequestMapping("/new")
    public ResponseEntity<ResponseModel> newUser(@RequestBody @Valid User user) {
        HttpStatus status;
        String reason;

        if(this.userService.createUser(user)) {
            return ResponseEntity.ok(
                    ResponseModel.builder()
                            .timeStamp(LocalDateTime.now())
                            .status(HttpStatus.CREATED)
                            .statusCode(HttpStatus.CREATED.value())
                            .message("User Created")
                            .build()
            );
        }
        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST)
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .reason("IDK")
                        .build()
        );
    }

    @GetMapping
    @RequestMapping("/address")
    public ResponseEntity<ResponseModel> getAllUserAddress(@AuthenticationPrincipal Jwt jwt) {
        HttpStatus status;
        String reason;
        List<UserAddressEntity> address = new ArrayList<>();
        String message;

        try {
            address = this.userService.getUserAddress(jwt.getSubject());
            status = HttpStatus.OK;
            message = "address found.";
            reason = "";
        } catch (ObjectNotFoundException e) {
            status = HttpStatus.NOT_FOUND;
            reason = e.getMessage();
            message = "";
        }

        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(status.value())
                        .status(status)
                        .reason(reason)
                        .message(message)
                        .data(status == HttpStatus.OK ? Map.of("address", address): null)
                        .build()
        );
    }

    @GetMapping
    @RequestMapping("/address/{address-id}")
    public ResponseEntity<ResponseModel> getAddressById(@AuthenticationPrincipal Jwt jwt, @PathVariable("address-id") Long addressId) {
        HttpStatus status;
        String reason;
        UserAddressEntity userAddress = null;
        String message;

        try {
            userAddress = this.userService.getAddressById(jwt.getSubject(), addressId);
            status = HttpStatus.OK;
            message = "address found.";
            reason = "";
        } catch (ObjectNotFoundException e) {
            status = HttpStatus.NOT_FOUND;
            reason = e.getMessage();
            message = "";
        } catch (UnauthorizedAccessException e) {
            status = HttpStatus.UNAUTHORIZED;
            reason = e.getMessage();
            message = "";
        }

        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(status.value())
                        .status(status)
                        .reason(reason)
                        .message(message)
                        .data(status == HttpStatus.OK ? Map.of("address", userAddress): null)
                        .build()
        );
    }

    @PostMapping
    @RequestMapping("/address/new")
    public ResponseEntity<ResponseModel> newAddress(@AuthenticationPrincipal Jwt jwt, @RequestBody @Valid UserAddress userAddress) {
        UserAddressEntity address = null;
        String reason = "";
        String message = "";
        HttpStatus status = HttpStatus.CREATED;

        System.out.println("INSIDE THE NEW ADDRESS MAPPING");

        try {
            address = this.userService.newUserAddress(jwt.getSubject(), userAddress);
            message = "Address Created.";
        } catch (NotNullException e) {
            reason = e.getMessage();
            status = HttpStatus.BAD_REQUEST;
        }

        return ResponseEntity.ok(
          ResponseModel.builder()
                  .timeStamp(LocalDateTime.now())
                  .status(status)
                  .message(message)
                  .reason(reason)
                  .statusCode(status.value())
                  .data(Map.of("address", status == HttpStatus.CREATED ? address : ""))
                  .build()
        );
    }


    @PostMapping
    @RequestMapping("/address/set-default/{address-id}")
    public ResponseEntity<ResponseModel> setDefaultAddress(@AuthenticationPrincipal Jwt jwt, @PathVariable("address-id") Long addressId) {
        HttpStatus status = HttpStatus.OK;
        String reason = "";
        UserAddressEntity userAddress = null;
        String message = "";

        try {
            userAddress = this.userService.setDefaultAddress(addressId);
            message = "address updated.";
        } catch(ObjectNotFoundException e) {
            reason = e.getMessage();
            status = HttpStatus.NOT_FOUND;
        }
        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(status.value())
                        .status(status)
                        .reason(reason)
                        .message(message)
                        .data(status == HttpStatus.OK ? Map.of("address", userAddress): null)
                        .build()
        );
    }
    @PutMapping
    @RequestMapping("/address/edit/{address-id}")
    public ResponseEntity<ResponseModel> editAddress(@AuthenticationPrincipal Jwt jwt, @RequestBody @Valid UserAddress userAddress, @PathVariable("address-id") Long addressId) {
        HttpStatus status;
        String reason;
        UserAddressEntity address = null;
        String message;

        try {
            address = this.userService.editUserAddress(jwt.getSubject(), userAddress, addressId);
            status = HttpStatus.OK;
            message = "address found.";
            reason = "";
        } catch (ObjectNotFoundException e) {
            status = HttpStatus.NOT_FOUND;
            reason = e.getMessage();
            message = "";
        } catch (UnauthorizedAccessException e) {
            status = HttpStatus.UNAUTHORIZED;
            reason = e.getMessage();
            message = "";
        }

        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(status.value())
                        .status(status)
                        .reason(reason)
                        .message(message)
                        .data(status == HttpStatus.OK ? Map.of("address", address): null)
                        .build()
        );
    }

    @DeleteMapping
    @RequestMapping("/address/delete/{address-id}")
    public ResponseEntity<ResponseModel> deleteAddress(@AuthenticationPrincipal Jwt jwt, @PathVariable("address-id") Long addressId) {
        String reason = "";
        String message = "";
        HttpStatus status = HttpStatus.OK;

        try {
            this.userService.deleteAddress(jwt.getSubject(), addressId);
            message = "Address deleted.";
        } catch (UnauthorizedAccessException e) {
            reason = e.getMessage();
            status = HttpStatus.UNAUTHORIZED;
        } catch (ObjectNotFoundException e) {
            reason = e.getMessage();
            status = HttpStatus.NOT_FOUND;
        }

        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .reason(reason)
                        .message(message)
                        .statusCode(status.value())
                        .status(status)
                        .build()
        );
    }

    @GetMapping
    @RequestMapping("/wishlist")
    public ResponseEntity<ResponseModel> getWishlist(@AuthenticationPrincipal Jwt jwt) {

        String message = "";
        String reason = "";
        HttpStatus status = HttpStatus.OK;
        WishlistResponse response = null;

        try {
            response = this.wishlistService.getWishlist(jwt.getSubject());
            message = "wishlist";
        } catch (ObjectNotFoundException e) {
            reason = e.getMessage();
            status = HttpStatus.NOT_FOUND;
        }
        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(status.value())
                        .status(status)
                        .data(Map.of("response", response == null ? "" : response))
                        .build()
        );

    }

    @PostMapping
    @RequestMapping("/wishlist/add/{product-id}")
    public ResponseEntity<ResponseModel> addToWishlist(@AuthenticationPrincipal Jwt jwt, @PathVariable("product-id") Long productId) {
        String message = "";
        String reason = "";
        HttpStatus status = HttpStatus.OK;
        boolean response = false;

        try {
            response = this.wishlistService.addToWishlist(jwt.getSubject(), productId);
            message = "Added";
        } catch (ObjectNotFoundException e) {
            reason = e.getMessage();
            status = HttpStatus.NOT_FOUND;
        }
        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(status.value())
                        .status(status)
                        .data(Map.of("response", response))
                        .build()
        );
    }

    @DeleteMapping
    @RequestMapping("/wishlist/remove/{product-id}")
    public ResponseEntity<ResponseModel> removeFromWishlist(@AuthenticationPrincipal Jwt jwt, @PathVariable("product-id") Long productId) {
        String message = "";
        String reason = "";
        HttpStatus status = HttpStatus.OK;
        boolean response = false;

        try {
            response = this.wishlistService.removeFromWishlist(jwt.getSubject(), productId);
            message = "Removed";
        } catch (ObjectNotFoundException e) {
            reason = e.getMessage();
            status = HttpStatus.NOT_FOUND;
        }
        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(status.value())
                        .status(status)
                        .data(Map.of("response", response))
                        .build()
        );
    }

    @GetMapping
    @RequestMapping("/wishlist/{product-id}")
    public ResponseEntity<ResponseModel> isProductWishlisted(@AuthenticationPrincipal Jwt jwt, @PathVariable("product-id") Long productId) {
        String message = "";
        String reason = "";
        HttpStatus status = HttpStatus.OK;
        boolean response = false;

        try {
            if(this.wishlistService.isProductWishlisted(jwt.getSubject(), productId)) {
                message = "product is wishlisted";
                response = true;
            } else {
                message = "product is not wishlisted";
            }
        } catch (ObjectNotFoundException e) {
            message = "product Not found";
            reason = e.getMessage();
            status = HttpStatus.NOT_FOUND;
        }

        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(status.value())
                        .status(status)
                        .data(Map.of("response", response))
                        .build()
        );
    }

    @GetMapping
    @RequestMapping("/notification/unread")
    public ResponseEntity<ResponseModel> getUnreadNotifications(@AuthenticationPrincipal Jwt jwt) {
        String message = "";
        HttpStatus status = HttpStatus.OK;
        int value = 0;
        try {
            value = this.userService.getUnreadNotification(jwt.getSubject());
            message = "Unread Notifications";
        } catch (ObjectNotFoundException e) {
            message = e.getMessage();
            status = HttpStatus.NOT_FOUND;
            value =  -1;
        }
        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .message(message)
                        .statusCode(status.value())
                        .status(status)
                        .data(Map.of("response", value))
                        .build()
        );
    }

    @GetMapping
    @RequestMapping("/notification/")
    public ResponseEntity<ResponseModel> getUserNotifications(@AuthenticationPrincipal Jwt jwt) {
        String message = "";
        HttpStatus status = HttpStatus.OK;
        List<MessageModel> messages = new ArrayList<>();
        try {
            messages = this.notificationSystem.getUserNotifications(jwt.getSubject());
            message = "User Notifications";
        } catch (ObjectNotFoundException e) {
            message = e.getMessage();
            status = HttpStatus.NOT_FOUND;
        }

        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .status(status)
                        .statusCode(status.value())
                        .message(message)
                        .data(Map.of("messages", messages))
                        .build()
        );
    }

    @PostMapping
    @RequestMapping("/notification/send")
    public ResponseEntity<ResponseModel> sendMessage(@AuthenticationPrincipal Jwt jwt, @RequestBody @Valid MessageModel message) {
        String response = "";
        HttpStatus status = HttpStatus.OK;

        try {
            this.notificationSystem.createNotification(message);
            response = "Message Sent.";
        } catch (ObjectNotFoundException e) {
            response = e.getMessage();
            status = HttpStatus.NOT_FOUND;
        }

        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .message(response)
                        .statusCode(status.value())
                        .status(status)
                        .data(Map.of("", "null"))
                        .build()
        );


    }

}
