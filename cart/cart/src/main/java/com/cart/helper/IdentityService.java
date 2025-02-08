package com.cart.helper;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Service
public class IdentityService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String IDENTITY_API_URL = "https://identityService.com/api/auth"; // Replace with actual URL


    // the identity service will give us token as a
    // response on the basis of email and password

    public String authenticateUser(String email, String password) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", email);
        requestBody.put("password", password);

        try {
            Map<String, String> response = restTemplate.postForObject(IDENTITY_API_URL, requestBody, Map.class);
            return response != null ? response.get("token") : null;
        } catch (Exception e) {
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }
    }

    public String getUserIdFromToken(String token) {
        System.out.println("Received Token: [" + token + "]"); // Debugging

        if (token == null || token.isEmpty()) {
            throw new RuntimeException("Token is missing");
        }

        // Extract token if it starts with "Bearer "
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        System.out.println("Extracted Token: [" + token + "]"); // Debugging

        // ✅ Instead of hardcoding, simulate dynamic users for testing
        Map<String, String> mockTokens = new HashMap<>();
        mockTokens.put("H.V.P-072", "user-124");
        mockTokens.put("A.B.C-101", "user-200");
        mockTokens.put("X.Y.Z-999", "user-300");

        if (mockTokens.containsKey(token)) {
            return mockTokens.get(token);
        }

        throw new RuntimeException("Invalid token");
    }


}
