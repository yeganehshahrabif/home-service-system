package ir.maktabsharif138.home_service_system.service.facade.impl;
import ir.maktabsharif138.home_service_system.dto.request.ExpertLoginRequest;
import ir.maktabsharif138.home_service_system.dto.request.ExpertRegisterRequest;
import ir.maktabsharif138.home_service_system.dto.request.ExpertUpdateRequest;
import ir.maktabsharif138.home_service_system.dto.request.OfferCreateRequest;
import ir.maktabsharif138.home_service_system.dto.response.*;
import ir.maktabsharif138.home_service_system.entity.CustomerOrder;
import ir.maktabsharif138.home_service_system.entity.Expert;
import ir.maktabsharif138.home_service_system.entity.Offer;
import ir.maktabsharif138.home_service_system.mapper.CustomerOrderMapper;
import ir.maktabsharif138.home_service_system.mapper.ExpertMapper;
import ir.maktabsharif138.home_service_system.mapper.OfferMapper;
import ir.maktabsharif138.home_service_system.service.core.CustomerOrderCoreService;
import ir.maktabsharif138.home_service_system.service.core.ExpertCoreService;
import ir.maktabsharif138.home_service_system.service.core.OfferCoreService;
import ir.maktabsharif138.home_service_system.service.facade.ExpertFacadeService;
import ir.maktabsharif138.home_service_system.service.storage.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpertFacadeServiceImpl implements ExpertFacadeService {

    private final ExpertCoreService expertCoreService;
    private final FileStorageService fileStorageService;
    private final CustomerOrderCoreService customerOrderCoreService;
    private final OfferCoreService offerCoreService;
    private final CustomerOrderMapper customerOrderMapper;
    private final ExpertMapper expertMapper;
    private final OfferMapper offerMapper;

    @Override
    public ExpertResponse register(ExpertRegisterRequest request, MultipartFile image) {

        Expert expert = expertMapper.toExpert(request);
        String imagePath = null;
        try {
            if (image != null && !image.isEmpty()) {
                imagePath = fileStorageService.saveProfileImage(image);
                expert.setProfileImage(imagePath);
            }

            Expert saved = expertCoreService.register(expert);
            return expertMapper.toExpertResponse(saved);
        } catch (RuntimeException ex) {
            if (StringUtils.hasText(imagePath)) {
                fileStorageService.delete(imagePath);
            }
            throw ex;
        }
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

//    @Override
//    public ExpertResponse updateProfile(Long id, ExpertUpdateRequest request, MultipartFile image) {
//        Expert expert = expertCoreService.findById(id);
//        expertCoreService.checkUpdate(expert, request);
//        expertMapper.updateExpert(expert, request);
//
//        if (Objects.nonNull(image) && !image.isEmpty()) {
//            if (StringUtils.hasText(expert.getProfileImage())) {
//                fileStorageService.delete(expert.getProfileImage());
//            }
//            String imagePath = fileStorageService.saveProfileImage(image);
//            expert.setProfileImage(imagePath);
//        }
//        Expert saved = expertCoreService.update(expert);
//        return expertMapper.toExpertResponse(saved);
//    }

    @Override
    public ExpertResponse updateProfile(Long id, ExpertUpdateRequest request, MultipartFile image) {

        Expert expert = expertCoreService.findById(id);
        boolean hasImage = image != null && !image.isEmpty();
        expertCoreService.checkUpdate(expert, request, hasImage);
        expertMapper.updateExpert(expert, request);
        String oldImage = expert.getProfileImage();
        String newImage = null;
        try {
            if (hasImage) {
                newImage = fileStorageService.saveProfileImage(image);
                expert.setProfileImage(newImage);
            }
            Expert saved = expertCoreService.update(expert);
            if (hasImage && StringUtils.hasText(oldImage)) {
                fileStorageService.delete(oldImage);
            }
            return expertMapper.toExpertResponse(saved);
        } catch (RuntimeException ex) {
            if (StringUtils.hasText(newImage)) {
                fileStorageService.delete(newImage);
            }
            throw ex;
        }
    }

    @Override
    @Transactional
    public OfferResponse createOffer(Long expertId, OfferCreateRequest request) {
        Expert expert = expertCoreService.findById(expertId);
        CustomerOrder order = customerOrderCoreService.findById(request.getOrderId());
        Offer offer = offerMapper.toOffer(request);
        offer.setExpert(expert);
        offer.setCustomerOrder(order);
        Offer saved = offerCoreService.createOffer(offer);
        return offerMapper.toOfferResponse(saved);
    }

    @Override
    public Page<OfferResponse> getMyOffers(Long expertId, Pageable pageable) {

        Page<Offer> offers = offerCoreService.findByExpertId(expertId, pageable);
        return offers.map(offerMapper::toOfferResponse);
    }

    @Override
    public Page<CustomerOrderResponse>
    getAvailableOrdersForExpert(Long expertId, Pageable pageable) {

        Page<CustomerOrder> customerOrders = customerOrderCoreService.
                findAvailableOrdersForExpert(expertId, pageable);
        return customerOrders.map(customerOrderMapper::toCustomerOrderResponse);

    }

    @Override
    public Page<ExpertOrderHistoryResponse> findOrderHistory
            (Long expertId, Pageable pageable) {

        Page<CustomerOrder> orders = customerOrderCoreService.findOrderHistory(expertId, pageable);

        return orders.map(customerOrderMapper::toExpertOrderHistoryResponse);
    }
}
