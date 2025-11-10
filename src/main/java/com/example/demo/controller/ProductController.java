package com.example.demo.controller;

import com.example.demo.dto.ProductDto;
import com.example.demo.dto.ProductFilter;
import com.example.demo.exception.ForbiddenException;
import com.example.demo.service.ProductService;
import com.example.demo.validation.Create;
import com.example.demo.validation.Update;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    private static final String ADMIN_EMAIL = "admin@admin.com";

    @GetMapping("/{id}")
    ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping
    ResponseEntity<Page<ProductDto>> searchProducts(@Valid ProductFilter filter) {
        return ResponseEntity.ok(productService.searchProducts(filter));
    }

    @PostMapping
    ResponseEntity<ProductDto> createProduct(@RequestBody @Validated(Create.class) ProductDto productDto, Authentication authentication) {
        checkAdmin(authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(productDto));
    }

    @PutMapping("/{id}")
    ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody @Validated(Update.class) ProductDto productDto, Authentication authentication) {
        checkAdmin(authentication);
        return ResponseEntity.ok(productService.updateProduct(id, productDto));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteProduct(@PathVariable Long id, Authentication authentication) {
        checkAdmin(authentication);
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    private void checkAdmin(Authentication authentication) {
        if (authentication == null || !ADMIN_EMAIL.equals(authentication.getName())) {
            throw new ForbiddenException("Accès refusé : uniquement admin");
        }
    }
}
