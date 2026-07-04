package ir.maktabsharif138.home_service_system.repository;

import ir.maktabsharif138.home_service_system.entity.Payment;
import ir.maktabsharif138.home_service_system.entity.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByPaymentReference(String paymentReference);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            Update Payment p
            set p.status ='EXPIRED'
            where p.status = 'PENDING'
            and p.expiresAt < :now
            
            """)
    int expirePayments(LocalDateTime now);

    Optional<Payment> findByIdAndCustomerId(Long paymentId, Long customerId);

    //
//    Page<Payment> findByCustomerId(Long customerId, Pageable pageable);
//
//    Page<Payment> findByStatus(PaymentStatus status, Pageable pageable);
//
//    List<Payment> findAllByStatusAndExpiresAtBefore(PaymentStatus status, LocalDateTime now);

}