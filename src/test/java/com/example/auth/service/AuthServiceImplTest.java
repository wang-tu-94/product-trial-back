package com.example.auth.service;

import com.example.auth.config.JwtConfig;
import com.example.auth.dto.JwtResponse;
import com.example.auth.dto.LoginRequest;
import com.example.auth.mapper.AccountMapper;
import com.example.demo.exception.ForbiddenException;
import com.example.auth.model.Account;
import com.example.auth.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthServiceImplTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtConfig jwtConfig;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AuthServiceImpl authService;

    private LoginRequest request;

    private Account account;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        request = new LoginRequest();
        request.setEmail("john@email.com");
        request.setPassword("password123");

        account = new Account();
        account.setEmail("john@email.com");
        account.setPassword("hashedPassword");

        when(jwtConfig.getSecret()).thenReturn("8w9eK7sVh3FQ2mJ6lP0rXyZtN1bQ4uDkFvHsT2cW+Eo=");
        when(jwtConfig.getExpirationMs()).thenReturn(3600000L);
    }

    @Test
    void login_ShouldReturnToken_WhenValidCredentials() {
        when(accountRepository.findByEmail("john@email.com")).thenReturn(Optional.of(account));
        when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(true);

        JwtResponse response = authService.login(request);

        assertNotNull(response);
        assertNotNull(response.getToken());
        verify(accountRepository).findByEmail("john@email.com");
        verify(passwordEncoder).matches("password123", "hashedPassword");
    }

    @Test
    void login_ShouldThrowException_WhenUsernameNotFound() {
        LoginRequest request = new LoginRequest();
        request.setEmail("unknown@email.com");

        when(accountRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        ForbiddenException exception = assertThrows(ForbiddenException.class,
                () -> authService.login(request));
        assertEquals("Email ou mot de passe invalide", exception.getMessage());
    }

    @Test
    void login_ShouldThrowException_WhenPasswordDoesNotMatch() {
        request.setPassword("wrongPassword");

        when(accountRepository.findByEmail("john@email.com")).thenReturn(Optional.of(account));
        when(passwordEncoder.matches("wrongPassword", "hashedPassword")).thenReturn(false);

        ForbiddenException exception = assertThrows(ForbiddenException.class,
                () -> authService.login(request));
        assertEquals("Email ou mot de passe invalide", exception.getMessage());
    }
}