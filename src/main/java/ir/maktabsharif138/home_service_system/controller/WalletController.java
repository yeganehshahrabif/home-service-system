package ir.maktabsharif138.home_service_system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import ir.maktabsharif138.home_service_system.dto.response.BalanceResponse;
import ir.maktabsharif138.home_service_system.dto.response.WalletTransactionResponse;
import ir.maktabsharif138.home_service_system.service.core.CustomerCoreService;
import ir.maktabsharif138.home_service_system.service.core.ExpertCoreService;
import ir.maktabsharif138.home_service_system.service.facade.WalletFacadeService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Wallet API",
        description = "Wallet balance and transaction history operations"
)
public class WalletController {

    private final WalletFacadeService walletFacadeService;

    @Operation(summary = "Get customer wallet balance")
    @GetMapping("/customers/{customerId}/balance")
    public ResponseEntity<BalanceResponse> getCustomerBalance(

            @PathVariable
            @Positive(message = "Customer id must be positive")
            Long customerId
    ) {

        return ResponseEntity.ok(
                        walletFacadeService.getBalanceForCustomer(customerId));
    }

    @Operation(summary = "Get expert wallet balance")
    @GetMapping("/experts/{expertId}/balance")
    public ResponseEntity<BalanceResponse> getExpertBalance(

            @PathVariable
            @Positive(message = "Expert id must be positive")
            Long expertId
    ) {

        return ResponseEntity.ok(walletFacadeService.getBalanceForExpert(expertId));
    }

    @Operation(summary = "Get customer wallet transactions")
    @GetMapping("/customers/{customerId}/transactions")
    public ResponseEntity<Page<WalletTransactionResponse>> getCustomerTransactions(

            @PathVariable
            @Positive(message = "Customer id must be positive")
            Long customerId,

            Pageable pageable
    ) {

        return ResponseEntity.ok(
                walletFacadeService.getCustomerTransactions(
                        customerId,
                        pageable
                )
        );
    }

    @Operation(summary = "Get expert wallet transactions")
    @GetMapping("/experts/{expertId}/transactions")
    public ResponseEntity<Page<WalletTransactionResponse>> getExpertTransactions(

            @PathVariable
            @Positive(message = "Expert id must be positive")
            Long expertId,

            Pageable pageable
    ) {

        return ResponseEntity.ok(
                walletFacadeService.getExpertTransactions(
                        expertId,
                        pageable
                )
        );
    }
}