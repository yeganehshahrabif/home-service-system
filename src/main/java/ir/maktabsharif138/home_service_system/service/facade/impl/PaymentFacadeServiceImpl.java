package ir.maktabsharif138.home_service_system.service.facade.impl;

import ir.maktabsharif138.home_service_system.common.calculator.CommissionCalculator;
import ir.maktabsharif138.home_service_system.dto.request.ConfirmRechargeRequest;
import ir.maktabsharif138.home_service_system.dto.response.OrderPaymentResponse;
import ir.maktabsharif138.home_service_system.dto.response.PaymentResponse;
import ir.maktabsharif138.home_service_system.entity.CustomerOrder;
import ir.maktabsharif138.home_service_system.entity.Payment;
import ir.maktabsharif138.home_service_system.exception.BadRequestException;
import ir.maktabsharif138.home_service_system.mapper.PaymentMapper;
import ir.maktabsharif138.home_service_system.service.core.*;
import ir.maktabsharif138.home_service_system.service.facade.PaymentFacadeService;
import ir.maktabsharif138.home_service_system.service.integration.captcha.CaptchaService;
import ir.maktabsharif138.home_service_system.service.integration.payment.PaymentLinkBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;

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

        BigDecimal amount = order.getFinalPrice();
        BigDecimal expertShare = commissionCalculator.expertShare(amount);
        BigDecimal platformShare = commissionCalculator.platformShare(amount);

        processWalletTransfer(order, expertShare, platformShare);
        orderCoreService.markAsPaid(order);

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
        validateBankInfo(request);
        captchaService.validate(
                request.getCaptchaKey(),
                request.getCaptchaInput()
        );

        Payment payment = paymentCoreService.verifyPayment(request.getPaymentId());

        PaymentResponse response = paymentMapper.toResponse(payment);

        response.setMessage("TOPUP SUCCESS");

        return response;
    }

    private void validateBankInfo(ConfirmRechargeRequest request) {

        if (Objects.isNull(request.getCardNumber()) || !request.getCardNumber().matches("\\d{16}")) {
            throw new BadRequestException("INVALID_CARD_NUMBER");
        }

        if (Objects.isNull(request.getCvv2()) || !request.getCvv2().matches("\\d{3,4}")) {
            throw new BadRequestException("INVALID_CVV2");
        }

        if (Objects.isNull(request.getExpireDate()) || !request.getExpireDate()
                .matches("(0[1-9]|1[0-2])/\\d{2}")) {
            throw new BadRequestException("INVALID_EXPIRE_DATE");
        }

        if (Objects.isNull(request.getPassword()) || request.getPassword().isBlank()) {
            throw new BadRequestException("INVALID_SECOND_PASSWORD");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPayment(Long paymentId) {

        return paymentMapper.toResponse(
                paymentCoreService.findById(paymentId)
        );
    }

    private void processWalletTransfer(CustomerOrder order, BigDecimal expertShare, BigDecimal platformShare) {

        walletCoreService.debit(
                order.getCustomer().getWallet().getId(),
                order.getFinalPrice(),
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