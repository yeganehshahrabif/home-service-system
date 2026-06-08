package ir.maktabsharif138.home_service_system.service;

import ir.maktabsharif138.home_service_system.dto.response.ExpertResponse;

import java.util.List;

public interface AdminService {

    List<ExpertResponse> getPendingExperts();
    void approveExpert(Long id);
    void rejectExpert(Long id);
    void addExpertToSubService(Long expertId, Long subServiceId);
    void removeExpertFromSubService(Long expertId, Long subServiceId);

}
