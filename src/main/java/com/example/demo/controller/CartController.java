package com.example.demo.controller;

import com.example.demo.dto.CartDto;
import com.example.demo.dto.CartItemDto;
import com.example.demo.dto.CartItemUpdateRequest;
import com.example.demo.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/carts")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<CartDto> getCart() {
        CartDto cart = cartService.getCart();
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<CartDto> addItemToCart(@PathVariable Long id, @Valid @RequestBody CartItemDto itemDto) {
        CartDto cart = cartService.addItemToCart(id, itemDto);
        return ResponseEntity.ok(cart);
    }

    @PatchMapping("/{cartId}/items/{cartItemId}")
    public ResponseEntity<CartDto> updateItemQuantity(@PathVariable Long cartId, @PathVariable Long cartItemId, @Valid @RequestBody CartItemUpdateRequest request) {
        CartDto cart = cartService.updateItemQuantity(cartId, cartItemId, request);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/{cartId}/items/{cartItemId}")
    public ResponseEntity<CartDto> removeItemFromCart(@PathVariable Long cartId, @PathVariable Long cartItemId) {
        CartDto cart = cartService.removeItemFromCart(cartId, cartItemId);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CartDto> clearCart(@PathVariable Long id) {
        CartDto cart = cartService.clearCart(id);
        return ResponseEntity.ok(cart);
    }
}
