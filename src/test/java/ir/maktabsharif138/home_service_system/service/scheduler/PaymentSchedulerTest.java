package ir.maktabsharif138.home_service_system.service.scheduler;

import ir.maktabsharif138.home_service_system.service.core.PaymentCoreService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentSchedulerTest {

    @Mock
    private PaymentCoreService paymentCoreService;

    @InjectMocks
    private PaymentScheduler paymentScheduler;

    @Test
    void expirePayments_shouldCallServiceAndLogWhenPaymentsExpired() {

        when(paymentCoreService.expireOldPayments())
                .thenReturn(5);

        paymentScheduler.expirePayments();

        verify(paymentCoreService, times(1))
                .expireOldPayments();

        verifyNoMoreInteractions(paymentCoreService);
    }

    @Test
    void expirePayments_shouldCallServiceWhenNoPaymentsExpired() {

        when(paymentCoreService.expireOldPayments())
                .thenReturn(0);

        paymentScheduler.expirePayments();

        verify(paymentCoreService, times(1))
                .expireOldPayments();

        verifyNoMoreInteractions(paymentCoreService);
    }
}