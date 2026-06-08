package ir.maktabsharif138.home_service_system.service.core.impl;

import ir.maktabsharif138.home_service_system.entity.Expert;
import ir.maktabsharif138.home_service_system.entity.HomeService;
import ir.maktabsharif138.home_service_system.service.core.ExpertCoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpertCoreServiceImpl implements ExpertCoreService {
    @Override
    public Expert register(Expert expert) {
        return null;
    }

    @Override
    public Expert login(String email, String rawPassword) {
        return null;
    }

    @Override
    public Expert findById(Long id) {
        return null;
    }

    @Override
    public Expert update(Expert expert) {
        return null;
    }

    @Override
    public List<Expert> findPendingExperts() {
        return List.of();
    }

    @Override
    public void approveExpert(Expert expert) {

    }

    @Override
    public void rejectExpert(Long id) {

    }

    @Override
    public void addSubService(Expert expert, HomeService subService) {

    }

    @Override
    public void removeSubService(Expert expert, HomeService subService) {

    }
}
