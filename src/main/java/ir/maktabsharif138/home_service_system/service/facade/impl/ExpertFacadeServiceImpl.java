package ir.maktabsharif138.home_service_system.service.facade.impl;

import ir.maktabsharif138.home_service_system.dto.request.ExpertLoginRequest;
import ir.maktabsharif138.home_service_system.dto.request.ExpertRegisterRequest;
import ir.maktabsharif138.home_service_system.dto.request.ExpertUpdateRequest;
import ir.maktabsharif138.home_service_system.dto.request.OfferCreateRequest;
import ir.maktabsharif138.home_service_system.dto.response.CustomerOrderResponse;
import ir.maktabsharif138.home_service_system.dto.response.ExpertResponse;
import ir.maktabsharif138.home_service_system.dto.response.LoginResponse;
import ir.maktabsharif138.home_service_system.dto.response.OfferResponse;
import ir.maktabsharif138.home_service_system.entity.Expert;
import ir.maktabsharif138.home_service_system.mapper.CustomerOrderMapper;
import ir.maktabsharif138.home_service_system.mapper.ExpertMapper;
import ir.maktabsharif138.home_service_system.service.core.CustomerOrderCoreService;
import ir.maktabsharif138.home_service_system.service.core.ExpertCoreService;
import ir.maktabsharif138.home_service_system.service.facade.ExpertFacadeService;
import ir.maktabsharif138.home_service_system.service.storage.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ExpertFacadeServiceImpl implements ExpertFacadeService {

    private final ExpertCoreService expertCoreService;
    private final FileStorageService fileStorageService;
    private final CustomerOrderCoreService customerOrderCoreService;
    private final CustomerOrderMapper customerOrderMapper;
    private final ExpertMapper expertMapper;

    @Override
    public ExpertResponse register(ExpertRegisterRequest request, MultipartFile image) {
        String imagePath = fileStorageService.saveProfileImage(image);
        Expert expert = expertMapper.toExpert(request);
        expert.setProfileImage(imagePath);
        Expert saved = expertCoreService.register(expert);
        return expertMapper.toExpertResponse(saved);
    }

    @Override
    public LoginResponse login(ExpertLoginRequest request) {
        Expert expert = expertCoreService.login(request.getEmail(), request.getPassword());
        return expertMapper.toLoginResponse(expert);
    }

    @Override
    public ExpertResponse getProfile(Long id) {
       Expert expert = expertCoreService.findById(id);
       return expertMapper.toExpertResponse(expert);
    }

    @Override
    @Transactional
    public ExpertResponse updateProfile(Long id, ExpertUpdateRequest request, MultipartFile image) {
        Expert expert = expertCoreService.findById(id);
        expertCoreService.checkUpdate(expert, request);
        expertMapper.updateExpert(expert, request);

        if (Objects.nonNull(image) && !image.isEmpty()) {
            if (StringUtils.hasText(expert.getProfileImage())) {
                fileStorageService.delete(expert.getProfileImage());
            }
            String imagePath = fileStorageService.saveProfileImage(image);
            expert.setProfileImage(imagePath);
        }
        Expert saved = expertCoreService.update(expert);
        return expertMapper.toExpertResponse(saved);
    }

    @Override
    public OfferResponse createOffer(OfferCreateRequest request) {
        return null;
    }

    @Override
    public List<OfferResponse> getMyOffers(Long expertId) {
        return List.of();
    }

    @Override
    public List<CustomerOrderResponse>
    getAvailableOrdersForExpert(Long expertId) {
        return customerOrderMapper.toOrderResponse(
                customerOrderCoreService.findAvailableOrdersForExpert(expertId)
        );
    }
}
