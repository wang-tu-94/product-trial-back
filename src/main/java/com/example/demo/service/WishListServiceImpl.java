package com.example.demo.service;

import com.example.demo.dto.WishListDto;
import com.example.demo.exception.NotFoundException;
import com.example.demo.mapper.WishListMapper;
import com.example.demo.model.Product;
import com.example.demo.model.WishList;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.WishListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishListServiceImpl implements WishListService {
    @Autowired
    private WishListRepository wishListRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WishListMapper wishListMapper;

    @Autowired
    private CurrentUserService currentUserService;

    @Override
    @Transactional(readOnly = true)
    public WishListDto getWishList() {
        WishList wishlist = wishListRepository.findByUserId(currentUserService.getCurrentUser().getId())
                .orElseThrow(() -> new NotFoundException("WishList not found"));
        return wishListMapper.toDto(wishlist);
    }


    @Override
    @Transactional
    public WishListDto createWishList() {
        Long userId = currentUserService.getCurrentUser().getId();

        wishListRepository.findByUserId(userId).ifPresent(w -> {
            throw new IllegalStateException("User already has a wishlist with id: " + w.getId());
        });

        WishList wishlist = new WishList();
        wishlist.setUserId(userId);

        return wishListMapper.toDto(wishListRepository.save(wishlist));
    }

    @Override
    @Transactional
    public WishListDto addProductToWishList(Long wishlistId, Long productId) {
        WishList wishlist = wishListRepository.findById(wishlistId)
                .orElseThrow(() -> new NotFoundException("WishList not found with id: " + wishlistId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + productId));

        wishlist.addProduct(product);
        WishList saved = wishListRepository.save(wishlist);
        return wishListMapper.toDto(saved);
    }

    @Override
    @Transactional
    public WishListDto removeProductFromWishList(Long wishlistId, Long productId) {
        WishList wishlist = wishListRepository.findById(wishlistId)
                .orElseThrow(() -> new NotFoundException("WishList not found with id: " + wishlistId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + productId));

        wishlist.removeProduct(product);
        WishList saved = wishListRepository.save(wishlist);
        return wishListMapper.toDto(saved);
    }
}
