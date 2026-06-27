package ir.maktabsharif138.home_service_system.service.facade;

import ir.maktabsharif138.home_service_system.dto.request.*;
import ir.maktabsharif138.home_service_system.dto.response.*;
import ir.maktabsharif138.home_service_system.entity.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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


    Page<ExpertResponse> getPendingExperts(Pageable pageable);
    void approveExpert(Long id);
    void rejectExpert(Long id);


    Page<CustomerOrderResponse> getOrdersByStatus(OrderStatus status, Pageable pageable);

    Page<UserSearchResponse> searchUsers(
            UserSearchRequest request,
            Pageable pageable
    );

    Page<OrderHistorySummaryResponse> getCustomerHistory(
            Long customerId,
            AdminHistoryFilterRequest request,
            Pageable pageable
    );

    Page<OrderHistorySummaryResponse> getExpertHistory(
            Long expertId,
            AdminHistoryFilterRequest request,
            Pageable pageable
    );

    OrderHistoryDetailsResponse getCustomerHistoryDetails(
            Long customerId,
            Long orderId
    );

    OrderHistoryDetailsResponse getExpertHistoryDetails(
            Long expertId,
            Long orderId
    );
}