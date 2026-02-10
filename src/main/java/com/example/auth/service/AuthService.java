package com.example.auth.service;

import com.example.auth.dto.JwtResponse;
import com.example.auth.dto.LoginRequest;

public interface AuthService {
    JwtResponse login(LoginRequest request);
}
