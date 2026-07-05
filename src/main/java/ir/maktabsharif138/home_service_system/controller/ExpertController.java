package ir.maktabsharif138.home_service_system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import ir.maktabsharif138.home_service_system.dto.request.ExpertLoginRequest;
import ir.maktabsharif138.home_service_system.dto.request.ExpertRegisterRequest;
import ir.maktabsharif138.home_service_system.dto.request.ExpertUpdateRequest;
import ir.maktabsharif138.home_service_system.dto.request.OfferCreateRequest;
import ir.maktabsharif138.home_service_system.dto.response.*;
import ir.maktabsharif138.home_service_system.service.facade.ExpertFacadeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/experts")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Expert API",
        description = "Expert operations including profile management, offers and available orders"
)
public class ExpertController {

    private final ExpertFacadeService expertFacadeService;

    @Operation(summary = "Register new expert")
    @PostMapping(
            value = "/register",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ExpertResponse> register(
            @Valid
            @ModelAttribute
            ExpertRegisterRequest request,
            @RequestPart(required = false)
            MultipartFile image
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(expertFacadeService.register(request, image));
    }

//    @Operation(summary = "Expert login")
//    @PostMapping("/login")
//    public ResponseEntity<LoginResponse> login(
//            @Valid
//            @RequestBody
//            ExpertLoginRequest request) {
//
//        return ResponseEntity.ok(
//                expertFacadeService.login(request));
//    }

    @PreAuthorize("hasRole('EXPERT')")
    @Operation(summary = "Get expert profile")
    @GetMapping("/me")
    public ResponseEntity<ExpertResponse> getProfile() {

        return ResponseEntity.ok(
                expertFacadeService.getProfile());
    }

    @PreAuthorize("hasRole('EXPERT')")
    @Operation(summary = "Update expert profile")
    @PutMapping(
            value = "/me",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ExpertResponse> updateProfile(
            @Valid
            @ModelAttribute
            ExpertUpdateRequest request,
            @RequestPart(required = false)
            MultipartFile image) {

        return ResponseEntity.ok(
                expertFacadeService.updateProfile(request, image)
        );
    }

    @PreAuthorize("hasRole('EXPERT')")
    @Operation(summary = "Create offer for order")
    @PostMapping("/me/offers")
    public ResponseEntity<OfferResponse> createOffer(
            @Valid
            @RequestBody
            OfferCreateRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(expertFacadeService.createOffer(request));
    }

    @PreAuthorize("hasRole('EXPERT')")
    @Operation(summary = "Get expert offers")
    @GetMapping("/me/offers")
    public ResponseEntity<Page<OfferResponse>> getMyOffers(
            Pageable pageable) {

        return ResponseEntity.ok(
                expertFacadeService.getMyOffers(pageable));
    }

    @PreAuthorize("hasRole('EXPERT')")
    @Operation(summary = "Get available orders for expert")
    @GetMapping("/me/orders/available")
    public ResponseEntity<Page<CustomerOrderResponse>> getAvailableOrders(
            Pageable pageable) {

        return ResponseEntity.ok(
                expertFacadeService.getAvailableOrdersForExpert(pageable)
        );
    }


    @PreAuthorize("hasRole('EXPERT')")
    @Operation(summary = "Get expert order history")
    @GetMapping("/me/orders/history")
    public ResponseEntity<Page<OrderHistorySummaryResponse>> getOrderHistory(
            Pageable pageable
    ) {

        return ResponseEntity.ok(
                expertFacadeService.getOrderHistory(pageable)
        );
    }

    @PreAuthorize("hasRole('EXPERT')")
    @Operation(summary = "Get expert order details")
    @GetMapping("/me/orders/history/{orderId}")
    public ResponseEntity<ExpertOrderHistoryDetailsResponse> getOrderDetails(
            @PathVariable Long orderId
    ) {

        return ResponseEntity.ok(
                expertFacadeService.getOrderDetails(orderId)
        );
    }

    @PreAuthorize("hasRole('EXPERT')")
    @Operation(summary = "Get exert rating for a specific order")
    @GetMapping("/me/orders/{orderId}/rating")
    public ResponseEntity<ExpertOrderRatingResponse> getOrderRating(
            @PathVariable
            @Positive(message = "Order id must be positive")
            Long orderId
    ) {

        return ResponseEntity.ok(
                expertFacadeService.getOrderRating(orderId));
    }

}