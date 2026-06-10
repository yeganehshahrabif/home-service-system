package ir.maktabsharif138.home_service_system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import ir.maktabsharif138.home_service_system.dto.request.ExpertLoginRequest;
import ir.maktabsharif138.home_service_system.dto.request.ExpertRegisterRequest;
import ir.maktabsharif138.home_service_system.dto.request.ExpertUpdateRequest;
import ir.maktabsharif138.home_service_system.dto.request.OfferCreateRequest;
import ir.maktabsharif138.home_service_system.dto.response.CustomerOrderResponse;
import ir.maktabsharif138.home_service_system.dto.response.ExpertResponse;
import ir.maktabsharif138.home_service_system.dto.response.LoginResponse;
import ir.maktabsharif138.home_service_system.dto.response.OfferResponse;
import ir.maktabsharif138.home_service_system.service.facade.ExpertFacadeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
            @RequestPart("image")
            MultipartFile image) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(expertFacadeService.register(
                                request,
                                image)
                );
    }

    @Operation(summary = "Expert login")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid
            @RequestBody
            ExpertLoginRequest request) {

        return ResponseEntity.ok(
                expertFacadeService.login(request));
    }

    @Operation(summary = "Get expert profile")
    @GetMapping("/{expertId}")
    public ResponseEntity<ExpertResponse> getProfile(
            @PathVariable
            @Positive(message = "Expert id must be positive")
            Long expertId) {

        return ResponseEntity.ok(
                expertFacadeService.getProfile(expertId));
    }

    @Operation(summary = "Update expert profile")
    @PutMapping(
            value = "/{expertId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ExpertResponse> updateProfile(
            @PathVariable
            @Positive(message = "Expert id must be positive")
            Long expertId,
            @Valid
            @ModelAttribute
            ExpertUpdateRequest request,
            @RequestPart(required = false)
            MultipartFile image) {

        return ResponseEntity.ok(
                expertFacadeService.updateProfile(expertId, request, image)
        );
    }

    @Operation(summary = "Create offer for order")
    @PostMapping("/{expertId}/offers")
    public ResponseEntity<OfferResponse> createOffer(
            @PathVariable
            @Positive(message = "Expert id must be positive")
            Long expertId,
            @Valid
            @RequestBody
            OfferCreateRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(expertFacadeService.createOffer(expertId, request));
    }

    @Operation(summary = "Get expert offers")
    @GetMapping("/{expertId}/offers")
    public ResponseEntity<List<OfferResponse>> getMyOffers(
            @PathVariable
            @Positive(message = "Expert id must be positive")
            Long expertId) {

        return ResponseEntity.ok(
                expertFacadeService.getMyOffers(expertId));
    }

    @Operation(summary = "Get available orders for expert")
    @GetMapping("/{expertId}/orders/available")
    public ResponseEntity<List<CustomerOrderResponse>> getAvailableOrders(
            @PathVariable
            @Positive(message = "Expert id must be positive")
            Long expertId) {

        return ResponseEntity.ok(
                expertFacadeService.getAvailableOrdersForExpert(
                        expertId
                )
        );
    }

}