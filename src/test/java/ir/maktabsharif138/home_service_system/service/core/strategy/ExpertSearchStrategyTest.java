package ir.maktabsharif138.home_service_system.service.core.strategy;

import ir.maktabsharif138.home_service_system.dto.request.UserSearchRequest;
import ir.maktabsharif138.home_service_system.entity.Expert;
import ir.maktabsharif138.home_service_system.entity.enums.Role;
import ir.maktabsharif138.home_service_system.repository.ExpertRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpertSearchStrategyTest {

    @Mock
    private ExpertRepository repository;

    @InjectMocks
    private ExpertSearchStrategy strategy;

    private UserSearchRequest request;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        request = new UserSearchRequest();
        pageable = Pageable.unpaged();
    }

    @Test
    void supports_shouldReturnTrue_whenRoleIsExpert() {

        assertTrue(strategy.supports(Role.EXPERT));
    }

    @Test
    void supports_shouldReturnFalse_whenRoleIsNotExpert() {

        assertFalse(strategy.supports(Role.CUSTOMER));
    }

    @Test
    void search_shouldReturnPageOfExperts() {

        Expert expert = new Expert();
        expert.setId(1L);

        Page<Expert> expected = new PageImpl<>(List.of(expert));

        when(repository.findAll(
                any(Specification.class),
                eq(pageable)
        )).thenReturn(expected);

        Page<Expert> result = strategy.search(request, pageable);

        assertEquals(expected, result);

        verify(repository, times(1))
                .findAll(any(Specification.class), eq(pageable));
    }
}