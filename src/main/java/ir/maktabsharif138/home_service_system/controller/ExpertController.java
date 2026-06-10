//package ir.maktabsharif138.home_service_system.controller;
//
//import ir.maktabsharif138.home_service_system.dto.request.*;
//import ir.maktabsharif138.home_service_system.dto.response.*;
//import ir.maktabsharif138.home_service_system.mapper.ExpertMapper;
//import ir.maktabsharif138.home_service_system.mapper.OfferMapper;
//import ir.maktabsharif138.home_service_system.service.ExpertService;
//import ir.maktabsharif138.home_service_system.service.OfferService;
//import ir.maktabsharif138.home_service_system.service.facade.ExpertFacadeService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/experts")
//@RequiredArgsConstructor
//public class ExpertController {
//
//   private final ExpertFacadeService expertFacadeService;
//    private final ExpertMapper expertMapper;
//    private final OfferMapper offerMapper;
//
//    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<ExpertResponse> register(@Valid @RequestBody ExpertRegisterRequest request,
//                                                   @RequestPart("image") MultipartFile image) {
//        // استفاده از ExpertService.register(request): ExpertResponse
//        return null;
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<LoginResponse> login(@Valid @RequestBody ExpertLoginRequest request) {
//        // استفاده از ExpertService.login(request): LoginResponse
//        return null;
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<ExpertResponse> getExpert(@PathVariable Long id) {
//        // استفاده از ExpertService.findById(id): ExpertResponse
//        return null;
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<ExpertResponse> updateProfile(@PathVariable Long id,
//                                                        @Valid @RequestBody ExpertUpdateRequest request) {
//        // استفاده از ExpertService.updateProfile(id, request): ExpertResponse
//        return null;
//    };
//
//    @PostMapping("/offers")
//    public ResponseEntity<OfferResponse> createOffer(@Valid @RequestBody OfferCreateRequest request) {
//        // استفاده از OfferService.createOffer(request): OfferResponse
//        return null;
//    }
//
//    @GetMapping("/{expertId}/offers")
//    public ResponseEntity<List<OfferResponse>> getExpertOffers(@PathVariable Long expertId) {
//        // استفاده از OfferService.getOffersByExpertId(expertId): List<OfferResponse>
//        return null;
//    }
//
//    @GetMapping("/orders/available")
//    public ResponseEntity<List<CustomerOrderResponse>> getAvailableOrdersForExpert() {
//        // استفاده از OrderService.getAvailableOrdersForExpert(): List<OrderResponse>
//        // (سفارشات با وضعیت WAITING_FOR_OFFERS یا WAITING_FOR_SELECTION)
//        return null;
//    }
//
//}