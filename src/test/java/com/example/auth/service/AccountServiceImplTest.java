package com.example.auth.service;

import com.example.auth.dto.AccountDto;
import com.example.auth.dto.RegisterRequest;
import com.example.demo.exception.BadRequestException;
import com.example.auth.mapper.AccountMapper;
import com.example.auth.model.Account;
import com.example.auth.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AccountServiceImplTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AccountServiceImpl accountService;

    private RegisterRequest request;

    private Account account;

    private AccountDto accountDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        request = new RegisterRequest();
        request.setUsername("john");
        request.setEmail("john@example.com");
        request.setPassword("password123");

        account = new Account();
        account.setUsername("john");
        account.setEmail("john@example.com");
        account.setPassword("hashed_password");

        accountDto = new AccountDto();
        accountDto.setId(1L);
        accountDto.setUsername("john");
        accountDto.setEmail("john@example.com");
    }

    @Test
    void register_ShouldCreateAccount_WhenValidRequest() {
        when(accountRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(accountRepository.existsByUsername("john")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashed_password");
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(accountMapper.toDto(any(Account.class))).thenReturn(accountDto);

        AccountDto result = accountService.register(request);

        assertNotNull(result);
        assertEquals("john", result.getUsername());
        assertEquals("john@example.com", result.getEmail());
        verify(passwordEncoder).encode("password123");
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void register_ShouldThrowException_WhenEmailExists() {
        when(accountRepository.existsByEmail("john@example.com")).thenReturn(true);

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> accountService.register(request));

        assertEquals("Un compte existe déjà avec cet email.", exception.getMessage());
        verify(accountRepository, never()).save(any());
    }

    @Test
    void register_ShouldThrowException_WhenUsernameExists() {
        when(accountRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(accountRepository.existsByUsername("john")).thenReturn(true);

        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> accountService.register(request));

        assertEquals("Un compte existe déjà avec ce nom d'utilisateur.", exception.getMessage());
        verify(accountRepository, never()).save(any());
    }
}