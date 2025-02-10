package com.cart.controller;

import com.cart.dto.CartRequestDTO;
import com.cart.dto.CartResponseDTO;
import com.cart.dto.apiResponse.ApiResponse;
import com.cart.service.CartService;
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
    public ResponseEntity<ApiResponse<CartResponseDTO>> getCart(@RequestHeader("Authorization") String token) {
        try {
            CartResponseDTO cartResponse = cartService.getCart(token);
            return ResponseEntity.ok(new ApiResponse<>(true, "Cart retrieved successfully", cartResponse));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "An unexpected error occurred.", null));
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse<String>> clearCart(@RequestHeader("Authorization") String token) {
        try {
            cartService.clearCart(token);
            return ResponseEntity.ok(new ApiResponse<>(true, "Cart cleared successfully.", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "An unexpected error occurred.", null));
        }
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<ApiResponse<CartResponseDTO>> removeCartItem(@RequestHeader("Authorization") String token,
                                                                       @PathVariable String productId) {
        try {
            CartResponseDTO updatedCart = cartService.removeCartItem(token, productId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Item removed successfully", updatedCart));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "An unexpected error occurred.", null));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<CartResponseDTO>> addToCart(@RequestHeader("Authorization") String token,
                                                                  @RequestBody CartRequestDTO cartRequestDTO) {
        try {
            CartResponseDTO cartResponse = cartService.addToCart(token, cartRequestDTO);
            return ResponseEntity.ok(new ApiResponse<>(true, "Item added to cart successfully", cartResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "An unexpected error occurred.", null));
        }
    }
}
