package ir.maktabsharif138.home_service_system.service.facade.impl;

import ir.maktabsharif138.home_service_system.common.calculator.CommissionCalculator;
import ir.maktabsharif138.home_service_system.dto.response.PaymentResponse;
import ir.maktabsharif138.home_service_system.entity.CustomerOrder;
import ir.maktabsharif138.home_service_system.entity.Payment;
import ir.maktabsharif138.home_service_system.entity.enums.OrderStatus;
import ir.maktabsharif138.home_service_system.exception.BadRequestException;
import ir.maktabsharif138.home_service_system.mapper.PaymentMapper;
import ir.maktabsharif138.home_service_system.service.core.CustomerOrderCoreService;
import ir.maktabsharif138.home_service_system.service.core.PaymentCoreService;
import ir.maktabsharif138.home_service_system.service.core.WalletCoreService;
import ir.maktabsharif138.home_service_system.service.facade.PaymentFacadeService;
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
    private final PaymentCoreService paymentCoreService;

    private final PaymentMapper paymentMapper;
    private final PaymentLinkBuilder paymentLinkBuilder;
    private final CommissionCalculator commissionCalculator;

    @Override
    @Transactional
    public PaymentResponse payOrder(Long customerId, Long orderId) {

        CustomerOrder order = getPayableOrder(customerId, orderId);

        processWalletTransfer(order);

        markOrderAsPaid(order);

        return buildPaymentResponse(order);
    }

    @Override
    public PaymentResponse createTopUpRequest(Long customerId, BigDecimal amount) {

        Payment payment = paymentCoreService.createTopUpPayment(customerId, amount);

        PaymentResponse response = paymentMapper.toResponse(payment);

        response.setMessage("topup created");
        response.setPaymentLink(paymentLinkBuilder.build(payment.getPaymentReference()));

        return response;
    }

    @Override
    @Transactional
    public PaymentResponse verifyPayment(Long paymentId) {

        Payment payment = paymentCoreService.verifyPayment(paymentId);

        walletCoreService.credit(
                payment.getCustomer().getWallet().getId(),
                payment.getAmount(),
                "topup"
        );

        return PaymentResponse.builder()
                .message("topup success")
                .amount(payment.getAmount())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPayment(Long paymentId) {

        return paymentMapper.toResponse(
                paymentCoreService.findById(paymentId)
        );
    }

    private CustomerOrder getPayableOrder(Long customerId, Long orderId) {

        CustomerOrder order = orderCoreService.findCustomerOrder(customerId, orderId);

        orderCoreService.validateCompleted(order);

        ensureNotAlreadyPaid(order);

        return order;
    }

    private void processWalletTransfer(CustomerOrder order) {

        BigDecimal amount = order.getFinalPrice();

        walletCoreService.debit(
                order.getCustomer().getWallet().getId(),
                amount,
                "order payment"
        );

        walletCoreService.credit(
                order.getAcceptedOffer().getExpert().getWallet().getId(),
                commissionCalculator.expertShare(amount),
                "order income"
        );
    }

    private void markOrderAsPaid(CustomerOrder order) {
        orderCoreService.markAsPaid(order);
    }

    private void ensureNotAlreadyPaid(CustomerOrder order) {

        if (OrderStatus.PAID.equals(order.getOrderStatus())) {
            throw new BadRequestException("already paid");
        }
    }

    private PaymentResponse buildPaymentResponse(CustomerOrder order) {

        return PaymentResponse.builder()
                .message("payment success")
                .amount(order.getFinalPrice())
                .build();
    }
}