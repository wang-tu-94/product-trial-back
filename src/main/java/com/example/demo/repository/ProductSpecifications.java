package com.example.demo.repository;

import com.example.demo.dto.ProductFilter;
import com.example.demo.model.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecifications {
    public static Specification<Product> buildSpecification(ProductFilter filter) {
        Specification<Product> spec = (root, query, cb) -> null;

        if (filter.getCategory() != null && !filter.getCategory().isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("category"), filter.getCategory()));
        }

        if (filter.getMinPrice() != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("price"), filter.getMinPrice()));
        }

        if (filter.getMaxPrice() != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("price"), filter.getMaxPrice()));
        }

        if (filter.getInventoryStatus() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("inventoryStatus"), filter.getInventoryStatus()));
        }

        return spec;
    }
}
