package ir.maktabsharif138.home_service_system.service.facade;

import ir.maktabsharif138.home_service_system.dto.response.WalletTransactionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface WalletFacadeService {

    BigDecimal getBalanceForCustomer(Long customerId);

    BigDecimal getBalanceForExpert(Long expertId);

    Page<WalletTransactionResponse> getCustomerTransactions(Long customerId, Pageable pageable);

    Page<WalletTransactionResponse> getExpertTransactions(Long expertId, Pageable pageable);
}