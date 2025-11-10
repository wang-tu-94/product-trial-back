package com.example.demo.mapper;

import com.example.demo.dto.CartDto;
import com.example.demo.model.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CartItemMapper.class})
public interface CartMapper {
    CartDto toDto(Cart cart);
}