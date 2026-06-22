package ir.maktabsharif138.home_service_system.service.core.impl;

import ir.maktabsharif138.home_service_system.entity.PlatformAccount;
import ir.maktabsharif138.home_service_system.exception.NotFoundException;
import ir.maktabsharif138.home_service_system.repository.PlatformAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlatformAccountCoreServiceImplTest {

    @Mock
    private PlatformAccountRepository platformAccountRepository;

    @InjectMocks
    private PlatformAccountCoreServiceImpl service;

    private PlatformAccount account;

    @BeforeEach
    void setUp() {

        account = new PlatformAccount();
        account.setId(1L);
    }

    // ---------------- getMainAccount ----------------

    @Test
    void getMainAccount_shouldReturnAccount_whenExists() {

        when(platformAccountRepository.findTopByOrderByIdAsc())
                .thenReturn(Optional.of(account));

        PlatformAccount result = service.getMainAccount();

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(platformAccountRepository).findTopByOrderByIdAsc();
    }

    @Test
    void getMainAccount_shouldThrow_whenNotFound() {

        when(platformAccountRepository.findTopByOrderByIdAsc())
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.getMainAccount());

        verify(platformAccountRepository).findTopByOrderByIdAsc();
    }
}