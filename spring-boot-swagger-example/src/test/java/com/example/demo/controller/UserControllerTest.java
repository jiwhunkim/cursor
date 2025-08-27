package com.example.demo.controller;

import com.example.demo.model.CreateUserRequest;
import com.example.demo.model.UpdateUserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/api/v1/users")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.content", hasSize(2))) // Initial 2 users
                .andExpect(jsonPath("$.data.pageNumber", is(0)))
                .andExpect(jsonPath("$.data.pageSize", is(10)));
    }

    @Test
    void testGetUserById() throws Exception {
        mockMvc.perform(get("/api/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.username", is("admin")));
    }

    @Test
    void testGetUserByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/users/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.message", containsString("not found")));
    }

    @Test
    void testCreateUser() throws Exception {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("testuser");
        request.setEmail("test@example.com");
        request.setPassword("password123");
        request.setFirstName("Test");
        request.setLastName("User");

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.username", is("testuser")))
                .andExpect(jsonPath("$.data.email", is("test@example.com")));
    }

    @Test
    void testCreateUserValidationError() throws Exception {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("ab"); // Too short
        request.setEmail("invalid-email"); // Invalid email

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.data", hasKey("username")))
                .andExpect(jsonPath("$.data", hasKey("email")));
    }

    @Test
    void testUpdateUser() throws Exception {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setFirstName("Updated");
        request.setLastName("Name");

        mockMvc.perform(put("/api/v1/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.firstName", is("Updated")))
                .andExpect(jsonPath("$.data.lastName", is("Name")));
    }

    @Test
    void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/v1/users/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", containsString("deleted successfully")));
    }

    @Test
    void testSearchUsers() throws Exception {
        mockMvc.perform(get("/api/v1/users/search")
                .param("query", "admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    void testHealthCheck() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("UP")))
                .andExpect(jsonPath("$.service", is("Spring Boot Swagger Example")));
    }

    @Test
    void testSwaggerUIAvailable() throws Exception {
        mockMvc.perform(get("/swagger-ui.html"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void testOpenAPIDocumentationAvailable() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.openapi", notNullValue()))
                .andExpect(jsonPath("$.info.title", is("Spring Boot Swagger Example API")));
    }
}