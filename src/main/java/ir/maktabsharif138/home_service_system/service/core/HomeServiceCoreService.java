package ir.maktabsharif138.home_service_system.service.core;

import ir.maktabsharif138.home_service_system.entity.HomeService;
import java.util.List;

public interface HomeServiceCoreService {

    HomeService create(HomeService service);
    HomeService update(HomeService service);
    void delete(Long id);
    HomeService findById(Long id);
    void addExpertToSubService(Long expertId, Long subServiceId);
    void removeExpertFromSubService(Long expertId, Long subServiceId);
    List<HomeService> findAllMainServices();
    List<HomeService> findSubServicesByParentId(Long parentId);
}