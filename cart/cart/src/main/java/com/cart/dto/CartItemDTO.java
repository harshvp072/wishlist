package com.cart.dto;

import lombok.Data;

@Data
public class CartItemDTO {
    private Long cartItemId; // Renamed from id
    private String productId;
    private int quantity;
}

