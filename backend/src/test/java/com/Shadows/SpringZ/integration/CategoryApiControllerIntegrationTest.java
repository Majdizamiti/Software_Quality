package com.Shadows.SpringZ.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CategoryApiControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String jwtToken;

    @BeforeEach
    void setUp() throws Exception {
        String signupBody = """
            {
                "name": "Category Admin",
                "email": "category.admin@example.com",
                "password": "password123"
            }
            """;
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(signupBody));

        String signinBody = """
            {
                "email": "category.admin@example.com",
                "password": "password123"
            }
            """;
        MvcResult result = mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signinBody))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode responseNode = objectMapper.readTree(result.getResponse().getContentAsString());
        jwtToken = responseNode.get("token").asText();
    }

    @Test
    void accessCategoriesWithoutToken_Returns401() throws Exception {
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void categoryCrudLifecycle_Success() throws Exception {
        // 1. Create Category
        String createCategoryBody = """
            {
                "title": "Books",
                "description": "Fiction and Non-Fiction Books"
            }
            """;

        MvcResult createResult = mockMvc.perform(post("/api/categories")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createCategoryBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Books"))
                .andReturn();

        JsonNode categoryNode = objectMapper.readTree(createResult.getResponse().getContentAsString());
        long categoryId = categoryNode.get("id").asLong();

        // 2. Fetch All Categories
        mockMvc.perform(get("/api/categories")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Books"));

        // 3. Fetch Category By ID
        mockMvc.perform(get("/api/categories/" + categoryId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Books"))
                .andExpect(jsonPath("$.description").value("Fiction and Non-Fiction Books"));

        // 4. Update Category
        String updateCategoryBody = """
            {
                "title": "Educational Books",
                "description": "Textbooks and Tutorials"
            }
            """;

        mockMvc.perform(put("/api/categories/" + categoryId)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateCategoryBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Educational Books"));

        // 5. Delete Category
        mockMvc.perform(delete("/api/categories/" + categoryId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNoContent());

        // 6. Verify NotFound after deletion
        mockMvc.perform(get("/api/categories/" + categoryId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNotFound());
    }
}
