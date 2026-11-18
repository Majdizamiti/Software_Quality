package com.Shadows.SpringZ.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Provides a clean JSON response on the root URL (/) 
 * instead of the default 404 Whitelabel Error Page.
 */
@RestController
public class RootApiController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> rootInfo() {
        return ResponseEntity.ok(Map.of(
                "application", "SpringZ E-Commerce REST API",
                "status", "UP",
                "message", "Welcome! The backend REST API is running successfully.",
                "endpoints", Map.of(
                        "auth_signup", "POST /api/auth/signup",
                        "auth_signin", "POST /api/auth/signin",
                        "products", "GET /api/products",
                        "categories", "GET /api/categories",
                        "health", "GET /actuator/health"
                )
        ));
    }
}
