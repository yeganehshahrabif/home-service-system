package ir.maktabsharif138.home_service_system.service.core;

import ir.maktabsharif138.home_service_system.dto.request.ExpertRegisterRequest;
import ir.maktabsharif138.home_service_system.dto.request.ExpertUpdateRequest;
import ir.maktabsharif138.home_service_system.entity.CustomerOrder;
import ir.maktabsharif138.home_service_system.entity.Expert;
import ir.maktabsharif138.home_service_system.entity.HomeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ExpertCoreService {

    Expert register(Expert expert);
//    Expert login(String email, String rawPassword);
    void checkUpdate(Expert expert, ExpertUpdateRequest request, boolean hasImage);
    Expert update(Expert expert);
    Expert findById(Long id);
    boolean existsByEmail(String email);
    Page<Expert> findPendingExperts(Pageable pageable);
    void approveExpert(Long id);
    void rejectExpert(Long id);
    void applyDelayPenalty(CustomerOrder order);
    void recalculateRating(Expert expert);
    Expert findByEmail(String email);
    void verifyEmail(Expert expert);
}