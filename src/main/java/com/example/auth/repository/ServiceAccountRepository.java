package com.example.auth.repository;

import com.example.auth.model.ServiceAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceAccountRepository extends JpaRepository<ServiceAccount, Long> {
    Optional<ServiceAccount> findByClientName(String clientName);
    boolean existsByClientName(String clientName);
}
