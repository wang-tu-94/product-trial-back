package com.example.demo.service;

import com.example.demo.dto.WishListDto;


public interface WishListService {
    WishListDto getWishList();

    WishListDto createWishList();

    WishListDto addProductToWishList(Long wishlistId, Long productId);

    WishListDto removeProductFromWishList(Long wishlistId, Long productId);
}
