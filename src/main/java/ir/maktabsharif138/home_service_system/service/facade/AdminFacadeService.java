package ir.maktabsharif138.home_service_system.service.facade;

import ir.maktabsharif138.home_service_system.dto.request.*;
import ir.maktabsharif138.home_service_system.dto.response.*;
import ir.maktabsharif138.home_service_system.entity.enums.OrderStatus;
import java.util.List;

public interface AdminFacadeService {

    HomeServiceResponse createService(HomeServiceCreateRequest request);
    HomeServiceResponse updateService(Long id, HomeServiceUpdateRequest request);
    void deleteService(Long id);
    List<HomeServiceResponse> getAllMainServices();
    List<HomeServiceResponse> getSubServicesByParentId(Long parentId);


    List<ExpertResponse> getPendingExperts();
    void approveExpert(Long id);
    void rejectExpert(Long id);
    void addExpertToSubService(Long expertId, Long subServiceId);
    void removeExpertFromSubService(Long expertId, Long subServiceId);


    List<CustomerOrderResponse> getOrdersByStatus(OrderStatus status);
}