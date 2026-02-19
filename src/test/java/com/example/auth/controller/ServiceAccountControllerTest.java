package com.example.auth.controller;

import com.example.TestConfig;
import com.example.auth.dto.JwtResponse;
import com.example.auth.dto.ServiceAccountDto;
import com.example.auth.service.ServiceAccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({TestConfig.class})
@WebMvcTest(AuthController.class)
class ServiceAccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ServiceAccountService serviceAccountService;

    @Test
    void createServiceAccount_ShouldReturnToken_WhenValidRequest() throws Exception {
        ServiceAccountDto request = new ServiceAccountDto(null, "App-Interne", null, null);

        when(serviceAccountService.createServiceAccount(any(ServiceAccountDto.class))).thenReturn(new JwtResponse("mockedJwtToken"));

        mockMvc.perform(post("/api/v1/service-accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("mockedJwtToken"));

    }

    @Test
    void createServiceAccount_ShouldReturnBadRequest_WhenInvalidRequest() throws Exception {
        // Given - Missing fields
        ServiceAccountDto invalidRequest = new ServiceAccountDto(null, "", null, null);

        // When + Then
        mockMvc.perform(post("/api/v1/service-accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

}