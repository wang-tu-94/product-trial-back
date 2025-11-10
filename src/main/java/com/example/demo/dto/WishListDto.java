package com.example.demo.dto;

import java.util.HashSet;
import java.util.Set;

public class WishListDto {
    private Long id;

    private Set<Long> productIds = new HashSet<>();

    public WishListDto() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Long> getProductIds() {
        return productIds;
    }

    public void setProductIds(Set<Long> productIds) {
        this.productIds = productIds;
    }
}
