package ir.maktabsharif138.home_service_system.controller;

import ir.maktabsharif138.home_service_system.service.facade.EmailVerificationFacadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final EmailVerificationFacadeService emailVerificationFacadeService;

    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {

        emailVerificationFacadeService.verify(token);

        return ResponseEntity.ok("Email verified successfully");
    }
}