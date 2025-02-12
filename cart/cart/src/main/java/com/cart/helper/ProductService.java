package com.cart.helper;//package com.cart.helper;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import java.util.Set;

@Service
public class ProductService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String PRODUCT_API_URL = "https://productService.com/api/products/"; // Replace with actual URL

    // Mock product set for local validation (for testing purposes)
    private static final Set<String> MOCK_VALID_PRODUCTS = Set.of("P-101", "P-202", "P-303");

    public void validateProduct(String productId) {
        try {
            // First, check locally if the product exists (for testing)
            if (MOCK_VALID_PRODUCTS.contains(productId)) {
                System.out.println("Product validated (Mock): " + productId);
                return;
            }

            // Call external Product API for validation
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<String> request = new HttpEntity<>(headers);

            ResponseEntity<Void> response = restTemplate.exchange(
                    PRODUCT_API_URL + productId, HttpMethod.HEAD, request, Void.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("Product validated: " + productId);
                return;
            }

            throw new RuntimeException("Invalid product: " + productId);

        } catch (Exception e) {
            throw new RuntimeException("Product validation failed: " + e.getMessage());
        }
    }
}
//import org.springframework.stereotype.Service;
//
//import java.util.Set;
//
//@Service
//public class ProductService {
//
//    // ✅ Mock valid product IDs for testing
//    private static final Set<String> MOCK_VALID_PRODUCTS = Set.of("P-101", "P-202", "P-303");
//
//    public void validateProduct(String productId) {
//        // ✅ Hardcoded validation
//        if (MOCK_VALID_PRODUCTS.contains(productId)) {
//            System.out.println("✅ Product validated (Hardcoded): " + productId);
//            return;
//        }
//
//        // ❌ Simulate an invalid product scenario
//        throw new RuntimeException("❌ Invalid product: " + productId);
//    }
//}
