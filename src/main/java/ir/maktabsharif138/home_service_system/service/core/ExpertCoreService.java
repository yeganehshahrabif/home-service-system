package ir.maktabsharif138.home_service_system.service.core;

import ir.maktabsharif138.home_service_system.entity.Expert;
import ir.maktabsharif138.home_service_system.entity.HomeService;
import java.util.List;

public interface ExpertCoreService {

    Expert register(Expert expert);
    Expert login(String email, String rawPassword);
    Expert findById(Long id);
    Expert update(Expert expert); // در داخل پیاده‌سازی، چک می‌شود که متخصص کار فعال نداشته باشد
    List<Expert> findPendingExperts(); // وضعیت NEW یا PENDING_APPROVAL
    void approveExpert(Expert expert);
    void rejectExpert(Long id);
    void addSubService(Expert expert, HomeService subService);
    void removeSubService(Expert expert, HomeService subService);
}