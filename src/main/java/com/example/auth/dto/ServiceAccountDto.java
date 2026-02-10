package com.example.auth.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Date;

public record ServiceAccountDto (
        Long id,
        @NotBlank(message = "Le nom du client est obligatoire")
        String clientName,
        Date createdAt,
        Boolean isActive
) {}
