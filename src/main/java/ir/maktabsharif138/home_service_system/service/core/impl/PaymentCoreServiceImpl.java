package ir.maktabsharif138.home_service_system.service.core.impl;

import ir.maktabsharif138.home_service_system.entity.Customer;
import ir.maktabsharif138.home_service_system.entity.Payment;
import ir.maktabsharif138.home_service_system.entity.enums.PaymentStatus;
import ir.maktabsharif138.home_service_system.exception.BadRequestException;
import ir.maktabsharif138.home_service_system.exception.NotFoundException;
import ir.maktabsharif138.home_service_system.repository.PaymentRepository;
import ir.maktabsharif138.home_service_system.service.core.CustomerCoreService;
import ir.maktabsharif138.home_service_system.service.core.PaymentCoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentCoreServiceImpl implements PaymentCoreService {

    private final PaymentRepository paymentRepository;
    private final CustomerCoreService customerCoreService;

    @Override
    @Transactional
    public Payment createTopUpPayment(Long customerId, BigDecimal amount) {

        checkAmount(amount);

        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(LocalDateTime.now());
        payment.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        payment.setPaymentReference(UUID.randomUUID().toString());
        payment.setCustomer(findCustomer(customerId));

        return paymentRepository.save(payment);
    }

    @Override
    @Transactional
    public Payment verifyPayment(Long paymentId) {

        Payment payment = findById(paymentId);
        checkPaymentAlreadyProcessed(payment);
        checkPaymentForVerification(payment);

        payment.setStatus(PaymentStatus.SUCCESS);

        return paymentRepository.save(payment);
    }

    @Override
    @Transactional
    public void expireOldPayments() {

        List<Payment> expiredPayments =
                paymentRepository.findAllByStatusAndExpiresAtBefore(
                        PaymentStatus.PENDING,
                        LocalDateTime.now()
                );

        expiredPayments.forEach(this::expirePayment);
    }

    private void expirePayment(Payment payment) {

        if (!PaymentStatus.PENDING.equals(payment.getStatus())) {
            return;
        }

        payment.setStatus(PaymentStatus.EXPIRED);
    }

    @Override
    @Transactional(readOnly = true)
    public Payment findById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("PAYMENT_NOT_FOUND"));
    }


    private Customer findCustomer(Long id) {
        return customerCoreService.findById(id);
    }

    private void checkAmount(BigDecimal amount) {
        if (Objects.isNull(amount) || amount.signum() <= 0)
            throw new BadRequestException("INVALID_AMOUNT");
    }

    private void checkPaymentAlreadyProcessed(Payment payment) {
        if (PaymentStatus.SUCCESS.equals(payment.getStatus())) {
            throw new BadRequestException("ALREADY_PAID");
        }
    }

    private void checkPaymentForVerification(Payment payment) {

        if (!PaymentStatus.PENDING.equals(payment.getStatus()))
            throw new BadRequestException("INVALID_STATUS");

        if (payment.getExpiresAt().isBefore(LocalDateTime.now()))
            throw new BadRequestException("EXPIRED");
    }
}