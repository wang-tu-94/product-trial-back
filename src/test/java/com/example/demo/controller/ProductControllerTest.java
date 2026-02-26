package com.example.demo.controller;

import com.example.TestConfig;
import com.example.demo.dto.ProductDto;
import com.example.demo.dto.ProductFilter;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.InventoryStatus;
import com.example.demo.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@Import({TestConfig.class})
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductDto productDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // initialise les mocks Mockito

        productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setCode("123");
        productDto.setQuantity(1);
        productDto.setName("Laptop");
        productDto.setCategory("Electronics");
        productDto.setInventoryStatus(InventoryStatus.INSTOCK);
        productDto.setPrice(1200.0);
    }

    @Test
    @WithMockUser
    void testGetProductById() throws Exception {
        when(productService.getProductById(1L)).thenReturn(productDto);

        mockMvc.perform(get("/v1/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Laptop"));

        verify(productService).getProductById(1L);
    }

    @Test
    @WithMockUser
    void testSearchProducts() throws Exception {
        Page<ProductDto> page = new PageImpl<>(List.of(productDto), PageRequest.of(0, 10), 1);
        when(productService.searchProducts(any(ProductFilter.class))).thenReturn(page);

        mockMvc.perform(get("/v1/products")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].name").value("Laptop"))
                .andExpect(jsonPath("$.totalElements").value(1));

        verify(productService).searchProducts(any(ProductFilter.class));
    }

    @Test
    @WithMockUser
    void testCreateProduct() throws Exception {
        when(productService.createProduct(any(ProductDto.class))).thenReturn(productDto);

        mockMvc.perform(post("/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Laptop"));

        verify(productService).createProduct(any(ProductDto.class));
    }

    @Test
    @WithMockUser
    void testUpdateProduct() throws Exception {
        when(productService.updateProduct(eq(1L), any(ProductDto.class))).thenReturn(productDto);

        mockMvc.perform(put("/v1/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Laptop"));

        verify(productService).updateProduct(eq(1L), any(ProductDto.class));
    }

    @Test
    @WithMockUser
    void testDeleteProduct() throws Exception {
        doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/v1/products/1"))
                .andExpect(status().isNoContent());

        verify(productService).deleteProduct(1L);
    }

    @Test
    @WithMockUser
    void testGetProductByIdNotFound() throws Exception {
        when(productService.getProductById(999L))
                .thenThrow(new NotFoundException("Product not found"));

        mockMvc.perform(get("/v1/products/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Product not found"));

        verify(productService).getProductById(999L);
    }

    @Test
    @WithMockUser
    void testCreateProductValidationFail() throws Exception {
        ProductDto invalidDTO = new ProductDto();
        invalidDTO.setPrice(-10.0);

        mockMvc.perform(post("/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists());
    }

}