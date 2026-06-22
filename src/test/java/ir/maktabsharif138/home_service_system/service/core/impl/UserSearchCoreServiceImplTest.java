package ir.maktabsharif138.home_service_system.service.core.impl;

import ir.maktabsharif138.home_service_system.dto.request.UserSearchRequest;
import ir.maktabsharif138.home_service_system.entity.BaseUser;
import ir.maktabsharif138.home_service_system.entity.Customer;
import ir.maktabsharif138.home_service_system.entity.Expert;
import ir.maktabsharif138.home_service_system.entity.enums.Role;
import ir.maktabsharif138.home_service_system.service.core.strategy.UserSearchStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserSearchCoreServiceImplTest {

    @Mock
    private UserSearchStrategy expertStrategy;

    @Mock
    private UserSearchStrategy customerStrategy;

    private UserSearchCoreServiceImpl service;

    private UserSearchRequest request;
    private Pageable pageable;

    @BeforeEach
    void setUp() {

        request = new UserSearchRequest();
        pageable = PageRequest.of(0, 10);

        lenient().when(expertStrategy.supports(Role.EXPERT)).thenReturn(true);
        lenient().when(customerStrategy.supports(Role.CUSTOMER)).thenReturn(true);

        service = new UserSearchCoreServiceImpl(
                List.of(expertStrategy, customerStrategy)
        );
    }

    @Test
    void search_shouldUseRoleStrategy_whenRoleIsProvided() {

        request.setRole(Role.EXPERT);

        Page<Expert> expertPage =
                new PageImpl<>(List.of(new Expert()), pageable, 1);

        when(expertStrategy.search(request, pageable))
                .thenReturn((Page) expertPage);

        Page<?> result = service.search(request, pageable);

        assertEquals(1, result.getTotalElements());
        verify(expertStrategy).search(request, pageable);
    }

    @Test
    void search_shouldReturnMergedResult_whenNoRole() {

        Page<Expert> expertPage =
                new PageImpl<>(List.of(new Expert()), pageable, 1);

        Page<Customer> customerPage =
                new PageImpl<>(List.of(new Customer()), pageable, 1);

        when(expertStrategy.search(any(), any()))
                .thenReturn((Page) expertPage);

        when(customerStrategy.search(any(), any()))
                .thenReturn((Page) customerPage);

        Page<?> result = service.search(request, pageable);

        assertEquals(2, result.getTotalElements());

        verify(expertStrategy).search(request, pageable);
        verify(customerStrategy).search(request, pageable);
    }

    @Test
    void search_shouldReturnOnlyExperts_whenExpertFiltersExist() {

        request.setHomeServiceId(1L);

        Page<Expert> expertPage =
                new PageImpl<>(List.of(new Expert()), pageable, 1);

        when(expertStrategy.search(any(), any()))
                .thenReturn((Page) expertPage);

        Page<?> result = service.search(request, pageable);

        assertEquals(1, result.getTotalElements());

        verify(expertStrategy).search(any(), any());
        verify(customerStrategy, never()).search(any(), any());
    }

    @Test
    void search_shouldThrow_whenNoStrategyFound() {

        UserSearchStrategy emptyStrategy = mock(UserSearchStrategy.class);

        UserSearchCoreServiceImpl testService =
                new UserSearchCoreServiceImpl(List.of(emptyStrategy));

        request.setRole(Role.EXPERT);

        when(emptyStrategy.supports(Role.EXPERT)).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> testService.search(request, pageable));
    }
}