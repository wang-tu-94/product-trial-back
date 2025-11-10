package com.example.demo.service;

import com.example.demo.dto.ProductDto;
import com.example.demo.dto.ProductFilter;
import com.example.demo.exception.NotFoundException;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.model.InventoryStatus;
import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;

    private ProductDto productDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(100.0);
        product.setCategory("Electronics");
        product.setInventoryStatus(InventoryStatus.INSTOCK);

        productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName("Test Product");
        productDto.setPrice(100.0);
        productDto.setCategory("Electronics");
        productDto.setInventoryStatus(InventoryStatus.INSTOCK);
    }

    @Test
    void testGetProductById_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toDto(product)).thenReturn(productDto);

        ProductDto result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals("Test Product", result.getName());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testGetProductById_NotFound() {
        when(productRepository.findById(2L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(NotFoundException.class, () -> productService.getProductById(2L));
        assertEquals("Product not found", exception.getMessage());
    }

    @Test
    void testSearchProducts_Pagination() {
        ProductFilter filter = new ProductFilter();
        filter.setPage(0);
        filter.setSize(10);

        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize(), Sort.by(filter.getSortBy()).ascending());

        when(productRepository.findAll(ArgumentMatchers.<Specification<Product>>any(), eq(pageable))).thenReturn(new PageImpl<>(List.of(product)));
        when(productMapper.toDto(product)).thenReturn(productDto);

        Page<ProductDto> result = productService.searchProducts(filter);

        assertEquals(1, result.getTotalElements());
        assertEquals("Test Product", result.getContent().getFirst().getName());
    }

    @Test
    void testSearchProducts_WithFilter() {
        ProductFilter filter = new ProductFilter();
        filter.setCategory("Electronics");
        filter.setPage(0);
        filter.setSize(10);
        filter.setSortBy("id");
        filter.setSortDirection("asc");

        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize(), Sort.by(filter.getSortBy()).ascending());

        when(productRepository.findAll(ArgumentMatchers.<Specification<Product>>any(), eq(pageable))).thenReturn(new PageImpl<>(List.of(product)));
        when(productMapper.toDto(product)).thenReturn(productDto);

        Page<ProductDto> result = productService.searchProducts(filter);

        assertEquals(1, result.getTotalElements());
        assertEquals("Test Product", result.getContent().getFirst().getName());
    }

    @Test
    void testCreateProduct() {
        when(productMapper.toEntity(productDto)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toDto(product)).thenReturn(productDto);

        ProductDto result = productService.createProduct(productDto);

        assertNotNull(result);
        assertEquals("Test Product", result.getName());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testUpdateProduct_Success() {
        ProductDto updateDto = new ProductDto();
        updateDto.setName("Updated Product");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        doNothing().when(productMapper).updateProductFromDto(updateDto, product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toDto(product)).thenReturn(productDto);

        ProductDto result = productService.updateProduct(1L, updateDto);

        assertNotNull(result);
        verify(productRepository, times(1)).save(product);
        verify(productMapper, times(1)).updateProductFromDto(updateDto, product);
    }

    @Test
    void testDeleteProduct() {
        doNothing().when(productRepository).deleteById(1L);

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).deleteById(1L);
    }
}