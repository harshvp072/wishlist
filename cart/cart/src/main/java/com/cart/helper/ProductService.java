package com.cart.helper;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

@Service
public class ProductService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String PRODUCT_API_URL = "https://productService.com/api/products/"; // Replace with actual URL

    public boolean validateProduct(String productId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<String> request = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    PRODUCT_API_URL + productId, HttpMethod.GET, request, Map.class
            );

            return response.getStatusCode() == HttpStatus.OK && response.getBody() != null;
        } catch (Exception e) {
            return false; // Return false if validation fails
        }
    }
}
