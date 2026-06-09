package ir.maktabsharif138.home_service_system.controller;

import ir.maktabsharif138.home_service_system.dto.request.HomeServiceCreateRequest;
import ir.maktabsharif138.home_service_system.dto.request.HomeServiceUpdateRequest;
import ir.maktabsharif138.home_service_system.dto.response.ExpertResponse;
import ir.maktabsharif138.home_service_system.dto.response.HomeServiceResponse;
import ir.maktabsharif138.home_service_system.dto.response.CustomerOrderResponse;
import ir.maktabsharif138.home_service_system.mapper.HomeServiceMapper;
import ir.maktabsharif138.home_service_system.service.ExpertService;
import ir.maktabsharif138.home_service_system.service.HomeServiceService;
import ir.maktabsharif138.home_service_system.service.facade.AdminFacadeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

 private final AdminFacadeService adminFacadeService;
    private final HomeServiceMapper homeServiceMapper;


    @PostMapping("/services")
    public ResponseEntity<HomeServiceResponse> createService(@Valid @RequestBody HomeServiceCreateRequest request) {
        // استفاده از HomeServiceService.create(request): HomeServiceResponse
        return null;
    }

    @PutMapping("/services/{id}")
    public ResponseEntity<HomeServiceResponse> updateService(@PathVariable Long id,
                                                             @Valid @RequestBody HomeServiceUpdateRequest request) {
        // استفاده از HomeServiceService.update(id, request): HomeServiceResponse
        return null;
    }

    @DeleteMapping("/services/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        // استفاده از HomeServiceService.delete(id)
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

    @GetMapping("/experts/pending")
    public ResponseEntity<List<ExpertResponse>> getPendingExperts() {

        return null;
    }

    @PatchMapping("/experts/{id}/approve")
    public ResponseEntity<Void> approveExpert(@PathVariable Long id) {
        return null;
    }

    @PatchMapping("/experts/{id}/reject")
    public ResponseEntity<Void> rejectExpert(@PathVariable Long id) {

        return null;
    }

    @PostMapping("/experts/{expertId}/sub-services/{subServiceId}")
    public ResponseEntity<Void> addExpertToSubService(@PathVariable Long expertId,
                                                      @PathVariable Long subServiceId) {

        return null;
    }

    @DeleteMapping("/experts/{expertId}/sub-services/{subServiceId}")
    public ResponseEntity<Void> removeExpertFromSubService(@PathVariable Long expertId,
                                                           @PathVariable Long subServiceId) {
        return null;
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<CustomerOrderResponse>> getOrdersByStatus(@PathVariable String status) {
        // استفاده از OrderService.getOrdersByStatus(OrderStatus.valueOf(status))
        return null;
    }
}