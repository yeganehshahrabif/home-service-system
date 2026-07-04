package ir.maktabsharif138.home_service_system.service.facade.impl;

import ir.maktabsharif138.home_service_system.dto.request.*;
import ir.maktabsharif138.home_service_system.dto.response.*;
import ir.maktabsharif138.home_service_system.entity.*;
import ir.maktabsharif138.home_service_system.entity.enums.Role;
import ir.maktabsharif138.home_service_system.entity.enums.SortBy;
import ir.maktabsharif138.home_service_system.service.core.*;
import ir.maktabsharif138.home_service_system.mapper.*;
import ir.maktabsharif138.home_service_system.service.integration.email.VerificationEmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerFacadeServiceImplTest {

    @Mock private CustomerMapper customerMapper;
    @Mock private CustomerOrderMapper customerOrderMapper;
    @Mock private HomeServiceMapper homeServiceMapper;
    @Mock private OfferMapper offerMapper;
    @Mock private ReviewMapper reviewMapper;

    @Mock private ReviewCoreService reviewCoreService;
    @Mock private OfferCoreService offerCoreService;
    @Mock private CustomerCoreService customerCoreService;
    @Mock private HomeServiceCoreService homeServiceCoreService;
    @Mock private CustomerOrderCoreService customerOrderCoreService;
    @Mock private VerificationEmailService verificationEmailService;

    @InjectMocks
    private CustomerFacadeServiceImpl facade;

    private final Long customerId = 1L;
    private final Long orderId = 2L;
    private final Pageable pageable = Pageable.unpaged();

    @Test
    void register_shouldReturnResponse() {

        CustomerRegisterRequest request = new CustomerRegisterRequest();
        Customer customer = new Customer();
        Customer saved = new Customer();
        CustomerResponse response = mock(CustomerResponse.class);

        when(customerMapper.toCustomer(request)).thenReturn(customer);
        when(customerCoreService.register(customer)).thenReturn(saved);
        when(customerMapper.toCustomerResponse(saved)).thenReturn(response);

        CustomerResponse result = facade.register(request);

        assertEquals(response, result);
    }

    @Test
    void register_shouldSendVerificationEmail() {

        CustomerRegisterRequest request = new CustomerRegisterRequest();

        Customer customer = new Customer();
        customer.setEmail("test@gmail.com");

        Customer saved = new Customer();
        saved.setEmail("test@gmail.com");

        CustomerResponse response = mock(CustomerResponse.class);

        when(customerMapper.toCustomer(request))
                .thenReturn(customer);

        when(customerCoreService.register(customer))
                .thenReturn(saved);

        when(customerMapper.toCustomerResponse(saved))
                .thenReturn(response);

        CustomerResponse result = facade.register(request);

        assertEquals(response, result);

        verify(verificationEmailService)
                .sendVerificationEmail(
                        "test@gmail.com",
                        Role.CUSTOMER
                );
    }

//    @Test
//    void login_shouldReturnResponse() {
//
//        CustomerLoginRequest request = new CustomerLoginRequest();
//        Customer customer = new Customer();
//        LoginResponse response = mock(LoginResponse.class);
//
//        when(customerCoreService.login(request.getEmail(), request.getPassword()))
//                .thenReturn(customer);
//
//        when(customerMapper.toLoginResponse(customer))
//                .thenReturn(response);
//
//        LoginResponse result = facade.login(request);
//
//        assertEquals(response, result);
//    }

    @Test
    void updateProfile_shouldNotSendVerificationEmail_WhenEmailNotChanged() {

        CustomerUpdateRequest request = new CustomerUpdateRequest();
        request.setEmail("old@gmail.com");

        Customer customer = new Customer();
        customer.setEmail("old@gmail.com");

        Customer updated = new Customer();

        CustomerResponse response = mock(CustomerResponse.class);
        when(customerCoreService.findById(customerId))
                .thenReturn(customer);
        when(customerCoreService.update(customer))
                .thenReturn(updated);
        when(customerMapper.toCustomerResponse(updated))
                .thenReturn(response);
        CustomerResponse result = facade.updateProfile(request);

        assertEquals(response, result);
        verify(verificationEmailService, never())
                .sendVerificationEmail(any(), any());
    }

    @Test
    void updateProfile_shouldSendVerificationEmail_WhenEmailChanged() {

        CustomerUpdateRequest request = new CustomerUpdateRequest();
        request.setEmail("new@gmail.com");
        Customer customer = new Customer();
        customer.setEmail("old@gmail.com");

        Customer updated = new Customer();
        updated.setEmail("new@gmail.com");

        CustomerResponse response = mock(CustomerResponse.class);

        when(customerCoreService.findById(customerId))
                .thenReturn(customer);

        when(customerCoreService.update(customer))
                .thenReturn(updated);

        when(customerMapper.toCustomerResponse(updated))
                .thenReturn(response);

        CustomerResponse result =
                facade.updateProfile(request);

        assertEquals(response, result);

        assertFalse(customer.isEmailVerified());

        verify(verificationEmailService)
                .sendVerificationEmail(
                        "new@gmail.com",
                        Role.CUSTOMER
                );
    }

    @Test
    void getProfile_shouldReturnResponse() {

        Customer customer = new Customer();
        CustomerResponse response = mock(CustomerResponse.class);

        when(customerCoreService.findById(customerId)).thenReturn(customer);
        when(customerMapper.toCustomerResponse(customer)).thenReturn(response);

        CustomerResponse result = facade.getProfile();

        assertEquals(response, result);
    }

    @Test
    void getAllMainServices_shouldReturnList() {List<HomeService> services = List.of(new HomeService());
        List<HomeServiceResponse> responses = List.of(mock(HomeServiceResponse.class));

        when(homeServiceCoreService.findAllMainServices()).thenReturn(services);
        when(homeServiceMapper.toHomeServiceResponse(services)).thenReturn(responses);

        List<HomeServiceResponse> result = facade.getAllMainServices();

        assertEquals(responses, result);
    }

    @Test
    void getSubServices_shouldReturnList() {

        List<HomeService> services = List.of(new HomeService());
        List<HomeServiceResponse> responses = List.of(mock(HomeServiceResponse.class));

        when(homeServiceCoreService.findSubServicesByParentId(orderId)).thenReturn(services);
        when(homeServiceMapper.toHomeServiceResponse(services)).thenReturn(responses);

        List<HomeServiceResponse> result = facade.getSubServices(orderId);

        assertEquals(responses, result);
    }

    @Test
    void createOrder_shouldReturnResponse() {

        OrderCreateRequest request = new OrderCreateRequest();
        request.setHomeServiceId(10L);

        Customer customer = new Customer();
        HomeService service = new HomeService();
        CustomerOrder order = new CustomerOrder();
        CustomerOrder saved = new CustomerOrder();
        CustomerOrderResponse response = mock(CustomerOrderResponse.class);

        when(customerCoreService.findById(customerId)).thenReturn(customer);
        when(homeServiceCoreService.findById(10L)).thenReturn(service);
        when(customerOrderMapper.toCustomerOrder(request)).thenReturn(order);
        when(customerOrderCoreService.createOrder(order)).thenReturn(saved);
        when(customerOrderMapper.toCustomerOrderResponse(saved)).thenReturn(response);

        CustomerOrderResponse result = facade.createOrder(request);
        verify(customerCoreService).findById(customerId);
        verify(homeServiceCoreService)
                .findById(request.getHomeServiceId());
        assertEquals(response, result);
    }

    @Test
    void getMyOrders_shouldReturnPage() {

        CustomerOrder order = new CustomerOrder();
        Page<CustomerOrder> page = new PageImpl<>(List.of(order));
        Page<CustomerOrderResponse> mapped =
                new PageImpl<>(List.of(mock(CustomerOrderResponse.class)));

        when(customerOrderCoreService.findByCustomerId(customerId, pageable))
                .thenReturn(page);

        when(customerOrderMapper.toCustomerOrderResponse(order))
                .thenReturn(mock(CustomerOrderResponse.class));

        Page<CustomerOrderResponse> result =
                facade.getMyOrders(pageable);

        assertEquals(page.getTotalElements(), result.getTotalElements());
        assertEquals(page.getContent().size(), result.getContent().size());
    }

    @Test
    void startOrder_shouldReturnResponse() {

        CustomerOrder order = new CustomerOrder();
        CustomerOrderResponse response = mock(CustomerOrderResponse.class);

        when(customerOrderCoreService.startOrder(orderId)).thenReturn(order);
        when(customerOrderMapper.toCustomerOrderResponse(order)).thenReturn(response);

        CustomerOrderResponse result = facade.startOrder(orderId);

        assertEquals(response, result);
        verify(customerOrderCoreService).startOrder(orderId);
    }

    @Test
    void completeOrder_shouldReturnResponse() {

        CustomerOrder order = new CustomerOrder();
        CustomerOrderResponse response = mock(CustomerOrderResponse.class);

        when(customerOrderCoreService.completeOrder(orderId)).thenReturn(order);
        when(customerOrderMapper.toCustomerOrderResponse(order)).thenReturn(response);

        CustomerOrderResponse result = facade.completeOrder(orderId);

        assertEquals(response, result);
        verify(customerOrderCoreService).completeOrder(orderId);
    }

    @Test
    void getOrderOffers_shouldReturnPage() {

        Offer offer = new Offer();
        Page<Offer> page = new PageImpl<>(List.of(offer));

        when(customerOrderCoreService.findCustomerOrder(customerId, orderId))
                .thenReturn(new CustomerOrder());

        when(offerCoreService.findByOrderIdSortedByPrice(orderId, pageable))
                .thenReturn(page);

        when(offerMapper.toOfferResponse(offer))
                .thenReturn(mock(OfferResponse.class));

        Page<OfferResponse> result =
                facade.getOrderOffers(orderId, SortBy.PRICE, pageable);

        assertNotNull(result);
    }

    @Test
    void getOrderOffers_shouldSortByRating() {

        Offer offer = new Offer();

        Page<Offer> page =
                new PageImpl<>(List.of(offer));

        when(customerOrderCoreService.findCustomerOrder(
                customerId,
                orderId
        )).thenReturn(new CustomerOrder());

        when(offerCoreService.findByOrderIdSortedByExpertRating(
                orderId,
                pageable
        )).thenReturn(page);

        when(offerMapper.toOfferResponse(offer))
                .thenReturn(mock(OfferResponse.class));

        Page<OfferResponse> result =
                facade.getOrderOffers(
                        orderId,
                        SortBy.RATING,
                        pageable
                );

        assertEquals(1, result.getTotalElements());

        verify(offerCoreService)
                .findByOrderIdSortedByExpertRating(
                        orderId,
                        pageable
                );
    }

    @Test
    void acceptOffer_shouldReturnResponse() {

        Offer offer = new Offer();
        OfferResponse response = mock(OfferResponse.class);

        when(customerOrderCoreService.findCustomerOrder(customerId, orderId))
                .thenReturn(new CustomerOrder());

        when(offerCoreService.acceptOffer(orderId, orderId))
                .thenReturn(offer);

        when(offerMapper.toOfferResponse(offer))
                .thenReturn(response);

        OfferResponse result =
                facade.acceptOffer(orderId, orderId);

        assertEquals(response, result);

        verify(offerCoreService).acceptOffer(orderId, orderId);
        verify(offerMapper).toOfferResponse(offer);
    }

    @Test
    void addReview_shouldReturnResponse() {

        ReviewCreateRequest request = new ReviewCreateRequest();
        request.setOrderId(orderId);

        CustomerOrder order = new CustomerOrder();
        Customer customer = new Customer();
        Offer offer = new Offer();
        Expert expert = new Expert();

        offer.setExpert(expert);
        order.setCustomer(customer);
        order.setAcceptedOffer(offer);

        Review review = new Review();
        Review saved = new Review();
        ReviewResponse response = mock(ReviewResponse.class);

        when(customerOrderCoreService.findCustomerOrder(customerId, orderId))
                .thenReturn(order);

        when(reviewMapper.toReview(request)).thenReturn(review);
        when(reviewCoreService.createReview(review)).thenReturn(saved);
        when(reviewMapper.toResponse(saved)).thenReturn(response);

        ReviewResponse result = facade.addReview(request);

        assertEquals(response, result);
    }

    @Test
    void getOrderHistory_shouldReturnPage() {

        OrderHistoryFilterRequest request =
                new OrderHistoryFilterRequest();

        CustomerOrder order = new CustomerOrder();

        Page<CustomerOrder> page =
                new PageImpl<>(List.of(order));

        CustomerOrderResponse response =
                mock(CustomerOrderResponse.class);

        when(customerOrderCoreService.getOrderHistory(
                customerId,
                request,
                pageable
        )).thenReturn(page);

        when(customerOrderMapper.toCustomerOrderResponse(order))
                .thenReturn(response);

        Page<CustomerOrderResponse> result =
                facade.getOrderHistory(
                        request,
                        pageable
                );

        assertEquals(1, result.getTotalElements());

        verify(customerOrderCoreService)
                .getOrderHistory(
                        customerId,
                        request,
                        pageable
                );
    }
}