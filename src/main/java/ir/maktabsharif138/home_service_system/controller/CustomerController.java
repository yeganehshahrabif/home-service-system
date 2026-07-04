package ir.maktabsharif138.home_service_system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import ir.maktabsharif138.home_service_system.dto.request.*;
import ir.maktabsharif138.home_service_system.dto.response.*;
import ir.maktabsharif138.home_service_system.entity.enums.SortBy;
import ir.maktabsharif138.home_service_system.service.facade.CustomerFacadeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Customer API",
        description = "Customer operations including profile management, orders, offers and reviews"
)
public class CustomerController {

    private final CustomerFacadeService customerFacadeService;

    @Operation(summary = "Register new customer")
    @PostMapping("/register")
    public ResponseEntity<CustomerResponse> register(
            @Valid @RequestBody CustomerRegisterRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(customerFacadeService.register(request));
    }

//    @Operation(summary = "Customer login")
//    @PostMapping("/login")
//    public ResponseEntity<LoginResponse> login(
//            @Valid @RequestBody CustomerLoginRequest request) {
//
//        return ResponseEntity.ok(
//                customerFacadeService.login(request));
//    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Get customer profile")
//    @GetMapping("/{customerId}")
    @GetMapping("/me")
    public ResponseEntity<CustomerResponse> getProfile() {

        return ResponseEntity.ok(
                customerFacadeService.getProfile());
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Update customer profile")
    @PutMapping("/me")
    public ResponseEntity<CustomerResponse> updateProfile(
            @Valid
            @RequestBody
            CustomerUpdateRequest request) {

        return ResponseEntity.ok(
                customerFacadeService.updateProfile(request));
    }

    @Operation(summary = "Get all main services")
    @GetMapping("/services")
    public ResponseEntity<List<HomeServiceResponse>> getMainServices() {
        return ResponseEntity.ok(
                customerFacadeService.getAllMainServices());
    }

    @Operation(summary = "Get sub services")
    @GetMapping("/services/{parentId}/subservices")
    public ResponseEntity<List<HomeServiceResponse>> getSubServices(
            @PathVariable
            @Positive(message = "Parent service id must be positive")
            Long parentId) {

        return ResponseEntity.ok(
                customerFacadeService.getSubServices(parentId));
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Create new order")
    @PostMapping("/me/orders")
    public ResponseEntity<CustomerOrderResponse> createOrder(
            @Valid
            @RequestBody
            OrderCreateRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(
                customerFacadeService.createOrder(request));
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Get customer orders")
    @GetMapping("/me/orders")
    public ResponseEntity<Page<CustomerOrderResponse>> getMyOrders(
            Pageable pageable) {

        return ResponseEntity.ok(
                customerFacadeService.getMyOrders(pageable));
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Start order")
    @PutMapping("/orders/{orderId}/start")
    public ResponseEntity<CustomerOrderResponse> startOrder(
            @PathVariable
            @Positive(message = "Order id must be positive")
            Long orderId) {

        return ResponseEntity.ok(
                customerFacadeService.startOrder(orderId));
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Complete order")
    @PutMapping("/orders/{orderId}/complete")
    public ResponseEntity<CustomerOrderResponse> completeOrder(
            @PathVariable
            @Positive(message = "Order id must be positive")
            Long orderId) {

        return ResponseEntity.ok(
                customerFacadeService.completeOrder(orderId));
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Get order offers sorted by price or expert score")
    @GetMapping("/me/orders/{orderId}/offers")
    public ResponseEntity<Page<OfferResponse>> getOrderOffers(
            @PathVariable
            @Positive(message = "Order id must be positive")
            Long orderId,
            @RequestParam(defaultValue = "PRICE")
            SortBy sortBy,
            Pageable pageable) {

        return ResponseEntity.ok(
                customerFacadeService.getOrderOffers(
                        orderId,
                        sortBy,
                        pageable
                ));
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Accept offer")
    @PutMapping("/me/orders/{orderId}/offers/{offerId}/accept")
    public ResponseEntity<OfferResponse> acceptOffer(
            @PathVariable
            @Positive(message = "Order id must be positive")
            Long orderId,
            @PathVariable
            @Positive(message = "Offer id must be positive")
            Long offerId) {

        return ResponseEntity.ok(
                customerFacadeService.acceptOffer(
                        orderId,
                        offerId
                ));
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Add review for expert")
    @PostMapping("/me/reviews")
    public ResponseEntity<ReviewResponse> addReview(
            @Valid
            @RequestBody
            ReviewCreateRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        customerFacadeService.addReview(request)
                );
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Get customer order history")
    @GetMapping("/me/orders/history")
    public ResponseEntity<Page<CustomerOrderResponse>> getOrderHistory(
            @ModelAttribute OrderHistoryFilterRequest request,
            Pageable pageable
    ) {

        return ResponseEntity.ok(
                customerFacadeService.getOrderHistory(
                        request,
                        pageable
                )
        );
    }
}