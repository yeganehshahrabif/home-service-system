package ir.maktabsharif138.home_service_system.service.core;

import ir.maktabsharif138.home_service_system.dto.request.ExpertRegisterRequest;
import ir.maktabsharif138.home_service_system.dto.request.ExpertUpdateRequest;
import ir.maktabsharif138.home_service_system.entity.Expert;
import ir.maktabsharif138.home_service_system.entity.HomeService;
import java.util.List;

public interface ExpertCoreService {

    Expert register(Expert expert);
    Expert login(String email, String rawPassword);
    void checkUpdate(Expert expert, ExpertUpdateRequest request);
    Expert update(Expert expert);
    Expert findById(Long id);
    boolean existsByEmail(String email);
    List<Expert> findPendingExperts();
    void approveExpert(Long id);
    void rejectExpert(Long id);
}