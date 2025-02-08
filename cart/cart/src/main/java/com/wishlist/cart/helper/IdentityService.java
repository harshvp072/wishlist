package com.wishlist.cart.helper;

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
        // Logic to extract userId from the token (e.g., by calling Identity API or decoding JWT)
        return "extracted-user-id"; // Mocked response
    }
}
