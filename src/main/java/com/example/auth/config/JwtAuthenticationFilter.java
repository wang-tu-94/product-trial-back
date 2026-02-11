package com.example.auth.config;

import com.example.auth.model.ServiceAccount;
import com.example.auth.repository.ServiceAccountRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    @Lazy
    private ServiceAccountRepository serviceAccountRepository;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.replace("Bearer ", "").trim();
        try {
            SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtConfig.getSecret()));

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String subject = claims.getSubject();
            String tokenType = claims.get("type", String.class); // Récupération du claim personnalisé
            if (subject != null) {
                if ("SERVICE".equals(tokenType)) {
                    // Logique spécifique aux comptes de service : Check BDD obligatoire
                    Optional<ServiceAccount> serviceAccount = serviceAccountRepository.findByClientName(subject);
                    String signature = token.substring(token.lastIndexOf(".") + 1);

                    if (serviceAccount.isPresent() && serviceAccount.get().getActive() &&
                            passwordEncoder.matches(signature, serviceAccount.get().getApiKeyHashed())) {

                        UsernamePasswordAuthenticationToken serviceAuthToken =
                                new UsernamePasswordAuthenticationToken(
                                    subject,
                                    null,
                                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_SERVICE_ACCOUNT"))
                                );

                        SecurityContextHolder.getContext().setAuthentication(serviceAuthToken);
                    } else {
                        SecurityContextHolder.clearContext();
                    }
                } else {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    subject,
                                    null,
                                    Collections.singletonList(new SimpleGrantedAuthority("USER"))
                            );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

        } catch (Exception e) {
            // Token invalide ou expiré, on ne met pas le contexte
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
