package com.example.demo.service;

import com.example.auth.service.CurrentUserService;
import com.example.demo.dto.CartDto;
import com.example.demo.dto.CartItemDto;
import com.example.demo.dto.CartItemUpdateRequest;
import com.example.demo.exception.NotFoundException;
import com.example.demo.mapper.CartItemMapper;
import com.example.demo.mapper.CartMapper;
import com.example.auth.model.Account;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Product;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceImplTest {
    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private CartMapper cartMapper;

    @Mock
    private CartItemMapper cartItemMapper;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private CartServiceImpl cartService;

    private Cart cart;
    private Product product;
    private CartItem cartItem;
    private CartDto cartDto;
    private CartItemDto cartItemDto;
    private Account account;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        product = new Product();
        product.setId(1L);
        product.setPrice(100.0);

        cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setProduct(product);
        cartItem.setQuantity(2);
        cartItem.setUnitPrice(product.getPrice());

        cart = new Cart();
        cart.setId(1L);

        cartDto = new CartDto();
        cartDto.setId(1L);

        cartItemDto = new CartItemDto();
        cartItemDto.setProductId(1L);
        cartItemDto.setQuantity(2);

        account = new Account();
        account.setId(1L);
    }

    @Test
    void testGetCartById_Success() {
        when(currentUserService.getCurrentUser()).thenReturn(account);
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(cartMapper.toDto(cart)).thenReturn(cartDto);

        CartDto result = cartService.getCart();

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(cartRepository, times(1)).findByUserId(1L);
    }

    @Test
    void testGetCartById_NotFound() {
        when(currentUserService.getCurrentUser()).thenReturn(account);
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(cartMapper.toDto(cart)).thenReturn(cartDto);

        CartDto result = cartService.getCart();

        // THEN
        assertNotNull(result);
        assertEquals(1, result.getId());

        // Vérifications des interactions
        verify(cartRepository).findByUserId(1L);
        verify(cartRepository).save(argThat(cart -> cart.getUserId().equals(1L)));
        verify(cartMapper).toDto(cart);
    }

    @Test
    void addItemToCart_NewItem_ShouldAdd() {
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartItemMapper.toEntity(cartItemDto, product)).thenReturn(cartItem);
        when(cartRepository.save(cart)).thenReturn(cart);
        when(cartMapper.toDto(cart)).thenReturn(new CartDto());

        CartDto result = cartService.addItemToCart(1L, cartItemDto);

        assertEquals(1, cart.getItems().size());
        assertEquals(2, cart.getItems().getFirst().getQuantity());
        verify(cartRepository).save(cart);
    }

    @Test
    void addItemToCart_ItemAlreadyExists_ShouldIncrementQuantity() {
        cart.addItem(cartItem); // Cart contient déjà le produit

        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartRepository.save(cart)).thenReturn(cart);
        when(cartMapper.toDto(cart)).thenReturn(new CartDto());

        CartDto result = cartService.addItemToCart(1L, cartItemDto);

        // La quantité doit être incrémentée
        assertEquals(1, cart.getItems().size());
        assertEquals(4, cart.getItems().get(0).getQuantity());
        verify(cartRepository).save(cart);
    }

    @Test
    void updateItemQuantity_ShouldUpdate() {
        cart.addItem(cartItem);

        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(cartRepository.save(cart)).thenReturn(cart);
        when(cartMapper.toDto(cart)).thenReturn(new CartDto());

        CartDto result = cartService.updateItemQuantity(1L, 1L, new CartItemUpdateRequest(7));

        assertEquals(7, cart.getItems().get(0).getQuantity());
        verify(cartRepository).save(cart);
    }

    @Test
    void updateItemQuantity_ItemNotFound_ShouldThrow() {
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));

        assertThrows(NotFoundException.class, () -> cartService.updateItemQuantity(1L, 999L, new CartItemUpdateRequest(5)));
    }

    @Test
    void testRemoveItemFromCart() {
        cart.addItem(cartItem);
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findById(1L)).thenReturn(Optional.of(cartItem));
        when(cartRepository.save(cart)).thenReturn(cart);
        when(cartMapper.toDto(cart)).thenReturn(cartDto);

        CartDto result = cartService.removeItemFromCart(1L, 1L);

        assertNotNull(result);
        assertFalse(cart.getItems().contains(cartItem));
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void testClearCart() {
        cart.addItem(cartItem);
        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        when(cartRepository.save(cart)).thenReturn(cart);
        when(cartMapper.toDto(cart)).thenReturn(cartDto);

        CartDto result = cartService.clearCart(1L);

        assertNotNull(result);
        assertTrue(cart.getItems().isEmpty());
        verify(cartRepository, times(1)).save(cart);
    }
}