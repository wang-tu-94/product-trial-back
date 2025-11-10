package com.example.demo.dto;

import com.example.demo.model.InventoryStatus;
import jakarta.validation.constraints.DecimalMin;

public class ProductFilter {
    private String category;

    @DecimalMin(value = "0.0", message = "minPrice doit être positif")
    private Double minPrice;

    @DecimalMin(value = "0.0", message = "minPrice doit être positif")
    private Double maxPrice;

    private InventoryStatus inventoryStatus;

    private int page = 0;

    private int size = 10;

    private String sortBy = "id";

    private String sortDirection = "asc";


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public InventoryStatus getInventoryStatus() {
        return inventoryStatus;
    }

    public void setInventoryStatus(InventoryStatus inventoryStatus) {
        this.inventoryStatus = inventoryStatus;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }
}
