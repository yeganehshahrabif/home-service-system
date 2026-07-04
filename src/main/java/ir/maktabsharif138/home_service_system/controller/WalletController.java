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
import org.springframework.security.access.prepost.PreAuthorize;
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


    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Get customer wallet balance")
    @GetMapping("/customers/me/balance")
    public ResponseEntity<BalanceResponse> getCustomerBalance() {

        return ResponseEntity.ok(
                        walletFacadeService.getBalanceForCustomer());
    }

    @PreAuthorize("hasRole('EXPERT')")
    @Operation(summary = "Get expert wallet balance")
    @GetMapping("/experts/me/balance")
    public ResponseEntity<BalanceResponse> getExpertBalance() {

        return ResponseEntity.ok(walletFacadeService.getBalanceForExpert());
    }
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Get customer wallet transactions")
    @GetMapping("/customers/me/transactions")
    public ResponseEntity<Page<WalletTransactionResponse>> getCustomerTransactions(

            Pageable pageable
    ) {

        return ResponseEntity.ok(
                walletFacadeService.getCustomerTransactions(
                        pageable
                )
        );
    }

    @PreAuthorize("hasRole('EXPERT')")
    @Operation(summary = "Get expert wallet transactions")
    @GetMapping("/experts/me/transactions")
    public ResponseEntity<Page<WalletTransactionResponse>> getExpertTransactions(

            Pageable pageable
    ) {

        return ResponseEntity.ok(
                walletFacadeService.getExpertTransactions(
                        pageable
                )
        );
    }
}