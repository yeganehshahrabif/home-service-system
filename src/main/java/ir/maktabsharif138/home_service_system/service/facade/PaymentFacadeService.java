package ir.maktabsharif138.home_service_system.service.facade;

import ir.maktabsharif138.home_service_system.dto.response.PaymentResponse;

import java.math.BigDecimal;

public interface PaymentFacadeService {

    // پرداخت سفارش (از کیف پول)
    PaymentResponse payOrder(Long customerId, Long orderId);

    // ساخت لینک شارژ کیف پول
    PaymentResponse createTopUpRequest(Long customerId, BigDecimal amount);

    // تایید پرداخت (callback یا manual)
    PaymentResponse verifyPayment(Long paymentId);

    // گرفتن اطلاعات پرداخت
    PaymentResponse getPayment(Long paymentId);
}