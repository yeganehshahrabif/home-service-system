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

    @Operation(summary = "Get customer profile")
    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> getProfile(
            @PathVariable
            @Positive(message = "Customer id must be positive")
            Long customerId) {

        return ResponseEntity.ok(
                customerFacadeService.getProfile(customerId));
    }

    @Operation(summary = "Update customer profile")
    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> updateProfile(
            @PathVariable
            @Positive(message = "Customer id must be positive")
            Long customerId,
            @Valid
            @RequestBody
            CustomerUpdateRequest request) {

        return ResponseEntity.ok(
                customerFacadeService.updateProfile(customerId, request));
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

    @Operation(summary = "Create new order")
    @PostMapping("/{customerId}/orders")
    public ResponseEntity<CustomerOrderResponse> createOrder(
            @PathVariable
            @Positive(message = "Customer id must be positive")
            Long customerId,
            @Valid
            @RequestBody
            OrderCreateRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(
                customerFacadeService.createOrder(customerId, request));
    }

    @Operation(summary = "Get customer orders")
    @GetMapping("/{customerId}/orders")
    public ResponseEntity<Page<CustomerOrderResponse>> getMyOrders(
            @PathVariable
            @Positive(message = "Customer id must be positive")
            Long customerId,
            Pageable pageable) {

        return ResponseEntity.ok(
                customerFacadeService.getMyOrders(customerId, pageable));
    }

    @Operation(summary = "Start order")
    @PutMapping("/orders/{orderId}/start")
    public ResponseEntity<CustomerOrderResponse> startOrder(
            @PathVariable
            @Positive(message = "Order id must be positive")
            Long orderId) {

        return ResponseEntity.ok(
                customerFacadeService.startOrder(orderId));
    }

    @Operation(summary = "Complete order")
    @PutMapping("/orders/{orderId}/complete")
    public ResponseEntity<CustomerOrderResponse> completeOrder(
            @PathVariable
            @Positive(message = "Order id must be positive")
            Long orderId) {

        return ResponseEntity.ok(
                customerFacadeService.completeOrder(orderId));
    }

    @Operation(summary = "Get order offers sorted by price or expert score")
    @GetMapping("/{customerId}/orders/{orderId}/offers")
    public ResponseEntity<Page<OfferResponse>> getOrderOffers(
            @PathVariable
            @Positive(message = "Customer id must be positive")
            Long customerId,
            @PathVariable
            @Positive(message = "Order id must be positive")
            Long orderId,
            @RequestParam(defaultValue = "PRICE")
            SortBy sortBy,
            Pageable pageable) {

        return ResponseEntity.ok(
                customerFacadeService.getOrderOffers(
                        customerId,
                        orderId,
                        sortBy,
                        pageable
                ));
    }

    @Operation(summary = "Accept offer")
    @PutMapping("/{customerId}/orders/{orderId}/offers/{offerId}/accept")
    public ResponseEntity<OfferResponse> acceptOffer(
            @PathVariable
            @Positive(message = "Customer id must be positive")
            Long customerId,
            @PathVariable
            @Positive(message = "Order id must be positive")
            Long orderId,
            @PathVariable
            @Positive(message = "Offer id must be positive")
            Long offerId) {

        return ResponseEntity.ok(
                customerFacadeService.acceptOffer(
                        customerId,
                        orderId,
                        offerId
                ));
    }

    @Operation(summary = "Add review for expert")
    @PostMapping("/{customerId}/reviews")
    public ResponseEntity<ReviewResponse> addReview(
            @PathVariable
            @Positive(message = "Customer id must be positive")
            Long customerId,
            @Valid
            @RequestBody
            ReviewCreateRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        customerFacadeService.addReview(customerId, request)
                );
    }

    @Operation(summary = "Get customer order history")
    @GetMapping("/{customerId}/orders/history")
    public ResponseEntity<Page<CustomerOrderResponse>> getOrderHistory(
            @PathVariable Long customerId,
            @ModelAttribute OrderHistoryFilterRequest request,
            Pageable pageable
    ) {

        return ResponseEntity.ok(
                customerFacadeService.getOrderHistory(
                        customerId,
                        request,
                        pageable
                )
        );
    }
}