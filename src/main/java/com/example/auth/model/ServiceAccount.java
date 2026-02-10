package com.example.auth.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "service_accounts")
public class ServiceAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_name", nullable = false, unique = true)
    private String clientName;

    @Column(name = "api_key_hashed", nullable = false, unique = true)
    private String apiKeyHashed;

    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @Column(name = "last_used_at")
    private Date lastUsedAt;

    @Column(name = "is_active")
    private Boolean isActive;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getApiKeyHashed() {
        return apiKeyHashed;
    }

    public void setApiKeyHashed(String apiKeyHashed) {
        this.apiKeyHashed = apiKeyHashed;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getLastUsedAt() {
        return lastUsedAt;
    }

    public void setLastUsedAt(Date lastUsedAt) {
        this.lastUsedAt = lastUsedAt;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        lastUsedAt = createdAt;
    }
}
