package com.example.demo.controller;

import com.example.TestConfig;
import com.example.auth.config.JwtConfig;
import com.example.demo.dto.CartDto;
import com.example.demo.dto.CartItemDto;
import com.example.demo.dto.CartItemUpdateRequest;
import com.example.demo.exception.NotFoundException;
import com.example.demo.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
@Import({TestConfig.class})
@WithMockUser
class CartControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CartService cartService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtConfig jwtConfig;

    private CartDto cartDto;

    private CartItemDto cartItemDto;

    @BeforeEach
    void setUp() {
        cartItemDto = new CartItemDto();
        cartItemDto.setProductId(1L);
        cartItemDto.setQuantity(2);
        cartItemDto.setUnitPrice(100.0);
        cartItemDto.setTotal(200.0);

        cartDto = new CartDto();
        cartDto.setId(1L);
        cartDto.setItems(List.of(cartItemDto));
    }

    @Test
    void testGetCart() throws Exception {
        when(cartService.getCart()).thenReturn(cartDto);

        mockMvc.perform(get("/api/v1/carts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.total").value(200.0))
                .andExpect(jsonPath("$.items[0].productId").value(1));

        verify(cartService).getCart();
    }

    @Test
    void testAddItemToCart() throws Exception {
        when(cartService.addItemToCart(eq(1L), any(CartItemDto.class))).thenReturn(cartDto);

        mockMvc.perform(post("/api/v1/carts/1/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartItemDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].productId").value(1))
                .andExpect(jsonPath("$.total").value(200.0));

        verify(cartService).addItemToCart(eq(1L), any(CartItemDto.class));
    }

    @Test
    void testRemoveItemFromCart() throws Exception {
        when(cartService.removeItemFromCart(1L, 1L)).thenReturn(cartDto);

        mockMvc.perform(delete("/api/v1/carts/1/items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].productId").value(1));

        verify(cartService).removeItemFromCart(1L, 1L);
    }

    @Test
    void testClearCart() throws Exception {
        CartDto emptyCart = new CartDto();
        emptyCart.setId(1L);
        emptyCart.setItems(List.of());

        when(cartService.clearCart(1L)).thenReturn(emptyCart);

        mockMvc.perform(delete("/api/v1/carts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isEmpty())
                .andExpect(jsonPath("$.total").value(0.0));

        verify(cartService).clearCart(1L);
    }

    @Test
    void testGetCartNotFound() throws Exception {
        when(cartService.getCart()).thenThrow(new NotFoundException("Cart not found"));

        mockMvc.perform(get("/api/v1/carts"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cart not found"));

        verify(cartService).getCart();
    }

    @Test
    void testAddItemToCartValidationFail() throws Exception {
        CartItemDto invalidItem = new CartItemDto();
        invalidItem.setQuantity(0);
        invalidItem.setProductId(null);

        mockMvc.perform(post("/api/v1/carts/1/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidItem)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.productId").exists())
                .andExpect(jsonPath("$.errors.quantity").exists());
    }

    @Test
    void updateItemQuantity_ShouldReturnOk() throws Exception {
        Long cartId = 1L;
        Long itemId = 10L;

        CartItemUpdateRequest request = new CartItemUpdateRequest(5);

        CartDto cartDto = new CartDto();
        cartDto.setId(cartId);

        when(cartService.updateItemQuantity(eq(cartId), eq(itemId), any(CartItemUpdateRequest.class)))
                .thenReturn(cartDto);

        mockMvc.perform(patch("/api/v1/carts/1/items/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cartId));
    }

    @Test
    void updateItemQuantity_InvalidQuantity_ShouldReturnBadRequest() throws Exception {
        CartItemUpdateRequest request = new CartItemUpdateRequest(-3);

        mockMvc.perform(patch("/api/v1/carts/1/items/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateItemQuantity_ItemNotFound_ShouldReturnNotFound() throws Exception {
        Long cartId = 1L;
        Long itemId = 999L;

        CartItemUpdateRequest request = new CartItemUpdateRequest(3);

        when(cartService.updateItemQuantity(eq(cartId), eq(itemId), any(CartItemUpdateRequest.class)))
                .thenThrow(new NotFoundException("Cart item not found"));

        mockMvc.perform(patch("/api/v1/carts/1/items/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}