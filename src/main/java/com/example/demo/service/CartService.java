package com.example.demo.service;

import com.example.demo.dto.CartDto;
import com.example.demo.dto.CartItemDto;
import com.example.demo.dto.CartItemUpdateRequest;

public interface CartService {
    CartDto getCart();

    CartDto addItemToCart(Long cartId, CartItemDto itemDto);

    CartDto updateItemQuantity(Long cartId,Long cartItemId, CartItemUpdateRequest request);

    CartDto removeItemFromCart(Long cartId, Long cartItemId);

    CartDto clearCart(Long cartId);
}
