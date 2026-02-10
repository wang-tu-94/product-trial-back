package com.example;

import com.example.auth.repository.AccountRepository;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.WishListRepository;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestConfig {
    @Bean
    @Primary
    public AccountRepository accountRepository() { return Mockito.mock(AccountRepository.class); }

    @Bean
    @Primary
    public CartItemRepository cartItemRepository() { return Mockito.mock(CartItemRepository.class); }

    @Bean
    @Primary
    public CartRepository cartRepository() { return Mockito.mock(CartRepository.class); }
    @Bean
    @Primary
    public ProductRepository productRepository() { return Mockito.mock(ProductRepository.class); }

    @Bean
    @Primary
    public WishListRepository wishListRepository() { return Mockito.mock(WishListRepository.class); }
}
