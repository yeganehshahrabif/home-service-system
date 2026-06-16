package ir.maktabsharif138.home_service_system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import ir.maktabsharif138.home_service_system.dto.request.HomeServiceCreateRequest;
import ir.maktabsharif138.home_service_system.dto.request.HomeServiceUpdateRequest;
import ir.maktabsharif138.home_service_system.dto.request.UserSearchRequest;
import ir.maktabsharif138.home_service_system.dto.response.CustomerOrderResponse;
import ir.maktabsharif138.home_service_system.dto.response.ExpertResponse;
import ir.maktabsharif138.home_service_system.dto.response.HomeServiceResponse;
import ir.maktabsharif138.home_service_system.dto.response.UserSearchResponse;
import ir.maktabsharif138.home_service_system.entity.enums.OrderStatus;
import ir.maktabsharif138.home_service_system.service.facade.AdminFacadeService;
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
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Admin API",
        description = "Administrative operations for managing services, experts and orders"
)
public class AdminController {

    private final AdminFacadeService adminFacadeService;

    @Operation(summary = "Create a new service")
    @PostMapping("/services")
    public ResponseEntity<HomeServiceResponse> createService(
            @Valid @RequestBody HomeServiceCreateRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(adminFacadeService.createService(request));
    }

    @Operation(summary = "Get service by id")
    @GetMapping("/services/{serviceId}")
    public ResponseEntity<HomeServiceResponse> getService(
            @PathVariable
            @Positive(message = "Service id must be positive")
            Long serviceId) {

        return ResponseEntity.ok(adminFacadeService.getHomeService(serviceId));
    }

    @Operation(summary = "Update service")
    @PutMapping("/services/{serviceId}")
    public ResponseEntity<HomeServiceResponse> updateService(
            @PathVariable
            @Positive(message = "Service id must be positive")
            Long serviceId,

            @Valid
            @RequestBody
            HomeServiceUpdateRequest request) {

        return ResponseEntity.ok(
                adminFacadeService.updateService(serviceId, request));
    }

    @Operation(summary = "Delete service")
    @DeleteMapping("/services/{serviceId}")
    public ResponseEntity<Void> deleteService(
            @PathVariable
            @Positive(message = "Service id must be positive")
            Long serviceId) {

        adminFacadeService.deleteService(serviceId);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all main services")
    @GetMapping("/services/main")
    public ResponseEntity<List<HomeServiceResponse>> getMainServices() {
        return ResponseEntity.ok(
                adminFacadeService.getAllMainServices());
    }

    @Operation(summary = "Get sub services by parent service id")
    @GetMapping("/services/{parentId}/subservices")
    public ResponseEntity<List<HomeServiceResponse>> getSubServices(
            @PathVariable
            @Positive(message = "Parent service id must be positive")
            Long parentId) {

        return ResponseEntity.ok(
                adminFacadeService.getSubServicesByParentId(parentId));
    }

    @Operation(summary = "Assign expert to sub service")
    @PutMapping("/subservices/{subServiceId}/experts/{expertId}")
    public ResponseEntity<Void> addExpertToSubService(
            @PathVariable
            @Positive(message = "Sub service id must be positive")
            Long subServiceId,
            @PathVariable
            @Positive(message = "Expert id must be positive")
            Long expertId) {

        adminFacadeService.addExpertToSubService(
                expertId,
                subServiceId
        );

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Remove expert from sub service")
    @DeleteMapping("/subservices/{subServiceId}/experts/{expertId}")
    public ResponseEntity<Void> removeExpertFromSubService(

            @PathVariable
            @Positive(message = "Sub service id must be positive")
            Long subServiceId,

            @PathVariable
            @Positive(message = "Expert id must be positive")
            Long expertId) {

        adminFacadeService.removeExpertFromSubService(
                expertId,
                subServiceId
        );

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get pending experts")
    @GetMapping("/experts/pending")
    public ResponseEntity<Page<ExpertResponse>> getPendingExperts(Pageable pageable) {

        return ResponseEntity.ok(
                adminFacadeService.getPendingExperts(pageable));
    }

    @Operation(summary = "Approve expert")
    @PutMapping("/experts/{expertId}/approve")
    public ResponseEntity<Void> approveExpert(

            @PathVariable
            @Positive(message = "Expert id must be positive")
            Long expertId) {

        adminFacadeService.approveExpert(expertId);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Reject expert")
    @PutMapping("/experts/{expertId}/reject")
    public ResponseEntity<Void> rejectExpert(
            @PathVariable
            @Positive(message = "Expert id must be positive")
            Long expertId) {

        adminFacadeService.rejectExpert(expertId);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get orders by status")
    @GetMapping("/orders")
    public ResponseEntity<Page<CustomerOrderResponse>> getOrdersByStatus(
            @RequestParam
            OrderStatus status,
            Pageable pageable) {

        return ResponseEntity.ok(
                adminFacadeService.getOrdersByStatus(status, pageable));
    }

    @Operation(summary = "Search users")
    @GetMapping("/search")
    public ResponseEntity<Page<UserSearchResponse>> searchUsers(
            @ModelAttribute
            UserSearchRequest request,
            Pageable pageable
    ) {

        return ResponseEntity.ok(
                adminFacadeService.searchUsers(request, pageable)
        );
    }


}