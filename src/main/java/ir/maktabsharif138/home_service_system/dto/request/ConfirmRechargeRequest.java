package ir.maktabsharif138.home_service_system.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConfirmRechargeRequest {

    @NotNull
    private Long paymentId;

    @NotBlank
    private String captchaKey;

    @NotBlank
    private String captchaInput;

    @NotBlank
    private String cardNumber;

    @NotBlank
    private String cvv2;

    @NotBlank
    private String expireDate;

    @NotBlank
    private String password;
}