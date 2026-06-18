package ir.maktabsharif138.home_service_system.service.core.impl;

import ir.maktabsharif138.home_service_system.entity.Customer;
import ir.maktabsharif138.home_service_system.entity.Payment;
import ir.maktabsharif138.home_service_system.entity.enums.PaymentStatus;
import ir.maktabsharif138.home_service_system.exception.BadRequestException;
import ir.maktabsharif138.home_service_system.exception.NotFoundException;
import ir.maktabsharif138.home_service_system.repository.PaymentRepository;
import ir.maktabsharif138.home_service_system.service.core.CustomerCoreService;
import ir.maktabsharif138.home_service_system.service.core.PaymentCoreService;
import ir.maktabsharif138.home_service_system.service.core.WalletCoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import java.util.UUID;
@Service
@RequiredArgsConstructor
public class PaymentCoreServiceImpl implements PaymentCoreService {

    private final PaymentRepository paymentRepository;
    private final CustomerCoreService customerCoreService;
    private final WalletCoreService walletCoreService;

    private static final int EXPIRATION_MINUTES = 10;


    @Override
    @Transactional
    public Payment createTopUpPayment(Long customerId, BigDecimal amount) {

        validateAmount(amount);

        Customer customer = findCustomer(customerId);

        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(LocalDateTime.now());
        payment.setExpiresAt(LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES));
        payment.setPaymentReference(UUID.randomUUID().toString());
        payment.setCustomer(customer);

        return paymentRepository.save(payment);
    }


    @Override
    @Transactional
    public Payment verifyPayment(Long paymentId) {

        Payment payment = findById(paymentId);

        validateCanBeVerified(payment);

        payment.setStatus(PaymentStatus.SUCCESS);

        Payment saved = paymentRepository.save(payment);

        walletCoreService.credit(
                payment.getCustomer().getWallet().getId(),
                payment.getAmount(),
                "TOP_UP_PAYMENT"
        );

        return saved;
    }


    @Override
    @Transactional
    public void expireOldPayments() {

        List<Payment> expiredPayments =
                paymentRepository.findAllByStatusAndExpiresAtBefore(
                        PaymentStatus.PENDING,
                        LocalDateTime.now()
                );

        expiredPayments.forEach(payment ->
                payment.setStatus(PaymentStatus.EXPIRED)
        );

        paymentRepository.saveAll(expiredPayments);
    }


    @Override
    @Transactional(readOnly = true)
    public Payment findById(Long id) {

        return paymentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("PAYMENT_NOT_FOUND"));
    }


    private void validateAmount(BigDecimal amount) {

        if (amount == null || amount.signum() <= 0) {
            throw new BadRequestException("INVALID_AMOUNT");
        }
    }

    private void validateCanBeVerified(Payment payment) {

        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            throw new BadRequestException("ALREADY_VERIFIED");
        }

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new BadRequestException("INVALID_STATUS");
        }

        if (payment.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("EXPIRED_PAYMENT");
        }
    }

    private Customer findCustomer(Long customerId) {
        return customerCoreService.findById(customerId);
    }
}