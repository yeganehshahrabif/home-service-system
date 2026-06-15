package ir.maktabsharif138.home_service_system.service.facade.impl;

import ir.maktabsharif138.home_service_system.dto.request.HomeServiceCreateRequest;
import ir.maktabsharif138.home_service_system.dto.request.HomeServiceUpdateRequest;
import ir.maktabsharif138.home_service_system.dto.request.UserSearchRequest;
import ir.maktabsharif138.home_service_system.dto.response.CustomerOrderResponse;
import ir.maktabsharif138.home_service_system.dto.response.ExpertResponse;
import ir.maktabsharif138.home_service_system.dto.response.HomeServiceResponse;
import ir.maktabsharif138.home_service_system.dto.response.UserSearchResponse;
import ir.maktabsharif138.home_service_system.entity.Customer;
import ir.maktabsharif138.home_service_system.entity.CustomerOrder;
import ir.maktabsharif138.home_service_system.entity.Expert;
import ir.maktabsharif138.home_service_system.entity.HomeService;
import ir.maktabsharif138.home_service_system.entity.enums.OrderStatus;
import ir.maktabsharif138.home_service_system.mapper.CustomerMapper;
import ir.maktabsharif138.home_service_system.mapper.CustomerOrderMapper;
import ir.maktabsharif138.home_service_system.mapper.ExpertMapper;
import ir.maktabsharif138.home_service_system.mapper.HomeServiceMapper;
import ir.maktabsharif138.home_service_system.service.core.CustomerOrderCoreService;
import ir.maktabsharif138.home_service_system.service.core.ExpertCoreService;
import ir.maktabsharif138.home_service_system.service.core.HomeServiceCoreService;
import ir.maktabsharif138.home_service_system.service.core.UserSearchCoreService;
import ir.maktabsharif138.home_service_system.service.facade.AdminFacadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AdminFacadeServiceImpl implements AdminFacadeService {

    private final HomeServiceMapper homeServiceMapper;
    private final ExpertMapper expertMapper;
    private final CustomerOrderMapper customerOrderMapper;
    private final CustomerMapper customerMapper;
    private final UserSearchCoreService userSearchCoreService;
    private final ExpertCoreService expertCoreService;
    private final HomeServiceCoreService homeServiceCoreService;
    private final CustomerOrderCoreService customerOrderCoreService;

    @Override
    @Transactional
    public HomeServiceResponse createService(HomeServiceCreateRequest request) {
        HomeService service = homeServiceMapper.toHomeService(request);
        if (Objects.nonNull(request.getParentServiceId())) {
            HomeService parent = homeServiceCoreService.findById(request.getParentServiceId());
            service.setParentService(parent);
        }
        HomeService saved = homeServiceCoreService.create(service);
        return homeServiceMapper.toHomeServiceResponse(saved);
    }

    @Override
    @Transactional
    public HomeServiceResponse updateService(Long id, HomeServiceUpdateRequest request) {

        HomeService existing = homeServiceCoreService.findById(id);
        homeServiceCoreService.checkUpdate(existing, request);
        if (Objects.nonNull(request.getParentServiceId())) {
            HomeService parent = homeServiceCoreService.findById(request.getParentServiceId());
            existing.setParentService(parent);
        }

        homeServiceMapper.updateHomeService(existing, request);
        HomeService saved = homeServiceCoreService.update(existing);
        return homeServiceMapper.toHomeServiceResponse(saved);
    }

    @Override
    public HomeServiceResponse getHomeService(Long id) {
        return homeServiceMapper.toHomeServiceResponse(homeServiceCoreService.findById(id));
    }

    @Override
    @Transactional
    public void deleteService(Long id) {
        homeServiceCoreService.delete(id);
    }

    @Override
    public List<HomeServiceResponse> getAllMainServices() {
        return homeServiceMapper.toHomeServiceResponse(homeServiceCoreService.findAllMainServices());
    }

    @Override
    public List<HomeServiceResponse> getSubServicesByParentId(Long parentId) {
        return homeServiceMapper.toHomeServiceResponse(homeServiceCoreService
                .findSubServicesByParentId(parentId));
    }


    @Override
    @Transactional
    public void addExpertToSubService(Long expertId, Long subServiceId) {
        homeServiceCoreService.addExpertToSubService(expertId, subServiceId);
    }

    @Override
    @Transactional
    public void removeExpertFromSubService(Long expertId, Long subServiceId) {
        homeServiceCoreService.removeExpertFromSubService(expertId, subServiceId);
    }

    @Override
    public Page<ExpertResponse> getPendingExperts(Pageable pageable) {

        Page<Expert> pendingExperts = expertCoreService.findPendingExperts(pageable);

        return pendingExperts.map(expertMapper::toExpertResponse);

    }

    @Override
    @Transactional
    public void approveExpert(Long id) {

        expertCoreService.approveExpert(id);
    }

    @Override
    @Transactional
    public void rejectExpert(Long id) {

        expertCoreService.rejectExpert(id);
    }

    @Override
    @Transactional
    public Page<CustomerOrderResponse> getOrdersByStatus(OrderStatus status, Pageable pageable) {
        Page<CustomerOrder> customerOrders = customerOrderCoreService.findByStatus(status, pageable);
        return customerOrders.map(customerOrderMapper::toCustomerOrderResponse);
    }

    @Override
    public Page<UserSearchResponse> searchUsers(
            UserSearchRequest request,
            Pageable pageable
    ) {

        return userSearchCoreService.search(request, pageable)
                .map(user -> {

                    if (user instanceof Expert expert) {
                        return expertMapper.toSearchResponse(expert);
                    }

                    return customerMapper.toSearchResponse(
                            (Customer) user
                    );
                });
    }
}
