package ir.maktabsharif138.home_service_system.service.facade.impl;

import ir.maktabsharif138.home_service_system.common.calculator.CommissionCalculator;
import ir.maktabsharif138.home_service_system.dto.request.ConfirmRechargeRequest;
import ir.maktabsharif138.home_service_system.dto.response.OrderPaymentResponse;
import ir.maktabsharif138.home_service_system.dto.response.PaymentResponse;
import ir.maktabsharif138.home_service_system.entity.*;
import ir.maktabsharif138.home_service_system.entity.enums.OrderPaymentStatus;
import ir.maktabsharif138.home_service_system.exception.BadRequestException;
import ir.maktabsharif138.home_service_system.mapper.PaymentMapper;
import ir.maktabsharif138.home_service_system.service.core.*;
import ir.maktabsharif138.home_service_system.service.integration.captcha.CaptchaService;
import ir.maktabsharif138.home_service_system.service.integration.payment.PaymentLinkBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentFacadeServiceImplTest {

    @Mock private CustomerOrderCoreService orderCoreService;
    @Mock private WalletCoreService walletCoreService;
    @Mock private ExpertCoreService expertCoreService;
    @Mock private PaymentCoreService paymentCoreService;
    @Mock private PlatformAccountCoreService platformService;
    @Mock private CaptchaService captchaService;

    @Mock private PaymentMapper paymentMapper;
    @Mock private PaymentLinkBuilder paymentLinkBuilder;
    @Mock private CommissionCalculator commissionCalculator;

    @InjectMocks
    private PaymentFacadeServiceImpl facade;

    private final Long customerId = 1L;
    private final Long orderId = 2L;

    @Test
    void payOrder_shouldProcessPaymentSuccessfully() {

        CustomerOrder order = mock(CustomerOrder.class);
        Customer customer = mock(Customer.class);
        Wallet customerWallet = mock(Wallet.class);

        Expert expert = mock(Expert.class);
        Wallet expertWallet = mock(Wallet.class);

        PlatformAccount platformAccount = mock(PlatformAccount.class);
        Wallet platformWallet = mock(Wallet.class);

        when(orderCoreService.findById(orderId)).thenReturn(order);
        when(order.getCustomer()).thenReturn(customer);
        when(customer.getWallet()).thenReturn(customerWallet);
        when(customerWallet.getId()).thenReturn(1L);

        when(order.getAcceptedOffer()).thenReturn(mock(Offer.class));
        when(order.getAcceptedOffer().getExpert()).thenReturn(expert);
        when(expert.getWallet()).thenReturn(expertWallet);
        when(expertWallet.getId()).thenReturn(2L);

        when(platformService.getMainAccount()).thenReturn(platformAccount);
        when(platformAccount.getWallet()).thenReturn(platformWallet);
        when(platformWallet.getId()).thenReturn(3L);

        when(order.getFinalPrice()).thenReturn(BigDecimal.valueOf(1000));

        when(commissionCalculator.expertShare(any())).thenReturn(BigDecimal.valueOf(800));
        when(commissionCalculator.platformShare(any())).thenReturn(BigDecimal.valueOf(200));

        when(order.getId()).thenReturn(orderId);
        when(order.getOrderPaymentStatus()).thenReturn(OrderPaymentStatus.PAID);

        OrderPaymentResponse result = facade.payOrder(orderId);

        assertEquals(orderId, result.getOrderId());
        assertEquals(BigDecimal.valueOf(1000), result.getAmount());

        verify(orderCoreService).validatePayOrder(order, customerId);
        verify(expertCoreService).applyDelayPenalty(order);
        verify(walletCoreService, times(1)).debit(anyLong(), any(), anyString());
        verify(walletCoreService, times(2)).credit(anyLong(), any(), anyString());
        verify(orderCoreService).markAsPaid(order);
    }

    @Test
    void rechargeWallet_shouldReturnResponse() {

        Payment payment = mock(Payment.class);
        PaymentResponse response = mock(PaymentResponse.class);

        when(paymentCoreService.createTopUpPayment(customerId, BigDecimal.valueOf(500)))
                .thenReturn(payment);

        when(paymentMapper.toResponse(payment)).thenReturn(response);
        when(payment.getPaymentReference()).thenReturn("REF123");
        when(paymentLinkBuilder.build("REF123")).thenReturn("LINK");

        PaymentResponse result =
                facade.rechargeWallet(BigDecimal.valueOf(500));

        assertEquals(response, result);
        verify(response).setMessage("RECHARGE INITIATED");
        verify(response).setPaymentLink("LINK");
    }

    @Test
    void confirmRecharge_shouldReturnSuccess() {

        ConfirmRechargeRequest request = new ConfirmRechargeRequest();
        request.setCardNumber("1234567812345678");
        request.setCvv2("123");
        request.setExpireDate("12/30");
        request.setPassword("1234");
        request.setCaptchaKey("k");
        request.setCaptchaInput("i");
        request.setPaymentId(1L);

        Payment payment = mock(Payment.class);
        PaymentResponse response = mock(PaymentResponse.class);

        when(paymentCoreService.verifyPayment(request.getPaymentId()))
                .thenReturn(payment);

        when(paymentMapper.toResponse(payment)).thenReturn(response);

        PaymentResponse result = facade.confirmRecharge(request);

        assertEquals(response, result);
        verify(captchaService).validate("k", "i");
        verify(response).setMessage("TOPUP SUCCESS");
    }

    @Test
    void getPayment_shouldReturnResponse() {

        Payment payment = mock(Payment.class);
        PaymentResponse response = mock(PaymentResponse.class);

        when(paymentCoreService.findById(1L)).thenReturn(payment);
        when(paymentMapper.toResponse(payment)).thenReturn(response);

        PaymentResponse result = facade.getPayment(1L);

        assertEquals(response, result);
    }

    @Test
    void confirmRecharge_shouldThrowException_whenCardInvalid() {

        ConfirmRechargeRequest request = new ConfirmRechargeRequest();
        request.setCardNumber("123"); // invalid

        assertThrows(BadRequestException.class,
                () -> facade.confirmRecharge(request));
    }
}