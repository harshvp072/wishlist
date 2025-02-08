package com.cart.helper;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class IdentityService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String IDENTITY_API_URL = "https://identityService.com/api/auth"; // Replace with actual URL

    // Hardcoded test token mapping
    private static final Map<String, String> MOCK_TOKENS = Map.of(
            "H.V.P-072", "user-124"
    );

    public String authenticateUser(String email, String password) {
        try {
            Map<String, String> response = restTemplate.postForObject(
                    IDENTITY_API_URL,
                    Map.of("email", email, "password", password),
                    Map.class
            );
            return response != null ? response.get("token") : null;
        } catch (Exception e) {
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }
    }

    public String getUserIdFromToken(String token) {
        if (token == null || token.isBlank()) {
            throw new RuntimeException("Token is missing");
        }

        // Extract token if prefixed with "Bearer "
        token = token.startsWith("Bearer ") ? token.substring(7) : token;

        // Check hardcoded tokens first
        if (MOCK_TOKENS.containsKey(token)) {
            return MOCK_TOKENS.get(token);
        }

        throw new RuntimeException("Invalid token");
    }
}
