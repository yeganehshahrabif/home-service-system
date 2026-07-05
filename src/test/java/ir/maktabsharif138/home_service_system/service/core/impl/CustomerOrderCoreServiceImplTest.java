package ir.maktabsharif138.home_service_system.service.core.impl;

import ir.maktabsharif138.home_service_system.dto.request.AdminHistoryFilterRequest;
import org.springframework.data.jpa.domain.Specification;
import ir.maktabsharif138.home_service_system.dto.request.OrderHistoryFilterRequest;
import ir.maktabsharif138.home_service_system.entity.*;
import ir.maktabsharif138.home_service_system.entity.enums.OrderPaymentStatus;
import ir.maktabsharif138.home_service_system.entity.enums.OrderStatus;
import ir.maktabsharif138.home_service_system.exception.BadRequestException;
import ir.maktabsharif138.home_service_system.exception.NotFoundException;
import ir.maktabsharif138.home_service_system.repository.CustomerOrderRepository;
import ir.maktabsharif138.home_service_system.repository.ExpertRepository;
import ir.maktabsharif138.home_service_system.repository.ReviewRepository;
import ir.maktabsharif138.home_service_system.service.core.ExpertCoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerOrderCoreServiceImplTest {

    @Mock
    private CustomerOrderRepository orderRepository;

    @Mock
    private ExpertCoreService expertCoreService;

    @InjectMocks
    private CustomerOrderCoreServiceImpl service;

    private CustomerOrder order;
    private Customer customer;
    private Expert expert;
    private Offer offer;
    private HomeService homeService;

    @BeforeEach
    void setUp() {

        customer = new Customer();
        customer.setId(1L);

        expert = new Expert();
        expert.setId(10L);

        offer = new Offer();
        offer.setId(20L);
        offer.setExpert(expert);
        offer.setProposedStartTime(LocalDateTime.now().minusHours(1));

        HomeService parent = new HomeService();

        homeService = new HomeService();
        homeService.setId(100L);
        homeService.setBasePrice(BigDecimal.valueOf(50));
        homeService.setParentService(parent);

        order = new CustomerOrder();
        order.setId(100L);
        order.setCustomer(customer);
        order.setAcceptedOffer(offer);
        order.setHomeService(homeService);
        order.setProposedPrice(BigDecimal.valueOf(100));
        order.setFinalPrice(BigDecimal.valueOf(100));
        order.setStartDateTime(LocalDateTime.now().plusDays(1));
    }

    @Test
    void createOrder_shouldSaveSuccessfully() {

        when(orderRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        CustomerOrder result = service.createOrder(order);

        assertEquals(OrderStatus.WAITING_FOR_OFFERS,
                result.getOrderStatus());

        verify(orderRepository).save(order);
    }

    @Test
    void createOrder_shouldThrow_whenPriceLessThanBase() {

        order.setProposedPrice(BigDecimal.valueOf(10));

        assertThrows(BadRequestException.class,
                () -> service.createOrder(order));
    }

    @Test
    void createOrder_shouldThrow_whenDateInPast() {

        order.setStartDateTime(LocalDateTime.now().minusDays(1));

        assertThrows(BadRequestException.class,
                () -> service.createOrder(order));
    }

    @Test
    void createOrder_shouldThrow_whenParentServiceNull() {

        homeService.setParentService(null);

        assertThrows(BadRequestException.class,
                () -> service.createOrder(order));
    }

    @Test
    void findById_shouldReturnOrder() {

        when(orderRepository.findById(100L))
                .thenReturn(Optional.of(order));

        CustomerOrder result = service.findById(100L);

        assertNotNull(result);
    }

    @Test
    void findById_shouldThrow_whenNotFound() {

        when(orderRepository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.findById(100L));
    }

    @Test
    void findByCustomerId_shouldReturnPage() {

        Pageable pageable = PageRequest.of(0, 10);

        when(orderRepository.findByCustomerId(1L, pageable))
                .thenReturn(Page.empty());

        Page<CustomerOrder> result =
                service.findByCustomerId(1L, pageable);

        assertNotNull(result);

        verify(orderRepository)
                .findByCustomerId(1L, pageable);
    }

    @Test
    void startOrder_shouldStartSuccessfully() {

        order.setOrderStatus(OrderStatus.WAITING_FOR_EXPERT);

        when(orderRepository.findById(100L))
                .thenReturn(Optional.of(order));

        when(orderRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        CustomerOrder result = service.startOrder(100L);

        assertEquals(OrderStatus.STARTED,
                result.getOrderStatus());

        assertNotNull(result.getActualStartTime());
    }

    @Test
    void startOrder_shouldThrow_whenWrongStatus() {

        order.setOrderStatus(OrderStatus.WAITING_FOR_OFFERS);

        when(orderRepository.findById(100L))
                .thenReturn(Optional.of(order));

        assertThrows(BadRequestException.class,
                () -> service.startOrder(100L));
    }

    @Test
    void startOrder_shouldThrow_whenNoAcceptedOffer() {

        order.setOrderStatus(OrderStatus.WAITING_FOR_EXPERT);
        order.setAcceptedOffer(null);

        when(orderRepository.findById(100L))
                .thenReturn(Optional.of(order));

        assertThrows(BadRequestException.class,
                () -> service.startOrder(100L));
    }

    @Test
    void startOrder_shouldThrow_whenBeforeExpertStartTime() {

        order.setOrderStatus(OrderStatus.WAITING_FOR_EXPERT);

        offer.setProposedStartTime(
                LocalDateTime.now().plusHours(2));

        when(orderRepository.findById(100L))
                .thenReturn(Optional.of(order));

        assertThrows(BadRequestException.class,
                () -> service.startOrder(100L));
    }

    @Test
    void completeOrder_shouldCompleteSuccessfully() {

        order.setOrderStatus(OrderStatus.STARTED);

        when(orderRepository.findById(100L))
                .thenReturn(Optional.of(order));

        when(orderRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        CustomerOrder result = service.completeOrder(100L);

        assertEquals(OrderStatus.COMPLETED,
                result.getOrderStatus());

        assertEquals(OrderPaymentStatus.UNPAID,
                result.getOrderPaymentStatus());
    }

    @Test
    void completeOrder_shouldThrow_whenNotStarted() {

        order.setOrderStatus(OrderStatus.WAITING_FOR_EXPERT);

        when(orderRepository.findById(100L))
                .thenReturn(Optional.of(order));

        assertThrows(BadRequestException.class,
                () -> service.completeOrder(100L));
    }

    @Test
    void findByStatus_shouldReturnPage() {

        Pageable pageable = PageRequest.of(0, 10);

        when(orderRepository.findByOrderStatus(
                OrderStatus.COMPLETED,
                pageable))
                .thenReturn(Page.empty());

        Page<CustomerOrder> result =
                service.findByStatus(
                        OrderStatus.COMPLETED,
                        pageable);

        assertNotNull(result);
    }

    @Test
    void findAvailableOrdersForExpert_shouldReturnPage() {

        Pageable pageable = PageRequest.of(0, 10);

        when(orderRepository
                .findByHomeService_Experts_IdAndOrderStatusIn(
                        anyLong(),
                        anyList(),
                        eq(pageable)))
                .thenReturn(Page.empty());

        Page<CustomerOrder> result =
                service.findAvailableOrdersForExpert(
                        10L,
                        pageable);

        assertNotNull(result);

        verify(expertCoreService)
                .validateApprovedExpert(10L);
    }

    @Test
    void findAvailableOrdersForExpert_shouldThrow_whenExpertNotApproved() {

        Pageable pageable = PageRequest.of(0, 10);

        doThrow(new BadRequestException("EXPERT_NOT_APPROVED"))
                .when(expertCoreService)
                .validateApprovedExpert(10L);

        assertThrows(
                BadRequestException.class,
                () -> service.findAvailableOrdersForExpert(
                        10L,
                        pageable
                )
        );
    }


    @Test
    void findCustomerOrder_shouldReturnOrder() {

        when(orderRepository.findById(100L))
                .thenReturn(Optional.of(order));

        CustomerOrder result =
                service.findCustomerOrder(1L, 100L);

        assertNotNull(result);
    }

    @Test
    void findCustomerOrder_shouldThrow_whenNotOwner() {

        when(orderRepository.findById(100L))
                .thenReturn(Optional.of(order));

        assertThrows(BadRequestException.class,
                () -> service.findCustomerOrder(999L, 100L));
    }

    @Test
    void markAsPaid_shouldUpdateStatus() {

        when(orderRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        service.markAsPaid(order);

        assertEquals(OrderPaymentStatus.PAID,
                order.getOrderPaymentStatus());

        verify(orderRepository).save(order);
    }

    @Test
    void findOrderHistory_shouldReturnPage() {

        Pageable pageable = PageRequest.of(0, 10);

        when(orderRepository.findHistoryByExpertId(
                10L,
                pageable))
                .thenReturn(Page.empty());

        Page<CustomerOrder> result =
                service.findOrderHistory(10L, pageable);

        assertNotNull(result);

        verify(expertCoreService)
                .validateApprovedExpert(10L);

        verify(orderRepository)
                .findHistoryByExpertId(10L, pageable);
    }

    @Test
    void findOrderHistory_shouldThrow_whenExpertNotApproved() {

        Pageable pageable = PageRequest.of(0, 10);

        doThrow(new BadRequestException("EXPERT_NOT_APPROVED"))
                .when(expertCoreService)
                .validateApprovedExpert(10L);

        assertThrows(
                BadRequestException.class,
                () -> service.findOrderHistory(
                        10L,
                        pageable
                )
        );
    }

    @Test
    void validatePayOrder_shouldPass() {

        order.setOrderStatus(OrderStatus.COMPLETED);

        assertDoesNotThrow(() ->
                service.validatePayOrder(order, 1L));
    }

    @Test
    void validatePayOrder_shouldThrow_whenOrderNull() {

        assertThrows(NotFoundException.class,
                () -> service.validatePayOrder(null, 1L));
    }

    @Test
    void validatePayOrder_shouldThrow_whenWrongCustomer() {

        order.setOrderStatus(OrderStatus.COMPLETED);

        assertThrows(BadRequestException.class,
                () -> service.validatePayOrder(order, 999L));
    }

    @Test
    void validatePayOrder_shouldThrow_whenOrderNotCompleted() {

        order.setOrderStatus(OrderStatus.STARTED);

        assertThrows(BadRequestException.class,
                () -> service.validatePayOrder(order, 1L));
    }

    @Test
    void validatePayOrder_shouldThrow_whenAlreadyPaid() {

        order.setOrderStatus(OrderStatus.COMPLETED);
        order.setOrderPaymentStatus(OrderPaymentStatus.PAID);

        assertThrows(BadRequestException.class,
                () -> service.validatePayOrder(order, 1L));
    }

    @Test
    void validatePayOrder_shouldThrow_whenFinalPriceNull() {

        order.setOrderStatus(OrderStatus.COMPLETED);
        order.setFinalPrice(null);

        assertThrows(BadRequestException.class,
                () -> service.validatePayOrder(order, 1L));
    }

    @Test
    void validatePayOrder_shouldThrow_whenFinalPriceInvalid() {

        order.setOrderStatus(OrderStatus.COMPLETED);
        order.setFinalPrice(BigDecimal.ZERO);

        assertThrows(BadRequestException.class,
                () -> service.validatePayOrder(order, 1L));
    }

    @Test
    void validatePayOrder_shouldThrow_whenAcceptedOfferNull() {

        order.setOrderStatus(OrderStatus.COMPLETED);
        order.setAcceptedOffer(null);

        assertThrows(BadRequestException.class,
                () -> service.validatePayOrder(order, 1L));
    }

    @Test
    void validatePayOrder_shouldThrow_whenExpertNull() {

        order.setOrderStatus(OrderStatus.COMPLETED);

        Offer invalidOffer = new Offer();
        invalidOffer.setExpert(null);

        order.setAcceptedOffer(invalidOffer);

        assertThrows(BadRequestException.class,
                () -> service.validatePayOrder(order, 1L));
    }


    @Test
    void getOrderHistory_shouldReturnPage() {

        OrderHistoryFilterRequest request = new OrderHistoryFilterRequest();

        Pageable pageable = PageRequest.of(0, 10);

        Page<CustomerOrder> page = Page.empty();

        when(orderRepository.findAll(
                any(Specification.class),
                eq(pageable)
        )).thenReturn(page);

        Page<CustomerOrder> result =
                service.getOrderHistory(
                        1L,
                        request,
                        pageable
                );

        assertEquals(page, result);

        verify(orderRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void getCustomerHistory_shouldReturnPage() {

        AdminHistoryFilterRequest request =
                new AdminHistoryFilterRequest();

        Pageable pageable =
                PageRequest.of(0, 10);

        Page<CustomerOrder> page =
                Page.empty();

        when(orderRepository.findAll(
                any(Specification.class),
                eq(pageable)
        )).thenReturn(page);

        Page<CustomerOrder> result =
                service.getCustomerHistory(
                        1L,
                        request,
                        pageable
                );

        assertEquals(page, result);
    }
    @Test
    void getExpertHistory_shouldReturnPage() {

        AdminHistoryFilterRequest request =
                new AdminHistoryFilterRequest();

        Pageable pageable =
                PageRequest.of(0, 10);

        Page<CustomerOrder> page =
                Page.empty();

        when(orderRepository.findAll(
                any(Specification.class),
                eq(pageable)
        )).thenReturn(page);

        Page<CustomerOrder> result =
                service.getExpertHistory(
                        10L,
                        request,
                        pageable
                );

        assertEquals(page, result);
    }
    @Test
    void getOrderDetails_shouldReturnOrder() {

        when(orderRepository.findDetailedById(100L))
                .thenReturn(Optional.of(order));

        CustomerOrder result =
                service.getOrderDetails(100L);

        assertEquals(order, result);
    }

    @Test
    void getOrderDetails_shouldThrow_whenNotFound() {

        when(orderRepository.findDetailedById(100L))
                .thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> service.getOrderDetails(100L)
        );
    }

    @Test
    void validatePayOrder_shouldThrow_whenCustomerNull() {

        order.setCustomer(null);
        order.setOrderStatus(OrderStatus.COMPLETED);

        assertThrows(
                BadRequestException.class,
                () -> service.validatePayOrder(order, 1L)
        );
    }

    @Test
    void findExpertOrderDetails_shouldReturnOrder() {

        when(orderRepository.findExpertAccessibleOrder(
                100L,
                10L
        )).thenReturn(Optional.of(order));

        CustomerOrder result =
                service.findExpertOrderDetails(
                        10L,
                        100L
                );

        assertEquals(order, result);
    }

    @Test
    void findExpertOrderDetails_shouldThrow_whenNoAccess() {

        when(orderRepository.findExpertAccessibleOrder(
                100L,
                10L
        )).thenReturn(Optional.empty());

        assertThrows(
                BadRequestException.class,
                () -> service.findExpertOrderDetails(
                        10L,
                        100L
                )
        );
    }
}