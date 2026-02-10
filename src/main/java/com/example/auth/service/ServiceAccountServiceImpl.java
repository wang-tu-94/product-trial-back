package com.example.auth.service;

import com.example.auth.config.JwtConfig;
import com.example.auth.dto.JwtResponse;
import com.example.auth.dto.ServiceAccountDto;
import com.example.auth.model.ServiceAccount;
import com.example.auth.repository.ServiceAccountRepository;
import com.example.demo.exception.BadRequestException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Service
public class ServiceAccountServiceImpl implements ServiceAccountService {

    private ServiceAccountRepository serviceAccountRepository;

    private PasswordEncoder passwordEncoder;

    private JwtConfig jwtConfig;

    public ServiceAccountServiceImpl(ServiceAccountRepository serviceAccountRepository, PasswordEncoder passwordEncoder, JwtConfig jwtConfig) {
        this.serviceAccountRepository = serviceAccountRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtConfig = jwtConfig;
    }

    @Override
    @Transactional
    public JwtResponse createServiceAccount(ServiceAccountDto request) {
        if (serviceAccountRepository.existsByClientName(request.clientName())) {
            throw new BadRequestException("Un compte existe déjà avec ce nom.");
        }

        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtConfig.getSecret()));

        String token = Jwts.builder()
                .setSubject(request.clientName())
                .setIssuedAt(new Date())
                .setExpiration(null)
                .claim("type", "SERVICE")
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        String signature = token.substring(token.lastIndexOf(".") + 1);

        ServiceAccount serviceAccount = new ServiceAccount();
        serviceAccount.setClientName(request.clientName());
        serviceAccount.setApiKeyHashed(passwordEncoder.encode(signature));
        serviceAccount.setActive(true);

        serviceAccountRepository.save(serviceAccount);

        return new JwtResponse(token);
    }
}
