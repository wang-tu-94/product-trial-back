package com.example.auth.service;

import com.example.auth.config.JwtConfig;
import com.example.auth.dto.JwtResponse;
import com.example.auth.dto.LoginRequest;
import com.example.demo.exception.ForbiddenException;
import com.example.auth.model.Account;
import com.example.auth.repository.AccountRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtConfig jwtConfig;

    @Override
    public JwtResponse login(LoginRequest request) {
        Account account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ForbiddenException("Email ou mot de passe invalide"));

        if (!passwordEncoder.matches(request.getPassword(), account.getPassword())) {
            throw new ForbiddenException("Email ou mot de passe invalide");
        }

        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtConfig.getSecret()));

        String token = Jwts.builder()
                .setSubject(account.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtConfig.getExpirationMs()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return new JwtResponse(token);
    }
}
