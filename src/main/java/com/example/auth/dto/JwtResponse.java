package com.example.auth.dto;

public class JwtResponse {
    private String token;

    private AccountDto account;

    public JwtResponse(String token, AccountDto account) {
        this.token = token;
        this.account = account;
    }

    public JwtResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public AccountDto getAccount() {
        return account;
    }

    public void setAccount(AccountDto account) {
        this.account = account;
    }

}
