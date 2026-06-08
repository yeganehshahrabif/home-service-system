package ir.maktabsharif138.home_service_system.service;
import ir.maktabsharif138.home_service_system.dto.request.HomeServiceCreateRequest;
import ir.maktabsharif138.home_service_system.dto.request.HomeServiceUpdateRequest;
import ir.maktabsharif138.home_service_system.dto.response.HomeServiceResponse;

import java.util.List;

public interface HomeServiceService {

    HomeServiceResponse create(HomeServiceCreateRequest request);
    HomeServiceResponse update(Long id, HomeServiceUpdateRequest request);
    void delete(Long id);
    List<HomeServiceResponse> getAllMainServices(); // بدون والد
    List<HomeServiceResponse> getSubServicesByParentId(Long parentId);
}
