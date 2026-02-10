package com.example.auth.controller;

import com.example.TestConfig;
import com.example.auth.config.JwtConfig;
import com.example.auth.repository.AccountRepository;
import com.example.auth.dto.AccountDto;
import com.example.auth.dto.RegisterRequest;
import com.example.auth.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import({TestConfig.class})
@WebMvcTest(AccountController.class)
class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AccountService accountService;

    @MockitoBean
    private AccountRepository accountRepository;

    @MockitoBean
    private JwtConfig jwtConfig;

    private RegisterRequest request;

    private AccountDto accountDto;

    @BeforeEach
    void setUp() {
        request = new RegisterRequest();
        request.setUsername("john");
        request.setEmail("john@example.com");
        request.setPassword("password123");

        accountDto = new AccountDto();
        accountDto.setId(1L);
        accountDto.setUsername("john");
        accountDto.setEmail("john@example.com");
    }
    @Test
    void register_shouldReturnCreatedAccount_whenValidRequest() throws Exception {
        Mockito.when(accountService.register(any(RegisterRequest.class))).thenReturn(accountDto);

        mockMvc.perform(post("/api/v1/accounts/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("john"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void register_shouldReturnBadRequest_whenInvalidRequest() throws Exception {
        RegisterRequest invalidRequest = new RegisterRequest();

        mockMvc.perform(post("/api/v1/accounts/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}