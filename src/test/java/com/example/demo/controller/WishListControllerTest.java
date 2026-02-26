package com.example.demo.controller;

import com.example.TestConfig;
import com.example.demo.dto.WishListDto;
import com.example.demo.service.WishListService;
import com.example.demo.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WishListController.class)
@Import({TestConfig.class})
@WithMockUser
class WishListControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WishListService wishListService;

    private WishListDto wishlistDto;

    @BeforeEach
    void setUp() {
        wishlistDto = new WishListDto();
        wishlistDto.setId(1L);
        wishlistDto.setProductIds(Set.of(100L, 101L));
    }

    @Test
    void getWishList_success() throws Exception {
        when(wishListService.getWishList()).thenReturn(wishlistDto);

        mockMvc.perform(get("/v1/wishlists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.productIds").isArray());
    }

    @Test
    void getWishList_notFound() throws Exception {
        when(wishListService.getWishList()).thenThrow(new NotFoundException("WishList not found"));

        mockMvc.perform(get("/v1/wishlists"))
                .andExpect(status().isNotFound());
    }



    @Test
    void createWishList_success() throws Exception {
        when(wishListService.createWishList()).thenReturn(wishlistDto);

        mockMvc.perform(post("/v1/wishlists"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void addProductToWishList_success() throws Exception {
        when(wishListService.addProductToWishList(1L, 100L)).thenReturn(wishlistDto);

        mockMvc.perform(post("/v1/wishlists/1/products/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.productIds").isArray());
    }

    @Test
    void removeProductFromWishList_success() throws Exception {
        when(wishListService.removeProductFromWishList(1L, 100L)).thenReturn(wishlistDto);

        mockMvc.perform(delete("/v1/wishlists/1/products/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void addProductToWishList_notFound() throws Exception {
        when(wishListService.addProductToWishList(anyLong(), anyLong()))
                .thenThrow(new NotFoundException("WishList or Product not found"));

        mockMvc.perform(post("/v1/wishlists/1/products/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void removeProductFromWishList_notFound() throws Exception {
        when(wishListService.removeProductFromWishList(anyLong(), anyLong()))
                .thenThrow(new NotFoundException("WishList or Product not found"));

        mockMvc.perform(delete("/v1/wishlists/1/products/999"))
                .andExpect(status().isNotFound());
    }
}