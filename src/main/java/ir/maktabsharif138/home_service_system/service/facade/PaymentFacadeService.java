package ir.maktabsharif138.home_service_system.service.facade;

import ir.maktabsharif138.home_service_system.dto.request.ConfirmRechargeRequest;
import ir.maktabsharif138.home_service_system.dto.response.PaymentResponse;

import java.math.BigDecimal;

public interface PaymentFacadeService {


    PaymentResponse payOrder(Long customerId, Long orderId);


    PaymentResponse rechargeWallet(Long customerId, BigDecimal amount);


    PaymentResponse confirmRecharge(ConfirmRechargeRequest request);


    PaymentResponse getPayment(Long paymentId);
}