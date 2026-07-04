package ir.maktabsharif138.home_service_system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import ir.maktabsharif138.home_service_system.dto.request.ConfirmRechargeRequest;
import ir.maktabsharif138.home_service_system.dto.response.OrderPaymentResponse;
import ir.maktabsharif138.home_service_system.dto.response.PaymentResponse;
import ir.maktabsharif138.home_service_system.service.facade.PaymentFacadeService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
@Validated
@Tag(
        name = "Payment API",
        description = "Payment operations including order payment and wallet recharge"
)
public class PaymentController {

    private final PaymentFacadeService paymentFacadeService;


    @Operation(summary = "Pay order from wallet")
    @PostMapping("/me/orders/{orderId}/pay")
    public ResponseEntity<OrderPaymentResponse> payOrder(
            @PathVariable
            @Positive(message = "Order id must be positive")
            Long orderId
    ) {
        return ResponseEntity.ok(
                paymentFacadeService.payOrder(orderId)
        );
    }

    @Operation(summary = "Create wallet recharge payment (returns payment link)")
    @PostMapping("/me/wallet/recharge")
    public ResponseEntity<PaymentResponse> rechargeWallet(
            @RequestParam
            @Positive(message = "Amount must be positive")
            BigDecimal amount
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentFacadeService.rechargeWallet(amount));
    }

    @Operation(summary = "Confirm wallet recharge after payment page submission")
    @PostMapping("/wallet/recharge/confirm")
    public ResponseEntity<PaymentResponse> confirmRecharge(

            @RequestBody
            ConfirmRechargeRequest request
    ) {
        return ResponseEntity.ok(
                paymentFacadeService.confirmRecharge(request)
        );
    }


    @Operation(summary = "Get payment by id")
    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> getPayment(

            @PathVariable
            @Positive(message = "Payment id must be positive")
            Long paymentId
    ) {
        return ResponseEntity.ok(
                paymentFacadeService.getPayment(paymentId)
        );
    }
}