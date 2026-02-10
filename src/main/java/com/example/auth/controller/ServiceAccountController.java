package com.example.auth.controller;

import com.example.auth.dto.JwtResponse;
import com.example.auth.dto.ServiceAccountDto;
import com.example.auth.service.ServiceAccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/service-accounts")
public class ServiceAccountController {
    @Autowired
    private ServiceAccountService serviceAccountService;

    @PostMapping
    public ResponseEntity<JwtResponse> createServiceAccount(@Valid @RequestBody ServiceAccountDto serviceAccountDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(serviceAccountService.createServiceAccount(serviceAccountDto));
    }
}
