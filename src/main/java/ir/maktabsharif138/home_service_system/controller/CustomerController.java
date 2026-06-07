package ir.maktabsharif138.home_service_system.controller;
import ir.maktabsharif138.home_service_system.dto.request.*;
import ir.maktabsharif138.home_service_system.dto.response.*;
import ir.maktabsharif138.home_service_system.mapper.CustomerMapper;
import ir.maktabsharif138.home_service_system.mapper.CustomerOrderMapper;
import ir.maktabsharif138.home_service_system.mapper.ReviewMapper;
import ir.maktabsharif138.home_service_system.service.CustomerOrderService;
import ir.maktabsharif138.home_service_system.service.CustomerService;
import ir.maktabsharif138.home_service_system.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerOrderService orderService;
    private final ReviewService reviewService;
    private final CustomerMapper customerMapper;
    private final CustomerOrderMapper orderMapper;
    private final ReviewMapper reviewMapper;


    @PostMapping("/register")
    public ResponseEntity<CustomerResponse> register(@Valid @RequestBody CustomerRegisterRequest request) {
        // استفاده از CustomerService.register(CustomerRegisterRequest): CustomerResponse
        return null;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody CustomerLoginRequest request) {
        // استفاده از CustomerService.login(CustomerLoginRequest): LoginResponse
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomer(@PathVariable Long id) {
        // استفاده از CustomerService.findById(id): CustomerResponse
        return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateProfile(@PathVariable Long id,
                                                          @Valid @RequestBody CustomerUpdateRequest request) {
        // استفاده از CustomerService.updateProfile(id, request): CustomerResponse
        return null;
    }


    @PostMapping("/{customerId}/orders")
    public ResponseEntity<CustomerOrderResponse> createOrder(@PathVariable Long customerId,
                                                             @Valid @RequestBody OrderCreateRequest request) {
        // استفاده از OrderService.createOrder(customerId, request): OrderResponse
        return null;
    }

    @GetMapping("/{customerId}/orders")
    public ResponseEntity<List<CustomerOrderResponse>> getCustomerOrders(@PathVariable Long customerId) {
        // استفاده از OrderService.getOrdersByCustomer(customerId): List<OrderResponse>
        return null;
    }

    @GetMapping("/orders/{orderId}/offers")
    public ResponseEntity<List<OfferResponse>> getOffersForOrder(@PathVariable Long orderId,
                                                                 @RequestParam(defaultValue = "price") String sortBy) {
        // اگر sortBy=price -> OfferService.getOffersByOrderSortedByPrice(orderId)
        // اگر sortBy=rating -> OfferService.getOffersByOrderSortedByExpertRating(orderId)
        return null;
    }

    @PatchMapping("/orders/{orderId}/accept-offer/{offerId}")
    public ResponseEntity<CustomerOrderResponse> acceptOffer(@PathVariable Long orderId,
                                                             @PathVariable Long offerId) {
        // استفاده از OrderService.acceptOffer(orderId, offerId): OrderResponse
        return null;
    }

    @PatchMapping("/orders/{orderId}/start")
    public ResponseEntity<CustomerOrderResponse> startOrder(@PathVariable Long orderId) {
        // استفاده از OrderService.startOrder(orderId): OrderResponse
        return null;
    }

    @PatchMapping("/orders/{orderId}/complete")
    public ResponseEntity<CustomerOrderResponse> completeOrder(@PathVariable Long orderId) {
        // استفاده از OrderService.completeOrder(orderId): OrderResponse
        return null;
    }


    @PostMapping("/reviews")
    public ResponseEntity<ReviewResponse> addReview(@Valid @RequestBody ReviewCreateRequest request) {
        // استفاده از ReviewService.addReview(request): ReviewResponse
        return null;
    }

    @GetMapping("/services/main")
    public ResponseEntity<List<HomeServiceResponse>> getAllMainServices() {
        // استفاده از HomeServiceService.getAllMainServices(): List<HomeServiceResponse>
        return null;
    }

    @GetMapping("/services/{parentId}/sub-services")
    public ResponseEntity<List<HomeServiceResponse>> getSubServices(@PathVariable Long parentId) {
        // استفاده از HomeServiceService.getSubServicesByParentId(parentId): List<HomeServiceResponse>
        return null;
    }

}