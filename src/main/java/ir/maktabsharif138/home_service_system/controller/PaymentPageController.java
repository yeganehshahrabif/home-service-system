package ir.maktabsharif138.home_service_system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import ir.maktabsharif138.home_service_system.entity.Payment;
import ir.maktabsharif138.home_service_system.service.core.PaymentCoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/payment")
@Tag(
        name = "Payment Page API",
        description = "Payment Page"
)
public class PaymentPageController {

    private final PaymentCoreService paymentCoreService;

    @Operation(summary = "Payment Page")
    @GetMapping("/{reference}")
    public String paymentPage(@PathVariable String reference) {

        Payment payment =
                paymentCoreService.findByReference(reference);

        return "redirect:/payment.html?paymentId=" + payment.getId();
    }
}