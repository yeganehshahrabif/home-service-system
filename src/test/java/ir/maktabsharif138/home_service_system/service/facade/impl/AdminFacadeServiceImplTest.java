package ir.maktabsharif138.home_service_system.service.facade.impl;

import ir.maktabsharif138.home_service_system.dto.request.*;
import ir.maktabsharif138.home_service_system.dto.response.*;
import ir.maktabsharif138.home_service_system.entity.*;
import ir.maktabsharif138.home_service_system.entity.enums.OrderStatus;
import ir.maktabsharif138.home_service_system.mapper.*;
import ir.maktabsharif138.home_service_system.service.core.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminFacadeServiceImplTest {

    @Mock
    private HomeServiceMapper homeServiceMapper;

    @Mock
    private ExpertMapper expertMapper;

    @Mock
    private CustomerOrderMapper customerOrderMapper;

    @Mock
    private CustomerMapper customerMapper;

    @Mock
    private UserSearchCoreService userSearchCoreService;

    @Mock
    private ExpertCoreService expertCoreService;

    @Mock
    private HomeServiceCoreService homeServiceCoreService;

    @Mock
    private CustomerOrderCoreService customerOrderCoreService;

    @InjectMocks
    private AdminFacadeServiceImpl adminFacadeService;

    private Pageable pageable;

    @BeforeEach
    void setUp() {
        pageable = Pageable.unpaged();
    }

    @Test
    void createService_shouldCreateSuccessfully() {

        HomeServiceCreateRequest request = new HomeServiceCreateRequest();
        HomeService entity = new HomeService();
        HomeService saved = new HomeService();
        HomeServiceResponse response = new HomeServiceResponse();

        when(homeServiceMapper.toHomeService(request)).thenReturn(entity);
        when(homeServiceCoreService.create(entity)).thenReturn(saved);
        when(homeServiceMapper.toHomeServiceResponse(saved)).thenReturn(response);

        HomeServiceResponse result = adminFacadeService.createService(request);

        assertNotNull(result);

        verify(homeServiceMapper).toHomeService(request);
        verify(homeServiceCoreService).create(entity);
    }

    @Test
    void createService_shouldSetParentService() {

        HomeServiceCreateRequest request =
                new HomeServiceCreateRequest();

        request.setParentServiceId(5L);

        HomeService entity =
                new HomeService();

        HomeService parent =
                new HomeService();

        HomeService saved =
                new HomeService();

        HomeServiceResponse response =
                new HomeServiceResponse();

        when(homeServiceMapper.toHomeService(request))
                .thenReturn(entity);

        when(homeServiceCoreService.findById(5L))
                .thenReturn(parent);

        when(homeServiceCoreService.create(entity))
                .thenReturn(saved);

        when(homeServiceMapper.toHomeServiceResponse(saved))
                .thenReturn(response);

        adminFacadeService.createService(request);

        assertEquals(parent, entity.getParentService());

        verify(homeServiceCoreService)
                .findById(5L);
    }

    @Test
    void updateService_shouldUpdateSuccessfully() {

        Long id = 1L;
        HomeServiceUpdateRequest request = new HomeServiceUpdateRequest();

        HomeService existing = new HomeService();
        HomeService updated = new HomeService();
        HomeServiceResponse response = new HomeServiceResponse();

        when(homeServiceCoreService.findById(id)).thenReturn(existing);
        when(homeServiceCoreService.update(existing)).thenReturn(updated);
        when(homeServiceMapper.toHomeServiceResponse(updated)).thenReturn(response);

        HomeServiceResponse result = adminFacadeService.updateService(id, request);

        assertNotNull(result);

        verify(homeServiceCoreService).findById(id);
        verify(homeServiceCoreService).update(existing);
    }

    @Test
    void updateService_shouldSetParentService() {

        Long id = 1L;

        HomeServiceUpdateRequest request =
                new HomeServiceUpdateRequest();

        request.setParentServiceId(5L);

        HomeService existing =
                new HomeService();

        HomeService parent =
                new HomeService();

        HomeService updated =
                new HomeService();

        HomeServiceResponse response =
                new HomeServiceResponse();

        when(homeServiceCoreService.findById(id))
                .thenReturn(existing);

        when(homeServiceCoreService.findById(5L))
                .thenReturn(parent);

        when(homeServiceCoreService.update(existing))
                .thenReturn(updated);

        when(homeServiceMapper.toHomeServiceResponse(updated))
                .thenReturn(response);

        adminFacadeService.updateService(id, request);

        assertEquals(parent, existing.getParentService());

        verify(homeServiceCoreService)
                .findById(5L);
    }

    @Test
    void getHomeService_shouldReturnResponse() {

        Long id = 1L;
        HomeService entity = new HomeService();
        HomeServiceResponse response = new HomeServiceResponse();

        when(homeServiceCoreService.findById(id)).thenReturn(entity);
        when(homeServiceMapper.toHomeServiceResponse(entity)).thenReturn(response);

        HomeServiceResponse result = adminFacadeService.getHomeService(id);

        assertNotNull(result);

        verify(homeServiceCoreService).findById(id);
        verify(homeServiceMapper).toHomeServiceResponse(entity);
    }

    @Test
    void deleteService_shouldCallCore() {

        Long id = 1L;

        adminFacadeService.deleteService(id);

        verify(homeServiceCoreService).delete(id);
    }

    @Test
    void getAllMainServices_shouldReturnList() {

        List<HomeService> list = List.of(new HomeService());
        List<HomeServiceResponse> mapped = List.of(new HomeServiceResponse());

        when(homeServiceCoreService.findAllMainServices()).thenReturn(list);
        when(homeServiceMapper.toHomeServiceResponse(list)).thenReturn(mapped);

        List<HomeServiceResponse> result = adminFacadeService.getAllMainServices();

        assertNotNull(result);

        verify(homeServiceCoreService).findAllMainServices();
        verify(homeServiceMapper).toHomeServiceResponse(list);
    }

    @Test
    void getSubServicesByParentId_shouldReturnList() {

        Long parentId = 1L;

        List<HomeService> list = List.of(new HomeService());
        List<HomeServiceResponse> mapped = List.of(new HomeServiceResponse());

        when(homeServiceCoreService.findSubServicesByParentId(parentId)).thenReturn(list);
        when(homeServiceMapper.toHomeServiceResponse(list)).thenReturn(mapped);

        List<HomeServiceResponse> result =
                adminFacadeService.getSubServicesByParentId(parentId);

        assertNotNull(result);

        verify(homeServiceCoreService).findSubServicesByParentId(parentId);
    }

    @Test
    void addExpertToSubService_shouldCallCore() {

        adminFacadeService.addExpertToSubService(1L, 2L);

        verify(homeServiceCoreService).addExpertToSubService(1L, 2L);
    }

    @Test
    void removeExpertFromSubService_shouldCallCore() {

        adminFacadeService.removeExpertFromSubService(1L, 2L);

        verify(homeServiceCoreService).removeExpertFromSubService(1L, 2L);
    }

    @Test
    void getPendingExperts_shouldReturnPage() {

        Expert expert = new Expert();
        Page<Expert> page = new PageImpl<>(List.of(expert));
        Page<ExpertResponse> mapped = new PageImpl<>(List.of(new ExpertResponse()));

        when(expertCoreService.findPendingExperts(pageable)).thenReturn(page);
        when(expertMapper.toExpertResponse(expert)).thenReturn(new ExpertResponse());

        Page<ExpertResponse> result =
                adminFacadeService.getPendingExperts(pageable);

        assertNotNull(result);

        verify(expertCoreService).findPendingExperts(pageable);
    }

    @Test
    void approveExpert_shouldCallCore() {

        adminFacadeService.approveExpert(1L);

        verify(expertCoreService).approveExpert(1L);
    }

    @Test
    void rejectExpert_shouldCallCore() {

        adminFacadeService.rejectExpert(1L);

        verify(expertCoreService).rejectExpert(1L);
    }

    @Test
    void getOrdersByStatus_shouldReturnPage() {

        CustomerOrder order = new CustomerOrder();
        Page<CustomerOrder> page = new PageImpl<>(List.of(order));

        when(customerOrderCoreService.findByStatus(OrderStatus.STARTED, pageable))
                .thenReturn(page);

        when(customerOrderMapper.toCustomerOrderResponse(order))
                .thenReturn(new CustomerOrderResponse());

        Page<CustomerOrderResponse> result =
                adminFacadeService.getOrdersByStatus(OrderStatus.STARTED, pageable);

        assertNotNull(result);

        verify(customerOrderCoreService).findByStatus(OrderStatus.STARTED, pageable);
    }

    @Test
    void searchUsers_shouldReturnMappedResults() {

        UserSearchRequest request = new UserSearchRequest();

        Expert expert = new Expert();
        Customer customer = new Customer();

        Page<? extends BaseUser> page =
                new PageImpl<>(List.of(expert, customer));

        when(userSearchCoreService.search(request, pageable))
                .thenReturn((Page) page);

        when(expertMapper.toSearchResponse(expert))
                .thenReturn(mock(UserSearchResponse.class));

        when(customerMapper.toSearchResponse(customer))
                .thenReturn(mock(UserSearchResponse.class));

        Page<UserSearchResponse> result =
                adminFacadeService.searchUsers(request, pageable);

        assertNotNull(result);

        verify(userSearchCoreService).search(request, pageable);
    }

    @Test
    void getCustomerHistory_shouldReturnMappedPage() {

        AdminHistoryFilterRequest request =
                new AdminHistoryFilterRequest();

        OrderHistorySummaryResponse response =
                new OrderHistorySummaryResponse();

        CustomerOrder order = new CustomerOrder();

        Page<CustomerOrder> page =
                new PageImpl<>(List.of(order));

        when(customerOrderCoreService.getCustomerHistory(
                1L,
                request,
                pageable
        )).thenReturn(page);

        when(customerOrderMapper.toOrderHistorySummaryResponse(order))
                .thenReturn(response);

        Page<OrderHistorySummaryResponse> result =
                adminFacadeService.getCustomerHistory(
                        1L,
                        request,
                        pageable
                );

        assertEquals(1, result.getTotalElements());

        verify(customerOrderCoreService)
                .getCustomerHistory(
                        1L,
                        request,
                        pageable
                );
    }
    @Test
    void getExpertHistory_shouldReturnMappedPage() {

        AdminHistoryFilterRequest request =
                new AdminHistoryFilterRequest();

        OrderHistorySummaryResponse response =
                new OrderHistorySummaryResponse();

        CustomerOrder order = new CustomerOrder();

        Page<CustomerOrder> page =
                new PageImpl<>(List.of(order));

        when(customerOrderCoreService.getExpertHistory(
                10L,
                request,
                pageable
        )).thenReturn(page);

        when(customerOrderMapper.toOrderHistorySummaryResponse(order))
                .thenReturn(response);

        Page<OrderHistorySummaryResponse> result =
                adminFacadeService.getExpertHistory(
                        10L,
                        request,
                        pageable
                );

        assertEquals(1, result.getTotalElements());

        verify(customerOrderCoreService)
                .getExpertHistory(
                        10L,
                        request,
                        pageable
                );
    }
    @Test
    void getOrderHistoryDetails_shouldReturnResponse() {

        CustomerOrder order =
                new CustomerOrder();

        OrderHistoryDetailsResponse response =
                new OrderHistoryDetailsResponse();

        when(customerOrderCoreService.getOrderDetails(100L))
                .thenReturn(order);

        when(customerOrderMapper
                .toOrderHistoryDetailsResponse(order))
                .thenReturn(response);

        OrderHistoryDetailsResponse result =
                adminFacadeService.getOrderHistoryDetails(100L);

        assertNotNull(result);

        verify(customerOrderCoreService)
                .getOrderDetails(100L);

        verify(customerOrderMapper)
                .toOrderHistoryDetailsResponse(order);
    }
}