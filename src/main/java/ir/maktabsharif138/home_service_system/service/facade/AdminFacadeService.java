package ir.maktabsharif138.home_service_system.service.facade;

import ir.maktabsharif138.home_service_system.dto.request.*;
import ir.maktabsharif138.home_service_system.dto.response.*;
import ir.maktabsharif138.home_service_system.entity.enums.OrderStatus;
import java.util.List;

public interface AdminFacadeService {

    HomeServiceResponse createService(HomeServiceCreateRequest request);
    HomeServiceResponse updateService(Long id, HomeServiceUpdateRequest request);
    HomeServiceResponse getHomeService(Long id);
    void deleteService(Long id);
    List<HomeServiceResponse> getAllMainServices();
    List<HomeServiceResponse> getSubServicesByParentId(Long parentId);
    void addExpertToSubService(Long expertId, Long subServiceId);
    void removeExpertFromSubService(Long expertId, Long subServiceId);


    List<ExpertResponse> getPendingExperts();
    void approveExpert(Long id);
    void rejectExpert(Long id);


    List<CustomerOrderResponse> getOrdersByStatus(OrderStatus status);
}