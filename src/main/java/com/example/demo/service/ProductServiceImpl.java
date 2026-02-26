package com.example.demo.service;

import com.example.demo.dto.ProductDto;
import com.example.demo.dto.ProductFilter;
import com.example.demo.exception.NotFoundException;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ProductSpecifications;
import com.myproject.log.annotation.Loggable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Override
    @Transactional(readOnly = true)
    public ProductDto getProductById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Product not found"));
    }

    @Override
    @Transactional(readOnly = true)
    @Loggable
    public Page<ProductDto> searchProducts(ProductFilter filter) {
        Specification<Product> spec = ProductSpecifications.buildSpecification(filter);

        Sort sort = Sort.by(filter.getSortDirection().equalsIgnoreCase("desc")
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC,
                filter.getSortBy());

        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize(), sort);

        Page<Product> productPage = productRepository.findAll(spec, pageable);

        return productPage.map(productMapper::toDto);
    }

    @Override
    @Transactional
    public ProductDto createProduct(ProductDto productDto) {
        Product product = productMapper.toEntity(productDto);
        return productMapper.toDto(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductDto updateProduct(Long id, ProductDto productDto) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        productMapper.updateProductFromDto(productDto, existing);

        return productMapper.toDto(productRepository.save(existing));
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
