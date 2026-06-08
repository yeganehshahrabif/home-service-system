package ir.maktabsharif138.home_service_system.service.core.impl;

import ir.maktabsharif138.home_service_system.entity.HomeService;
import ir.maktabsharif138.home_service_system.service.core.HomeServiceCoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeServiceCoreServiceImpl implements HomeServiceCoreService {
    @Override
    public HomeService create(HomeService service) {
        return null;
    }

    @Override
    public HomeService update(HomeService service) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public HomeService findById(Long id) {
        return null;
    }

    @Override
    public List<HomeService> findAllMainServices() {
        return List.of();
    }

    @Override
    public List<HomeService> findSubServicesByParentId(Long parentId) {
        return List.of();
    }
}
