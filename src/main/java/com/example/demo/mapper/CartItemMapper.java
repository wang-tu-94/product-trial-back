package com.example.demo.mapper;

import com.example.demo.dto.CartItemDto;
import com.example.demo.model.CartItem;
import com.example.demo.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    CartItemDto toDto(CartItem cartItem);

    @Mapping(source = "productId", target = "product.id")
    CartItem toEntity(CartItemDto cartItemDto);

    default CartItem toEntity(CartItemDto dto, Product product) {
        if (dto == null || product == null) {
            return null;
        }

        CartItem item = toEntity(dto);
        item.setProduct(product);
        item.setUnitPrice(product.getPrice());

        return item;
    }
}
