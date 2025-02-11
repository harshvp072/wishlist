package com.cart.helper;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class IdentityService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String IDENTITY_API_URL = "https://identityService.com/api/token"; // Replace with actual URL

    // Hardcoded test token mapping
    private static final Map<String, String> MOCK_TOKENS = Map.of(
            "user-1", "H123456"
    );

    public String getTokenByUserId(String userId) {
        try {
            // Check hardcoded tokens first
            if (MOCK_TOKENS.containsKey(userId)) {
                return MOCK_TOKENS.get(userId);
            }

            // Call Identity API to get token
            Map<String, String> response = restTemplate.postForObject(
                    IDENTITY_API_URL,
                    Map.of("userId", userId),
                    Map.class
            );

            return response != null ? response.get("token") : null;
        } catch (Exception e) {
            throw new RuntimeException("Token retrieval failed: " + e.getMessage());
        }
    }

    public String getUserIdFromToken(String token) {
        if (token == null || token.isBlank()) {
            throw new RuntimeException("Token is missing");
        }

        // Extract token if prefixed with "Bearer "
        token = token.startsWith("Bearer ") ? token.substring(7) : token;

        // Check hardcoded tokens first
        for (Map.Entry<String, String> entry : MOCK_TOKENS.entrySet()) {
            if (entry.getValue().equals(token)) {
                return entry.getKey();
            }
        }
        throw new RuntimeException("Invalid token");
    }
}