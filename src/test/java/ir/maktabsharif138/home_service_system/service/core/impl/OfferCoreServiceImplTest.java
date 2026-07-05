package ir.maktabsharif138.home_service_system.service.core.impl;

import ir.maktabsharif138.home_service_system.entity.CustomerOrder;
import ir.maktabsharif138.home_service_system.entity.Expert;
import ir.maktabsharif138.home_service_system.entity.HomeService;
import ir.maktabsharif138.home_service_system.entity.Offer;
import ir.maktabsharif138.home_service_system.entity.enums.AccountStatus;
import ir.maktabsharif138.home_service_system.entity.enums.OfferStatus;
import ir.maktabsharif138.home_service_system.entity.enums.OrderStatus;
import ir.maktabsharif138.home_service_system.exception.BadRequestException;
import ir.maktabsharif138.home_service_system.exception.NotFoundException;
import ir.maktabsharif138.home_service_system.repository.CustomerOrderRepository;
import ir.maktabsharif138.home_service_system.repository.OfferRepository;
import ir.maktabsharif138.home_service_system.service.core.CustomerOrderCoreService;
import ir.maktabsharif138.home_service_system.service.core.ExpertCoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OfferCoreServiceImplTest {

    @Mock
    private OfferRepository offerRepository;

    @Mock
    private CustomerOrderRepository customerOrderRepository;

    @Mock
    private CustomerOrderCoreService customerOrderCoreService;

    @Mock
    private ExpertCoreService expertCoreService;

    @InjectMocks
    private OfferCoreServiceImpl offerCoreService;

    private Offer offer;
    private CustomerOrder order;
    private Expert expert;
    private HomeService homeService;

    @BeforeEach
    void setUp() {

        homeService = new HomeService();
        homeService.setId(1L);

        expert = new Expert();
        expert.setId(2L);
        expert.setAccountStatus(AccountStatus.APPROVED);

        Set<HomeService> services = new HashSet<>();
        services.add(homeService);
        expert.setHomeServices(services);

        order = new CustomerOrder();
        order.setId(1L);
        order.setHomeService(homeService);
        order.setOrderStatus(OrderStatus.WAITING_FOR_OFFERS);
        order.setOffers(new HashSet<>());

        offer = new Offer();
        offer.setId(10L);
        offer.setCustomerOrder(order);
        offer.setExpert(expert);
        offer.setProposedPrice(BigDecimal.valueOf(100));
        offer.setProposedStartTime(LocalDateTime.now().plusDays(1));
        offer.setDurationHours(2);
    }

    @Test
    void createOffer_shouldCreateSuccessfully() {

        when(offerRepository.existsByCustomerOrderIdAndExpertId(1L, 2L))
                .thenReturn(false);

        when(offerRepository.existsByCustomerOrderId(1L))
                .thenReturn(false);

        when(offerRepository.save(any(Offer.class)))
                .thenReturn(offer);

        Offer result = offerCoreService.createOffer(offer);

        assertNotNull(result);
        verify(offerRepository).save(any(Offer.class));
        assertEquals(OrderStatus.WAITING_FOR_SELECTION,
                order.getOrderStatus());
    }

    @Test
    void createOffer_shouldThrow_whenStartTimeInPast() {

        offer.setProposedStartTime(
                LocalDateTime.now().minusHours(1)
        );

        assertThrows(
                BadRequestException.class,
                () -> offerCoreService.createOffer(
                        offer
                )
        );
    }

    @Test
    void createOffer_shouldThrow_whenExpertNotApproved() {

        expert.setAccountStatus(AccountStatus.NEW);

        assertThrows(BadRequestException.class,
                () -> offerCoreService.createOffer(offer));
    }

    @Test
    void createOffer_shouldThrow_whenWrongService() {

        HomeService anotherService = new HomeService();
        anotherService.setId(99L);

        order.setHomeService(anotherService);

        assertThrows(BadRequestException.class,
                () -> offerCoreService.createOffer(offer));
    }

    @Test
    void createOffer_shouldThrow_whenDuplicateOffer() {

        when(offerRepository.existsByCustomerOrderIdAndExpertId(1L, 2L))
                .thenReturn(true);

        assertThrows(BadRequestException.class,
                () -> offerCoreService.createOffer(offer));
    }

    @Test
    void createOffer_shouldThrow_whenOrderStatusInvalid() {

        order.setOrderStatus(OrderStatus.COMPLETED);

        assertThrows(BadRequestException.class,
                () -> offerCoreService.createOffer(offer));
    }

    @Test
    void findByExpertId_shouldReturnPage() {

        Pageable pageable = PageRequest.of(0, 10);

        Page<Offer> page =
                new PageImpl<>(java.util.List.of(offer));

        when(offerRepository.findByExpertId(
                2L,
                pageable
        )).thenReturn(page);

        Page<Offer> result =
                offerCoreService.findByExpertId(
                        2L,
                        pageable
                );

        assertEquals(
                1,
                result.getTotalElements()
        );

        verify(expertCoreService)
                .validateApprovedExpert(2L);
    }

    @Test
    void findByExpertId_shouldThrow_whenExpertNotApproved() {

        Pageable pageable =
                PageRequest.of(0, 10);

        doThrow(
                new BadRequestException(
                        "Expert account is not approved yet"
                )
        ).when(expertCoreService)
                .validateApprovedExpert(2L);

        assertThrows(
                BadRequestException.class,
                () -> offerCoreService.findByExpertId(
                        2L,
                        pageable
                )
        );

        verify(offerRepository, never())
                .findByExpertId(anyLong(), any());
    }

    @Test
    void findByOrderIdSortedByPrice_shouldReturnPage() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<Offer> page = new PageImpl<>(java.util.List.of(offer));

        when(offerRepository
                .findByCustomerOrderIdOrderByProposedPriceAsc(1L, pageable))
                .thenReturn(page);

        Page<Offer> result =
                offerCoreService.findByOrderIdSortedByPrice(1L, pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void findByOrderIdSortedByExpertRating_shouldReturnPage() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<Offer> page = new PageImpl<>(java.util.List.of(offer));

        when(offerRepository.findByOrderIdOrderByExpertRating(1L, pageable))
                .thenReturn(page);

        Page<Offer> result =
                offerCoreService.findByOrderIdSortedByExpertRating(1L, pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void findById_shouldReturnOffer() {

        when(offerRepository.findById(10L))
                .thenReturn(Optional.of(offer));

        Offer result = offerCoreService.findById(10L);

        assertEquals(10L, result.getId());
    }

    @Test
    void findById_shouldThrow_whenNotFound() {

        when(offerRepository.findById(10L))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> offerCoreService.findById(10L));
    }

    @Test
    void acceptOffer_shouldAcceptSuccessfully() {

        Offer anotherOffer = new Offer();
        anotherOffer.setId(20L);
        anotherOffer.setOfferStatus(OfferStatus.PENDING);

        Set<Offer> offers = new HashSet<>();
        offers.add(offer);
        offers.add(anotherOffer);

        order.setOffers(offers);
        order.setOrderStatus(OrderStatus.WAITING_FOR_SELECTION);

        offer.setOfferStatus(OfferStatus.PENDING);

        when(customerOrderCoreService.findById(1L))
                .thenReturn(order);

        when(offerRepository.findById(10L))
                .thenReturn(Optional.of(offer));

        Offer result =
                offerCoreService.acceptOffer(1L, 10L);

        assertEquals(OfferStatus.ACCEPTED,
                result.getOfferStatus());

        assertEquals(OrderStatus.WAITING_FOR_EXPERT,
                order.getOrderStatus());

        assertEquals(OfferStatus.REJECTED,
                anotherOffer.getOfferStatus());

        verify(offerRepository).save(offer);
        verify(customerOrderRepository).save(order);
    }

    @Test
    void acceptOffer_shouldThrow_whenOfferAlreadyProcessed() {

        offer.setOfferStatus(OfferStatus.ACCEPTED);

        order.setOrderStatus(OrderStatus.WAITING_FOR_SELECTION);

        when(customerOrderCoreService.findById(1L))
                .thenReturn(order);

        when(offerRepository.findById(10L))
                .thenReturn(Optional.of(offer));

        assertThrows(BadRequestException.class,
                () -> offerCoreService.acceptOffer(1L, 10L));
    }

    @Test
    void acceptOffer_shouldThrow_whenOfferDoesNotBelongToOrder() {

        CustomerOrder anotherOrder = new CustomerOrder();
        anotherOrder.setId(99L);

        offer.setCustomerOrder(anotherOrder);
        offer.setOfferStatus(OfferStatus.PENDING);

        order.setOrderStatus(OrderStatus.WAITING_FOR_SELECTION);

        when(customerOrderCoreService.findById(1L))
                .thenReturn(order);

        when(offerRepository.findById(10L))
                .thenReturn(Optional.of(offer));

        assertThrows(BadRequestException.class,
                () -> offerCoreService.acceptOffer(1L, 10L));
    }

    @Test
    void acceptOffer_shouldThrow_whenOrderNotWaitingForSelection() {

        offer.setOfferStatus(OfferStatus.PENDING);

        order.setOrderStatus(OrderStatus.STARTED);

        when(customerOrderCoreService.findById(1L))
                .thenReturn(order);

        when(offerRepository.findById(10L))
                .thenReturn(Optional.of(offer));

        assertThrows(BadRequestException.class,
                () -> offerCoreService.acceptOffer(1L, 10L));
    }
}