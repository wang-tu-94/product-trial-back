package com.example.demo.service;

import com.example.demo.dto.ProductDto;
import com.example.demo.dto.ProductFilter;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    ProductDto getProductById(Long id);

    Page<ProductDto> searchProducts(ProductFilter filter);

    ProductDto createProduct(ProductDto productDTO);

    ProductDto updateProduct(Long id, ProductDto productDTO);

    void deleteProduct(Long id);
}
