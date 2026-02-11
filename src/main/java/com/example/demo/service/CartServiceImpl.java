package com.example.demo.service;

import com.example.auth.service.CurrentUserService;
import com.example.demo.dto.CartDto;
import com.example.demo.dto.CartItemDto;
import com.example.demo.dto.CartItemUpdateRequest;
import com.example.demo.exception.NotFoundException;
import com.example.demo.mapper.CartItemMapper;
import com.example.demo.mapper.CartMapper;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Product;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private CartItemMapper cartItemMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CurrentUserService currentUserService;

    @Override
    @Transactional
    public CartDto getCart() {
        Cart cart = cartRepository.findByUserId(currentUserService.getCurrentUser().getId())
                .orElseGet(this::createCart);

        CartDto dto = cartMapper.toDto(cart);

        List<Long> productIds = dto.getItems().stream()
                .map(CartItemDto::getProductId)
                .distinct()
                .toList();

        Map<Long, Product> productMap = productRepository.findAllById(productIds).stream()
                .collect(Collectors.toMap(Product::getId, p -> p));

        dto.getItems().forEach(item ->
                item.setProduct(productMapper.toDto(productMap.get(item.getProductId())))
        );

        return dto;
    }

    private Cart createCart() {
        Cart cart = new Cart();
        cart.setUserId(currentUserService.getCurrentUser().getId());

        return cartRepository.save(cart);
    }

    @Override
    @Transactional
    public CartDto addItemToCart(Long cartId, CartItemDto itemDto) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new NotFoundException("Cart not found"));

        Product product = productRepository.findById(itemDto.getProductId())
                .orElseThrow(() -> new NotFoundException("Product not found"));


        cart.getItems().stream()
                .filter(ci -> ci.getProduct().getId().equals(product.getId()))
                .findFirst()
                .ifPresentOrElse(
                        existingItem -> existingItem.setQuantity(existingItem.getQuantity() + itemDto.getQuantity()),
                        () -> cart.addItem(cartItemMapper.toEntity(itemDto, product))
                );

        return cartMapper.toDto(cartRepository.save(cart));
    }

    @Override
    @Transactional
    public CartDto updateItemQuantity(Long cartId, Long cartItemId, CartItemUpdateRequest request) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new NotFoundException("Cart not found"));

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Cart item not found"));

        item.setQuantity(request.getQuantity());

        return cartMapper.toDto(cartRepository.save(cart));
    }

    @Override
    @Transactional
    public CartDto removeItemFromCart(Long cartId, Long cartItemId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new NotFoundException("Cart not found"));

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new NotFoundException("CartItem not found"));

        cart.removeItem(cartItem);

        return cartMapper.toDto(cartRepository.save(cart));
    }

    @Override
    @Transactional
    public CartDto clearCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new NotFoundException("Cart not found"));
        cart.getItems().clear();
        return cartMapper.toDto(cartRepository.save(cart));
    }
}
