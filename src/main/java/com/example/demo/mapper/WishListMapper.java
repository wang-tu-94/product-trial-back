package com.example.demo.mapper;

import com.example.demo.dto.WishListDto;
import com.example.demo.model.Product;
import com.example.demo.model.WishList;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface WishListMapper {
    @Mapping(target = "productIds", source = "products")
    WishListDto toDto(WishList wishlist);

    default Set<Long> mapProductsToIds(Set<Product> products) {
        return products.stream().map(Product::getId).collect(Collectors.toSet());
    }
}