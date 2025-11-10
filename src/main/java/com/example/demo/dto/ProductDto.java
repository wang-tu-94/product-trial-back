package com.example.demo.dto;

import com.example.demo.model.InventoryStatus;
import com.example.demo.validation.Create;
import jakarta.validation.constraints.*;

public class ProductDto {
    private Long id;

    @NotBlank(groups = Create.class, message = "Le code produit est obligatoire")
    private String code;

    @NotBlank(message = "Le nom est obligatoire")
    private String name;

    private String description;

    @Size(max = 255, message = "Le chemin de l'image ne peut pas dépasser 255 caractères")
    private String image;

    private String category;

    @NotNull(groups = Create.class, message = "Le prix est obligatoire")
    @DecimalMin(value = "0.0", inclusive = true, message = "Le prix doit être positif")
    private Double price;

    @NotNull(groups = Create.class, message = "La quantité est obligatoire")
    @Min(value = 0, message = "La quantité ne peut pas être négative")
    private Integer quantity;

    private String internalReference;

    private Long shellId;

    @NotNull(groups = Create.class, message = "Le statut de stock est obligatoire")
    private InventoryStatus inventoryStatus;

    private Double rating;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getInternalReference() {
        return internalReference;
    }

    public void setInternalReference(String internalReference) {
        this.internalReference = internalReference;
    }

    public Long getShellId() {
        return shellId;
    }

    public void setShellId(Long shellId) {
        this.shellId = shellId;
    }

    public InventoryStatus getInventoryStatus() {
        return inventoryStatus;
    }

    public void setInventoryStatus(InventoryStatus inventoryStatus) {
        this.inventoryStatus = inventoryStatus;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
