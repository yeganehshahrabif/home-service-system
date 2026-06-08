package ir.maktabsharif138.home_service_system.service;
import ir.maktabsharif138.home_service_system.dto.request.ExpertLoginRequest;
import ir.maktabsharif138.home_service_system.dto.request.ExpertRegisterRequest;
import ir.maktabsharif138.home_service_system.dto.request.ExpertUpdateRequest;
import ir.maktabsharif138.home_service_system.dto.response.ExpertResponse;
import ir.maktabsharif138.home_service_system.dto.response.LoginResponse

import java.util.List;

public interface ExpertService {

    ExpertResponse register(ExpertRegisterRequest request);
    LoginResponse login(ExpertLoginRequest request);
    ExpertResponse findById(Long id);
    ExpertResponse updateProfile(Long id, ExpertUpdateRequest request);


    List<ExpertResponse> getPendingExperts();
    void approveExpert(Long id);
    void rejectExpert(Long id);
    void addExpertToSubService(Long expertId, Long subServiceId);
    void removeExpertFromSubService(Long expertId, Long subServiceId);
}
