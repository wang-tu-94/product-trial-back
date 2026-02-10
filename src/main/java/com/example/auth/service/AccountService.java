package com.example.auth.service;

import com.example.auth.dto.AccountDto;
import com.example.auth.dto.RegisterRequest;

public interface AccountService {
    AccountDto register(RegisterRequest request);
}
