package ir.maktabsharif138.home_service_system.service.facade;

import ir.maktabsharif138.home_service_system.dto.request.ConfirmRechargeRequest;
import ir.maktabsharif138.home_service_system.dto.response.OrderPaymentResponse;
import ir.maktabsharif138.home_service_system.dto.response.PaymentResponse;

import java.math.BigDecimal;

public interface PaymentFacadeService {


    OrderPaymentResponse payOrder(Long orderId);


    PaymentResponse rechargeWallet(BigDecimal amount);


    PaymentResponse confirmRecharge(ConfirmRechargeRequest request);


    PaymentResponse getPayment(Long paymentId);
}