package com.example.demo.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class CartDto {
    private Long id;

    @NotEmpty(message = "Le panier doit contenir au moins un produit")
    private List<@Valid CartItemDto> items;

    private Double total;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public List<CartItemDto> getItems() { return items; }

    public void setItems(List<CartItemDto> items) {
        this.items = items;
        recalculateTotal();
    }

    public Double getTotal() { return total; }

    private void recalculateTotal() {
        if (items != null) {
            total = items.stream()
                    .mapToDouble(CartItemDto::getTotal)
                    .sum();
        } else {
            total = 0.0;
        }
    }
}
