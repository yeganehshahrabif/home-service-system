package ir.maktabsharif138.home_service_system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import ir.maktabsharif138.home_service_system.dto.response.EmailVerificationResponse;
import ir.maktabsharif138.home_service_system.service.facade.EmailVerificationFacadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(
        name = "Authentication",
        description = "Authentication and email verification APIs"
)
public class AuthController {

    private final EmailVerificationFacadeService emailVerificationFacadeService;

    @Operation(summary = "Verify email address")
    @GetMapping("/verify-email")
    public ResponseEntity<EmailVerificationResponse> verifyEmail
            (@RequestParam String token) {

        return ResponseEntity.ok(
                emailVerificationFacadeService.verify(token)
        );
    }
}