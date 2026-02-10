package com.example.auth.controller;

import com.example.auth.dto.AccountDto;
import com.example.auth.dto.RegisterRequest;
import com.example.auth.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/register")
    public ResponseEntity<AccountDto> register(@Valid @RequestBody RegisterRequest request) {
        AccountDto createdAccount = accountService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
    }
}
