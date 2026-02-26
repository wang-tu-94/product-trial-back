package com.example.demo.controller;

import com.example.demo.dto.WishListDto;
import com.example.demo.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/wishlists")
public class WishListController {
    @Autowired
    private WishListService wishListService;

    @GetMapping
    public ResponseEntity<WishListDto> getWishList() {
        WishListDto dto = wishListService.getWishList();
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<WishListDto> createWishList() {
        WishListDto dto = wishListService.createWishList();
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PostMapping("/{wishlistId}/products/{productId}")
    public ResponseEntity<WishListDto> addProductToWishlist(@PathVariable Long wishlistId, @PathVariable Long productId) {
        WishListDto dto = wishListService.addProductToWishList(wishlistId, productId);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{wishlistId}/products/{productId}")
    public ResponseEntity<WishListDto> removeProductFromWishlist(@PathVariable Long wishlistId, @PathVariable Long productId) {
        WishListDto dto = wishListService.removeProductFromWishList(wishlistId, productId);
        return ResponseEntity.ok(dto);
    }
}
