package ir.maktabsharif138.home_service_system.service.core;

import ir.maktabsharif138.home_service_system.entity.Payment;

import java.math.BigDecimal;

public interface PaymentCoreService {

    Payment createTopUpPayment(Long customerId, BigDecimal amount);
    Payment verifyPayment(Long paymentId);
    Payment expirePayment(Long paymentId);
    Payment findById(Long paymentId);
}