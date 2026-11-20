package com.Shadows.SpringZ.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void signup_Success() throws Exception {
        String body = """
            {
                "name": "Integration User",
                "email": "int.user@example.com",
                "password": "password123"
            }
            """;

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully"));
    }

    @Test
    void signup_DuplicateEmail_Returns400() throws Exception {
        String body = """
            {
                "name": "User One",
                "email": "duplicate@example.com",
                "password": "password123"
            }
            """;

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error: Email is already in use!"));
    }

    @Test
    void signin_Success_ReturnsJwtToken() throws Exception {
        String signupBody = """
            {
                "name": "Signin Integration",
                "email": "signin.int@example.com",
                "password": "password123"
            }
            """;

        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(signupBody));

        String signinBody = """
            {
                "email": "signin.int@example.com",
                "password": "password123"
            }
            """;

        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signinBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.email").value("signin.int@example.com"));
    }

    @Test
    void signin_InvalidPassword_Returns401() throws Exception {
        String signupBody = """
            {
                "name": "Valid User",
                "email": "valid.user@example.com",
                "password": "correctpassword"
            }
            """;

        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(signupBody));

        String wrongSigninBody = """
            {
                "email": "valid.user@example.com",
                "password": "wrongpassword"
            }
            """;

        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(wrongSigninBody))
                .andExpect(status().isUnauthorized());
    }
}
