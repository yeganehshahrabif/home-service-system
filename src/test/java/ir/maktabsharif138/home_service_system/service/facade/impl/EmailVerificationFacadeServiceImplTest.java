package ir.maktabsharif138.home_service_system.service.facade.impl;

import ir.maktabsharif138.home_service_system.dto.response.EmailVerificationResponse;
import ir.maktabsharif138.home_service_system.entity.Customer;
import ir.maktabsharif138.home_service_system.entity.EmailVerificationToken;
import ir.maktabsharif138.home_service_system.entity.Expert;
import ir.maktabsharif138.home_service_system.entity.enums.Role;
import ir.maktabsharif138.home_service_system.exception.BadRequestException;
import ir.maktabsharif138.home_service_system.service.core.CustomerCoreService;
import ir.maktabsharif138.home_service_system.service.core.EmailVerificationTokenCoreService;
import ir.maktabsharif138.home_service_system.service.core.ExpertCoreService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailVerificationFacadeServiceImplTest {

    @Mock
    private EmailVerificationTokenCoreService tokenCoreService;

    @Mock
    private CustomerCoreService customerCoreService;

    @Mock
    private ExpertCoreService expertCoreService;

    @InjectMocks
    private EmailVerificationFacadeServiceImpl facade;

    @Test
    void verify_shouldVerifyCustomerSuccessfully() {

        String tokenValue = "token";

        EmailVerificationToken token =
                new EmailVerificationToken();

        token.setEmail("customer@test.com");
        token.setRole(Role.CUSTOMER);

        Customer customer =
                new Customer();

        when(tokenCoreService.findValidToken(tokenValue))
                .thenReturn(token);

        when(customerCoreService.findByEmail("customer@test.com"))
                .thenReturn(customer);

        EmailVerificationResponse result =
                facade.verify(tokenValue);

        assertNotNull(result);
        assertEquals(
                "Email verified successfully",
                result.getMessage()
        );

        verify(customerCoreService)
                .findByEmail("customer@test.com");

        verify(customerCoreService)
                .verifyEmail(customer);

        verify(tokenCoreService)
                .markAsUsed(token);

        verifyNoInteractions(expertCoreService);
    }

    @Test
    void verify_shouldVerifyExpertSuccessfully() {

        String tokenValue = "token";

        EmailVerificationToken token =
                new EmailVerificationToken();

        token.setEmail("expert@test.com");
        token.setRole(Role.EXPERT);

        Expert expert =
                new Expert();

        when(tokenCoreService.findValidToken(tokenValue))
                .thenReturn(token);

        when(expertCoreService.findByEmail("expert@test.com"))
                .thenReturn(expert);

        EmailVerificationResponse result =
                facade.verify(tokenValue);

        assertNotNull(result);
        assertEquals(
                "Email verified successfully",
                result.getMessage()
        );

        verify(expertCoreService)
                .findByEmail("expert@test.com");

        verify(expertCoreService)
                .verifyEmail(expert);

        verify(tokenCoreService)
                .markAsUsed(token);

        verifyNoInteractions(customerCoreService);
    }

    @Test
    void verify_shouldThrowException_whenRoleInvalid() {

        String tokenValue = "token";

        EmailVerificationToken token =
                new EmailVerificationToken();

        token.setRole(null);

        when(tokenCoreService.findValidToken(tokenValue))
                .thenReturn(token);

        assertThrows(
                BadRequestException.class,
                () -> facade.verify(tokenValue)
        );

        verify(tokenCoreService)
                .findValidToken(tokenValue);

        verify(tokenCoreService, never())
                .markAsUsed(any());
    }
    @Test
    void verify_shouldMarkTokenAsUsedAfterVerification() {

        String tokenValue = "token";

        EmailVerificationToken token =
                new EmailVerificationToken();

        token.setEmail("customer@test.com");
        token.setRole(Role.CUSTOMER);

        Customer customer =
                new Customer();

        when(tokenCoreService.findValidToken(tokenValue))
                .thenReturn(token);

        when(customerCoreService.findByEmail(anyString()))
                .thenReturn(customer);

        facade.verify(tokenValue);

        verify(tokenCoreService)
                .markAsUsed(token);
    }
}