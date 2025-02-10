package com.cart.dto;

import lombok.Data;

@Data
public class CartItemDTO {
    private Long cartItemId;
    private String productId;
    private int quantity;
}

