package com.example.auth.service;

import com.example.auth.dto.JwtResponse;
import com.example.auth.dto.ServiceAccountDto;

public interface ServiceAccountService {
    JwtResponse createServiceAccount(ServiceAccountDto request);
}
