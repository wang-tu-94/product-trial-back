package com.example.demo.service;

import com.example.demo.dto.AccountDto;
import com.example.demo.dto.RegisterRequest;

public interface AccountService {
    AccountDto register(RegisterRequest request);
}
