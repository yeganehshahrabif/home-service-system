package ir.maktabsharif138.home_service_system.service.facade.impl;

import ir.maktabsharif138.home_service_system.common.calculator.CommissionCalculator;
import ir.maktabsharif138.home_service_system.dto.request.ConfirmRechargeRequest;
import ir.maktabsharif138.home_service_system.dto.response.OrderPaymentResponse;
import ir.maktabsharif138.home_service_system.dto.response.PaymentResponse;
import ir.maktabsharif138.home_service_system.entity.CustomerOrder;
import ir.maktabsharif138.home_service_system.entity.Payment;
import ir.maktabsharif138.home_service_system.mapper.PaymentMapper;
import ir.maktabsharif138.home_service_system.service.core.*;
import ir.maktabsharif138.home_service_system.service.facade.PaymentFacadeService;
import ir.maktabsharif138.home_service_system.service.integration.captcha.CaptchaService;
import ir.maktabsharif138.home_service_system.service.integration.payment.PaymentLinkBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentFacadeServiceImpl implements PaymentFacadeService {

    private final CustomerOrderCoreService orderCoreService;
    private final WalletCoreService walletCoreService;
    private final ExpertCoreService expertCoreService;
    private final PaymentCoreService paymentCoreService;
    private final PlatformAccountCoreService platformService;
    private final CaptchaService captchaService;

    private final PaymentMapper paymentMapper;
    private final PaymentLinkBuilder paymentLinkBuilder;
    private final CommissionCalculator commissionCalculator;

    @Override
    @Transactional
    public OrderPaymentResponse payOrder(Long customerId, Long orderId) {

        CustomerOrder order = orderCoreService.findById(orderId);
        orderCoreService.validatePayOrder(order, customerId);
        expertCoreService.applyDelayPenalty(order);
        processWalletTransfer(order);
        orderCoreService.markAsPaid(order);

        BigDecimal amount = order.getFinalPrice();
        BigDecimal expertShare = commissionCalculator.expertShare(amount);
        BigDecimal platformShare = commissionCalculator.platformShare(amount);

        return OrderPaymentResponse.builder()
                .orderId(order.getId())
                .amount(amount)
                .expertShare(expertShare)
                .platformShare(platformShare)
                .status(order.getOrderPaymentStatus())
                .message("PAYMENT SUCCESS")
                .build();
    }

    @Override
    public PaymentResponse rechargeWallet(Long customerId, BigDecimal amount) {

        Payment payment = paymentCoreService.createTopUpPayment(customerId, amount);

        PaymentResponse response = paymentMapper.toResponse(payment);

        response.setMessage("RECHARGE INITIATED");

        response.setPaymentLink(paymentLinkBuilder.build(payment.getPaymentReference()));

        return response;
    }
    @Override
    @Transactional
    public PaymentResponse confirmRecharge(ConfirmRechargeRequest request) {

        captchaService.validate(
                request.getCaptchaKey(),
                request.getCaptchaInput()
        );

        Payment payment = paymentCoreService.verifyPayment(request.getPaymentId());

        PaymentResponse response = paymentMapper.toResponse(payment);

        response.setMessage("TOPUP SUCCESS");

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPayment(Long paymentId) {

        return paymentMapper.toResponse(
                paymentCoreService.findById(paymentId)
        );
    }

    private void processWalletTransfer(CustomerOrder order) {

        BigDecimal amount = order.getFinalPrice();
        BigDecimal expertShare = commissionCalculator.expertShare(amount);
        BigDecimal platformShare = commissionCalculator.platformShare(amount);

        walletCoreService.debit(
                order.getCustomer().getWallet().getId(),
                amount,
                "ORDER_PAYMENT"
        );

        walletCoreService.credit(
                order.getAcceptedOffer().getExpert().getWallet().getId(),
                expertShare,
                "EXPERT_INCOME"
        );

        walletCoreService.credit(
                platformService
                        .getMainAccount()
                        .getWallet()
                        .getId(),
                platformShare,
                "PLATFORM_COMMISSION"
        );
    }

}