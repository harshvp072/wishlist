package com.wishlist.cart.controller;

import com.wishlist.cart.dto.cart.CartRequestDTO;
import com.wishlist.cart.dto.cart.CartResponseDTO;
import com.wishlist.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<?> getCart(@RequestHeader("Authorization") String token) {
        try {
            CartResponseDTO cartResponse = cartService.getCart(token);
            return ResponseEntity.ok(cartResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestHeader("Authorization") String token,
                                       @RequestBody CartRequestDTO cartRequestDTO) {
        try {
            CartResponseDTO cartResponse = cartService.addToCart(token, cartRequestDTO);
            return ResponseEntity.ok(cartResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }
}
