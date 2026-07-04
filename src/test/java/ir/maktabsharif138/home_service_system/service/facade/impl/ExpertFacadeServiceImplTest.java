package ir.maktabsharif138.home_service_system.service.facade.impl;

import ir.maktabsharif138.home_service_system.dto.request.*;
import ir.maktabsharif138.home_service_system.dto.response.*;
import ir.maktabsharif138.home_service_system.entity.*;
import ir.maktabsharif138.home_service_system.entity.enums.Role;
import ir.maktabsharif138.home_service_system.mapper.*;
import ir.maktabsharif138.home_service_system.service.core.*;
import ir.maktabsharif138.home_service_system.service.integration.email.VerificationEmailService;
import ir.maktabsharif138.home_service_system.service.storage.FileStorageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpertFacadeServiceImplTest {

    @Mock private ExpertCoreService expertCoreService;
    @Mock private FileStorageService fileStorageService;
    @Mock private CustomerOrderCoreService customerOrderCoreService;
    @Mock private OfferCoreService offerCoreService;
    @Mock private VerificationEmailService verificationEmailService;
    @Mock private CustomerOrderMapper customerOrderMapper;
    @Mock private ExpertMapper expertMapper;
    @Mock private OfferMapper offerMapper;
    @Mock private ReviewCoreService reviewCoreService;
    @Mock private ReviewMapper reviewMapper;

    @InjectMocks
    private ExpertFacadeServiceImpl facade;

    private final Long expertId = 1L;
    private final Long orderId = 2L;
    private final Pageable pageable = Pageable.unpaged();

    @Test
    void register_shouldReturnResponse_whenImageExists() {

        ExpertRegisterRequest request = new ExpertRegisterRequest();

        MultipartFile image =
                new MockMultipartFile(
                        "img",
                        "a.jpg",
                        "image/jpg",
                        new byte[]{1}
                );

        Expert expert = new Expert();

        Expert saved = new Expert();
        saved.setEmail("test@gmail.com");

        ExpertResponse response = mock(ExpertResponse.class);

        when(expertMapper.toExpert(request))
                .thenReturn(expert);

        when(fileStorageService.saveProfileImage(image))
                .thenReturn("path.jpg");

        when(expertCoreService.register(expert))
                .thenReturn(saved);

        when(expertMapper.toExpertResponse(saved))
                .thenReturn(response);

        ExpertResponse result =
                facade.register(request, image);

        assertEquals(response, result);

        verify(fileStorageService)
                .saveProfileImage(image);

        verify(verificationEmailService)
                .sendVerificationEmail(
                        "test@gmail.com",
                        Role.EXPERT
                );
    }

    @Test
    void register_shouldWork_whenNoImage() {

        ExpertRegisterRequest request =
                new ExpertRegisterRequest();

        Expert expert = new Expert();

        Expert saved = new Expert();
        saved.setEmail("test@gmail.com");

        ExpertResponse response =
                mock(ExpertResponse.class);

        when(expertMapper.toExpert(request))
                .thenReturn(expert);

        when(expertCoreService.register(expert))
                .thenReturn(saved);

        when(expertMapper.toExpertResponse(saved))
                .thenReturn(response);

        ExpertResponse result =
                facade.register(request, null);

        assertEquals(response, result);

        verify(fileStorageService, never())
                .saveProfileImage(any());

        verify(verificationEmailService)
                .sendVerificationEmail(
                        "test@gmail.com",
                        Role.EXPERT
                );
    }

//    @Test
//    void login_shouldReturnResponse() {
//
//        ExpertLoginRequest request = new ExpertLoginRequest();
//        request.setEmail("a@a.com");
//        request.setPassword("123");
//
//        Expert expert = new Expert();
//        LoginResponse response = mock(LoginResponse.class);
//
//        when(expertCoreService.login("a@a.com", "123")).thenReturn(expert);
//        when(expertMapper.toLoginResponse(expert)).thenReturn(response);
//
//        LoginResponse result = facade.login(request);
//
//        assertEquals(response, result);
//    }

    @Test
    void getProfile_shouldReturnResponse() {

        Expert expert = new Expert();
        ExpertResponse response = mock(ExpertResponse.class);

        when(expertCoreService.findById(expertId)).thenReturn(expert);
        when(expertMapper.toExpertResponse(expert)).thenReturn(response);

        ExpertResponse result = facade.getProfile();

        assertEquals(response, result);
    }

    @Test
    void updateProfile_shouldUpdateImageSuccessfully() {

        ExpertUpdateRequest request =
                new ExpertUpdateRequest();

        MultipartFile image =
                new MockMultipartFile(
                        "img",
                        "a.jpg",
                        "image/jpg",
                        new byte[]{1}
                );

        Expert expert = new Expert();
        expert.setProfileImage("old.jpg");
        expert.setEmail("old@gmail.com");

        Expert saved = new Expert();
        saved.setEmail("old@gmail.com");

        ExpertResponse response =
                mock(ExpertResponse.class);

        when(expertCoreService.findById(expertId))
                .thenReturn(expert);

        when(fileStorageService.saveProfileImage(image))
                .thenReturn("new.jpg");

        when(expertCoreService.update(expert))
                .thenReturn(saved);

        when(expertMapper.toExpertResponse(saved))
                .thenReturn(response);

        ExpertResponse result =
                facade.updateProfile(
                        request,
                        image
                );

        assertEquals(response, result);

        verify(expertCoreService)
                .checkUpdate(
                        expert,
                        request,
                        true
                );

        verify(fileStorageService)
                .delete("old.jpg");
    }
    @Test
    void updateProfile_shouldUpdateWithoutImage() {

        ExpertUpdateRequest request =
                new ExpertUpdateRequest();

        Expert expert = new Expert();
        expert.setEmail("old@gmail.com");

        Expert saved = new Expert();
        saved.setEmail("old@gmail.com");

        ExpertResponse response =
                mock(ExpertResponse.class);

        when(expertCoreService.findById(expertId))
                .thenReturn(expert);

        when(expertCoreService.update(expert))
                .thenReturn(saved);

        when(expertMapper.toExpertResponse(saved))
                .thenReturn(response);

        ExpertResponse result =
                facade.updateProfile(
                        request,
                        null
                );

        assertEquals(response, result);

        verify(expertCoreService)
                .checkUpdate(
                        expert,
                        request,
                        false
                );

        verify(fileStorageService, never())
                .saveProfileImage(any());

        verify(fileStorageService, never())
                .delete(anyString());
    }

    @Test
    void updateProfile_shouldSendVerificationEmail_whenEmailChanged() {

        ExpertUpdateRequest request =
                new ExpertUpdateRequest();

        request.setEmail("new@gmail.com");

        Expert expert = new Expert();
        expert.setEmail("old@gmail.com");

        Expert saved = new Expert();
        saved.setEmail("new@gmail.com");

        ExpertResponse response =
                mock(ExpertResponse.class);

        when(expertCoreService.findById(expertId))
                .thenReturn(expert);

        when(expertCoreService.update(expert))
                .thenReturn(saved);

        when(expertMapper.toExpertResponse(saved))
                .thenReturn(response);

        facade.updateProfile(
                request,
                null
        );

        verify(verificationEmailService)
                .sendVerificationEmail(
                        "new@gmail.com",
                        Role.EXPERT
                );
    }

    @Test
    void updateProfile_shouldDeleteNewImage_whenUpdateFails() {

        ExpertUpdateRequest request =
                new ExpertUpdateRequest();

        MultipartFile image =
                new MockMultipartFile(
                        "img",
                        "a.jpg",
                        "image/jpg",
                        new byte[]{1}
                );

        Expert expert = new Expert();

        when(expertCoreService.findById(expertId))
                .thenReturn(expert);

        when(fileStorageService.saveProfileImage(image))
                .thenReturn("new.jpg");

        when(expertCoreService.update(expert))
                .thenThrow(
                        new RuntimeException("DB error")
                );

        assertThrows(
                RuntimeException.class,
                () -> facade.updateProfile(
                        request,
                        image
                )
        );

        verify(fileStorageService)
                .delete("new.jpg");
    }

    @Test
    void createOffer_shouldReturnResponse() {

        OfferCreateRequest request = new OfferCreateRequest();
        request.setOrderId(orderId);

        Expert expert = new Expert();
        CustomerOrder order = new CustomerOrder();
        Offer offer = new Offer();
        Offer saved = new Offer();
        OfferResponse response = mock(OfferResponse.class);

        when(expertCoreService.findById(expertId)).thenReturn(expert);
        when(customerOrderCoreService.findById(orderId)).thenReturn(order);
        when(offerMapper.toOffer(request)).thenReturn(offer);
        when(offerCoreService.createOffer(offer)).thenReturn(saved);
        when(offerMapper.toOfferResponse(saved)).thenReturn(response);

        OfferResponse result = facade.createOffer(request);

        assertEquals(response, result);
    }

    @Test
    void getMyOffers_shouldReturnPage() {

        Offer offer = new Offer();
        OfferResponse response = mock(OfferResponse.class);

        Page<Offer> page = new PageImpl<>(List.of(offer));

        when(offerCoreService.findByExpertId(expertId, pageable)).thenReturn(page);
        when(offerMapper.toOfferResponse(offer)).thenReturn(response);

        Page<OfferResponse> result = facade.getMyOffers(pageable);

        assertEquals(1, result.getContent().size());
        assertEquals(response, result.getContent().get(0));
    }

    @Test
    void getAvailableOrders_shouldReturnPage() {

        CustomerOrder order = new CustomerOrder();
        CustomerOrderResponse response = mock(CustomerOrderResponse.class);

        Page<CustomerOrder> page = new PageImpl<>(List.of(order));

        when(customerOrderCoreService.findAvailableOrdersForExpert(expertId, pageable))
                .thenReturn(page);

        when(customerOrderMapper.toCustomerOrderResponse(order))
                .thenReturn(response);

        Page<CustomerOrderResponse> result =
                facade.getAvailableOrdersForExpert(pageable);

        assertEquals(1, result.getContent().size());
        assertEquals(response, result.getContent().get(0));
    }

    @Test
    void findOrderHistory_shouldReturnPage() {

        CustomerOrder order = new CustomerOrder();
        ExpertOrderHistoryResponse response = mock(ExpertOrderHistoryResponse.class);

        Page<CustomerOrder> page = new PageImpl<>(List.of(order));

        when(customerOrderCoreService.findOrderHistory(expertId, pageable))
                .thenReturn(page);

        when(customerOrderMapper.toExpertOrderHistoryResponse(order))
                .thenReturn(response);

        Page<ExpertOrderHistoryResponse> result =
                facade.findOrderHistory(pageable);

        assertEquals(1, result.getContent().size());
    }

    @Test
    void getOrderRating_shouldReturnResponse() {

        Review review = new Review();
        ExpertOrderRatingResponse response = mock(ExpertOrderRatingResponse.class);

        when(reviewCoreService.findExpertOrderReview(expertId, orderId))
                .thenReturn(review);

        when(reviewMapper.toExpertOrderRatingResponse(review))
                .thenReturn(response);

        ExpertOrderRatingResponse result =
                facade.getOrderRating(orderId);

        assertEquals(response, result);
    }
}