package ovh.homecitadel.uni.techbazar.Controller;

import jakarta.validation.Valid;
import jakarta.ws.rs.Path;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ovh.homecitadel.uni.techbazar.Entity.Auction.AuctionEntity;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.AuctionException;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.ObjectNotFoundException;
import ovh.homecitadel.uni.techbazar.Helper.Exceptions.UnauthorizedAccessException;
import ovh.homecitadel.uni.techbazar.Helper.Model.Auction.AuctionRequest;
import ovh.homecitadel.uni.techbazar.Helper.Model.Auction.BidModel;
import ovh.homecitadel.uni.techbazar.Helper.Model.ResponseModel;
import ovh.homecitadel.uni.techbazar.Service.Auction.AuctionService;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/REST/auction")
public class AuctionController {

    private final AuctionService auctionService;


    public AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @GetMapping
    @RequestMapping("/")
    public ResponseEntity<ResponseModel> getAll() {
        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("All Auctions")
                        .data(Map.of("auctions", this.auctionService.getAll()))
                        .build()
        );
    }

    @GetMapping
    @RequestMapping("/{auction-id}")
    public ResponseEntity<ResponseModel> getByAuctionId(@PathVariable("auction-id") Long auctionId) {
        String message = "";
        String reason = "";
        HttpStatus status = HttpStatus.OK;
        AuctionEntity auction = null;

        try {
            auction = this.auctionService.getAuctionById(auctionId);
            message = "auction";
        } catch(ObjectNotFoundException e) {
            reason = e.getMessage();
            status = HttpStatus.NOT_FOUND;
        }

        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .status(status)
                        .statusCode(status.value())
                        .message(message)
                        .reason(reason)
                        .data(Map.of("auction", auction == null ? "" : auction))
                        .build()
        );
    }

    @PostMapping
    @RequestMapping("/new")
    public ResponseEntity<ResponseModel> newAuction(@AuthenticationPrincipal Jwt jwt, @RequestBody @Valid AuctionRequest request) {
        String message = "";
        String reason = "";
        HttpStatus status = HttpStatus.OK;
        AuctionEntity auction = null;

        try {
            auction = this.auctionService.newAuction(jwt.getSubject(), request);
            message = "auction";
        } catch(ObjectNotFoundException e) {
            reason = e.getMessage();
            status = HttpStatus.NOT_FOUND;
        }

        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .status(status)
                        .statusCode(status.value())
                        .message(message)
                        .reason(reason)
                        .data(Map.of("auction", auction == null ? "" : auction))
                        .build()
        );
    }

    @PutMapping
    @RequestMapping("/edit/{auction-id}")
    public ResponseEntity<ResponseModel> editAuction(@AuthenticationPrincipal Jwt jwt, @RequestBody @Valid AuctionRequest request, @PathVariable("auction-id") Long auctionId) {
        String message = "";
        String reason = "";
        HttpStatus status = HttpStatus.OK;
        AuctionEntity auction = null;

        try {
            auction = this.auctionService.editAuction(jwt.getSubject(), request, auctionId);
            message = "auction";
        } catch(ObjectNotFoundException e) {
            reason = e.getMessage();
            status = HttpStatus.NOT_FOUND;
        } catch (UnauthorizedAccessException e) {
            reason = e.getMessage();
            status = HttpStatus.UNAUTHORIZED;
        } catch (AuctionException e) {
            reason = e.getMessage();
            status = HttpStatus.BAD_REQUEST;
        }

        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .status(status)
                        .statusCode(status.value())
                        .message(message)
                        .reason(reason)
                        .data(Map.of("auction", auction == null ? "" : auction))
                        .build()
        );
    }

    @PostMapping
    @RequestMapping("/{auction-id}/bid")
    public ResponseEntity<ResponseModel> placeBid(@AuthenticationPrincipal Jwt jwt, @RequestBody @Valid BidModel bidModel, @PathVariable("auction-id") Long auctionId) {
        String message = "";
        String reason = "";
        HttpStatus status = HttpStatus.OK;
        boolean bidPlaced = false;

        try {
            bidPlaced = this.auctionService.placeBid(jwt.getSubject(), bidModel, auctionId);
            message = "auction";
        } catch(ObjectNotFoundException e) {
            reason = e.getMessage();
            status = HttpStatus.NOT_FOUND;
        } catch (AuctionException e) {
            reason = e.getMessage();
            status = HttpStatus.BAD_REQUEST;
        }

        return ResponseEntity.ok(
                ResponseModel.builder()
                        .timeStamp(LocalDateTime.now())
                        .status(status)
                        .statusCode(status.value())
                        .message(message)
                        .reason(reason)
                        .data(Map.of("bidPlaced", bidPlaced))
                        .build()
        );
    }
}
