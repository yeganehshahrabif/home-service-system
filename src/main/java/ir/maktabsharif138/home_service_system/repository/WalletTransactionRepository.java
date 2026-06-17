package ir.maktabsharif138.home_service_system.repository;

import ir.maktabsharif138.home_service_system.entity.WalletTransaction;
import ir.maktabsharif138.home_service_system.entity.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, Long> {

    Page<WalletTransaction> findByWalletId(Long walletId, Pageable pageable);

    Page<WalletTransaction> findByWalletIdAndType(Long walletId, TransactionType type, Pageable pageable);

    @Query("""
            SELECT
            SUM(CASE 
                WHEN t.type = 'DEPOSIT' THEN t.amount
                ELSE -t.amount
            END)
            FROM WalletTransaction t
            WHERE t.wallet.id = :walletId
            """)
    BigDecimal calculateBalance(@Param("walletId") Long walletId);

}