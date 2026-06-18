package ir.maktabsharif138.home_service_system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import ir.maktabsharif138.home_service_system.dto.response.CaptchaResponse;
import ir.maktabsharif138.home_service_system.service.integration.captcha.CaptchaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/captcha")
@RequiredArgsConstructor
@Tag(name = "Captcha API", description = "Captcha generation and validation support")
public class CaptchaController {

    private final CaptchaService captchaService;

    @Operation(summary = "Generate captcha image")
    @GetMapping
    public ResponseEntity<CaptchaResponse> generateCaptcha() {

        return ResponseEntity.ok(
                captchaService.generateCaptcha()
        );
    }
}