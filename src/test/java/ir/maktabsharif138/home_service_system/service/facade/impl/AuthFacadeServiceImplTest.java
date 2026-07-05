package ir.maktabsharif138.home_service_system.service.facade.impl;

import ir.maktabsharif138.home_service_system.dto.request.LoginRequest;
import ir.maktabsharif138.home_service_system.dto.response.LoginResponse;
import ir.maktabsharif138.home_service_system.security.jwt.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthFacadeServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthFacadeServiceImpl authFacadeService;

    @Test
    void login_shouldReturnTokenAndEmail() {

        LoginRequest request =
                new LoginRequest(
                        "test@gmail.com",
                        "1234"
                );

        UserDetails userDetails =
                new User(
                        "test@gmail.com",
                        "encoded-password",
                        Collections.emptyList()
                );

        when(authenticationManager.authenticate(
                any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(authentication.getPrincipal())
                .thenReturn(userDetails);

        when(jwtService.generateToken(userDetails))
                .thenReturn("jwt-token");

        LoginResponse result =
                authFacadeService.login(request);

        assertNotNull(result);
        assertEquals("jwt-token", result.getToken());
        assertEquals("test@gmail.com", result.getEmail());

        verify(authenticationManager)
                .authenticate(any(
                        UsernamePasswordAuthenticationToken.class));

        verify(jwtService)
                .generateToken(userDetails);
    }

    @Test
    void login_shouldThrowException_whenAuthenticationFails() {

        LoginRequest request =
                new LoginRequest(
                        "wrong@gmail.com",
                        "wrong"
                );

        when(authenticationManager.authenticate(
                any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(
                        new BadCredentialsException(
                                "Bad credentials"
                        )
                );

        assertThrows(
                BadCredentialsException.class,
                () -> authFacadeService.login(request)
        );

        verify(jwtService, never())
                .generateToken(any());
    }
}