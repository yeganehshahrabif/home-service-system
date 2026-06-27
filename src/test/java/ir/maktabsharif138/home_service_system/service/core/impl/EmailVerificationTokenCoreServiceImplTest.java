package ir.maktabsharif138.home_service_system.service.core.impl;

import ir.maktabsharif138.home_service_system.entity.EmailVerificationToken;
import ir.maktabsharif138.home_service_system.entity.enums.Role;
import ir.maktabsharif138.home_service_system.exception.BadRequestException;
import ir.maktabsharif138.home_service_system.exception.NotFoundException;
import ir.maktabsharif138.home_service_system.repository.EmailVerificationTokenRepository;
import ir.maktabsharif138.home_service_system.service.integration.token.TokenGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailVerificationTokenCoreServiceImplTest {

    @Mock
    private EmailVerificationTokenRepository repository;

    @Mock
    private TokenGenerator tokenGenerator;

    @InjectMocks
    private EmailVerificationTokenCoreServiceImpl service;

    private EmailVerificationToken token;

    @BeforeEach
    void setUp() {

        token = new EmailVerificationToken();
        token.setId(1L);
        token.setEmail("test@test.com");
        token.setRole(Role.CUSTOMER);
        token.setToken("token-123");
        token.setUsed(false);
        token.setExpireAt(LocalDateTime.now().plusHours(1));
    }

    @Test
    void createToken_shouldCreateSuccessfully() {

        when(tokenGenerator.generate())
                .thenReturn("generated-token");

        when(repository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        EmailVerificationToken result =
                service.createToken(
                        "test@test.com",
                        Role.CUSTOMER
                );

        assertEquals(
                "generated-token",
                result.getToken()
        );

        assertFalse(result.isUsed());

        verify(repository)
                .save(any(EmailVerificationToken.class));
    }

    @Test
    void createToken_shouldDeleteOldUnusedToken() {

        when(repository.findByEmailAndRoleAndUsedFalse(
                "test@test.com",
                Role.CUSTOMER
        )).thenReturn(Optional.of(token));

        when(tokenGenerator.generate())
                .thenReturn("new-token");

        when(repository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        service.createToken(
                "test@test.com",
                Role.CUSTOMER
        );

        verify(repository).delete(token);
    }

    @Test
    void findValidToken_shouldReturnToken() {

        when(repository.findByToken("token"))
                .thenReturn(Optional.of(token));

        EmailVerificationToken result =
                service.findValidToken("token");

        assertNotNull(result);
    }

    @Test
    void findValidToken_shouldThrow_whenNotFound() {

        when(repository.findByToken("token"))
                .thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> service.findValidToken("token")
        );
    }

    @Test
    void findValidToken_shouldThrow_whenAlreadyUsed() {

        token.setUsed(true);

        when(repository.findByToken("token"))
                .thenReturn(Optional.of(token));

        assertThrows(
                BadRequestException.class,
                () -> service.findValidToken("token")
        );
    }

    @Test
    void findValidToken_shouldThrow_whenExpired() {

        token.setExpireAt(
                LocalDateTime.now().minusMinutes(1)
        );

        when(repository.findByToken("token"))
                .thenReturn(Optional.of(token));

        assertThrows(
                BadRequestException.class,
                () -> service.findValidToken("token"));
    }

    @Test
    void markAsUsed_shouldUpdateToken() {

        service.markAsUsed(token);

        assertTrue(token.isUsed());

        verify(repository).save(token);
    }

    @Test
    void deleteExpiredTokens_shouldDelete() {

        service.deleteExpiredTokens();

        verify(repository)
                .deleteByExpireAtBefore(any(LocalDateTime.class));
    }
}