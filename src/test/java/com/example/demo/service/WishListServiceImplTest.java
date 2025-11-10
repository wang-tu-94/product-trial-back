package com.example.demo.service;

import com.example.demo.dto.WishListDto;
import com.example.demo.exception.NotFoundException;
import com.example.demo.mapper.WishListMapper;
import com.example.demo.model.Account;
import com.example.demo.model.Product;
import com.example.demo.model.WishList;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.WishListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class WishListServiceImplTest {
    @Mock
    private WishListRepository wishlistRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private WishListMapper wishlistMapper;

    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private WishListServiceImpl wishlistService;

    private Account account;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        account = new Account();
        account.setId(1L);
    }

    @Test
    void createWishList_success() {
        Long userId = 1L;
        WishList saved = new WishList();
        saved.setId(1L);
        saved.setUserId(userId);
        WishListDto dto = new WishListDto();
        dto.setId(1L);

        when(currentUserService.getCurrentUser()).thenReturn(account);
        when(wishlistRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(wishlistRepository.save(any(WishList.class))).thenReturn(saved);
        when(wishlistMapper.toDto(saved)).thenReturn(dto);

        WishListDto result = wishlistService.createWishList();

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(wishlistRepository).save(any(WishList.class));
    }

    @Test
    void createWishList_alreadyExists() {
        Long userId = 1L;
        WishList existing = new WishList();
        existing.setId(1L);
        existing.setUserId(userId);

        when(currentUserService.getCurrentUser()).thenReturn(account);
        when(wishlistRepository.findByUserId(userId)).thenReturn(Optional.of(existing));

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> wishlistService.createWishList());

        assertEquals("User already has a wishlist with id: 1", ex.getMessage());
        verify(wishlistRepository, never()).save(any());
    }

    @Test
    void getWishList_success() {
        Long wishlistId = 1L;
        WishList wishlist = new WishList();
        wishlist.setId(wishlistId);
        WishListDto dto = new WishListDto();
        dto.setId(wishlistId);

        when(currentUserService.getCurrentUser()).thenReturn(account);
        when(wishlistRepository.findByUserId(1L)).thenReturn(Optional.of(wishlist));
        when(wishlistMapper.toDto(wishlist)).thenReturn(dto);

        WishListDto result = wishlistService.getWishList();

        assertEquals(wishlistId, result.getId());
    }

    @Test
    void getWishList_notFound() {
        when(currentUserService.getCurrentUser()).thenReturn(account);
        when(wishlistRepository.findByUserId(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> wishlistService.getWishList());
    }

    @Test
    void addProductToWishList_success() {
        Long wishlistId = 1L;
        Long productId = 10L;

        WishList wishlist = new WishList();
        wishlist.setId(wishlistId);
        Product product = new Product();
        product.setId(productId);

        WishList saved = new WishList();
        saved.setId(wishlistId);
        WishListDto dto = new WishListDto();
        dto.setId(wishlistId);

        when(wishlistRepository.findById(wishlistId)).thenReturn(Optional.of(wishlist));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(wishlistRepository.save(wishlist)).thenReturn(saved);
        when(wishlistMapper.toDto(saved)).thenReturn(dto);

        WishListDto result = wishlistService.addProductToWishList(wishlistId, productId);

        assertEquals(wishlistId, result.getId());
        verify(wishlistRepository).save(wishlist);
    }

    @Test
    void addProductToWishList_wishlistNotFound() {
        when(wishlistRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                wishlistService.addProductToWishList(1L, 10L));
    }

    @Test
    void addProductToWishList_productNotFound() {
        WishList wishlist = new WishList();
        wishlist.setId(1L);

        when(wishlistRepository.findById(1L)).thenReturn(Optional.of(wishlist));
        when(productRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                wishlistService.addProductToWishList(1L, 10L));
    }

    @Test
    void removeProductFromWishList_success() {
        Long wishlistId = 1L;
        Long productId = 10L;

        WishList wishlist = new WishList();
        wishlist.setId(wishlistId);
        Product product = new Product();
        product.setId(productId);

        WishList saved = new WishList();
        saved.setId(wishlistId);
        WishListDto dto = new WishListDto();
        dto.setId(wishlistId);

        when(wishlistRepository.findById(wishlistId)).thenReturn(Optional.of(wishlist));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(wishlistRepository.save(wishlist)).thenReturn(saved);
        when(wishlistMapper.toDto(saved)).thenReturn(dto);

        WishListDto result = wishlistService.removeProductFromWishList(wishlistId, productId);

        assertEquals(wishlistId, result.getId());
        verify(wishlistRepository).save(wishlist);
    }

    @Test
    void removeProductFromWishList_wishlistNotFound() {
        when(wishlistRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                wishlistService.removeProductFromWishList(1L, 10L));
    }

    @Test
    void removeProductFromWishList_productNotFound() {
        WishList wishlist = new WishList();
        wishlist.setId(1L);

        when(wishlistRepository.findById(1L)).thenReturn(Optional.of(wishlist));
        when(productRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                wishlistService.removeProductFromWishList(1L, 10L));
    }
}