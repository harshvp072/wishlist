package com.wishlist.cart.dto.cart;

import lombok.Data;

@Data
public class CartItemDTO {
    private Long id;
    private String productId;
    private int quantity;
}
