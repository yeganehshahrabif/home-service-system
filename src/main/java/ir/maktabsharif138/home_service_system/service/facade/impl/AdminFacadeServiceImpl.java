package ir.maktabsharif138.home_service_system.service.facade.impl;

import ir.maktabsharif138.home_service_system.dto.request.HomeServiceCreateRequest;
import ir.maktabsharif138.home_service_system.dto.request.HomeServiceUpdateRequest;
import ir.maktabsharif138.home_service_system.dto.response.CustomerOrderResponse;
import ir.maktabsharif138.home_service_system.dto.response.ExpertResponse;
import ir.maktabsharif138.home_service_system.dto.response.HomeServiceResponse;
import ir.maktabsharif138.home_service_system.entity.HomeService;
import ir.maktabsharif138.home_service_system.entity.enums.OrderStatus;
import ir.maktabsharif138.home_service_system.mapper.HomeServiceMapper;
import ir.maktabsharif138.home_service_system.service.core.HomeServiceCoreService;
import ir.maktabsharif138.home_service_system.service.facade.AdminFacadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminFacadeServiceImpl implements AdminFacadeService {

    private final HomeServiceMapper mapper;
    private final HomeServiceCoreService homeServiceCoreService;

    @Override
    @Transactional
    public HomeServiceResponse createService(HomeServiceCreateRequest request) {
        HomeService service = mapper.toHomeService(request);
        if (request.getParentServiceId() != null) {
            HomeService parent = homeServiceCoreService.findById(request.getParentServiceId());
            service.setParentService(parent);
        }
        HomeService saved = homeServiceCoreService.create(service);
        return mapper.toHomeServiceResponse(saved);
    }

    @Override
    @Transactional
    public HomeServiceResponse updateService(Long id, HomeServiceUpdateRequest request) {

        HomeService existing = homeServiceCoreService.findById(id);
        homeServiceCoreService.checkUpdate(existing, request);
        if (request.getParentServiceId() != null) {
            HomeService parent = homeServiceCoreService.findById(request.getParentServiceId());
            existing.setParentService(parent);
        }

        mapper.updateHomeService(existing, request);
        HomeService saved = homeServiceCoreService.update(existing);
        return mapper.toHomeServiceResponse(saved);
    }

    @Override
    public HomeServiceResponse getHomeService(Long id) {
        return mapper.toHomeServiceResponse(homeServiceCoreService.findById(id));
    }

    @Override
    @Transactional
    public void deleteService(Long id) {
        homeServiceCoreService.delete(id);
    }

    @Override
    public List<HomeServiceResponse> getAllMainServices() {
        return mapper.toHomeServiceResponse(homeServiceCoreService.findAllMainServices());
    }

    @Override
    public List<HomeServiceResponse> getSubServicesByParentId(Long parentId) {
        return mapper.toHomeServiceResponse(homeServiceCoreService
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
    public List<ExpertResponse> getPendingExperts() {
        return List.of();
    }

    @Override
    public void approveExpert(Long id) {

    }

    @Override
    public void rejectExpert(Long id) {

    }

    @Override
    public List<CustomerOrderResponse> getOrdersByStatus(OrderStatus status) {
        return List.of();
    }
}
