package com.cart.dto;

import lombok.Data;
import java.util.List;

@Data
public class CartRequestDTO {
    private String userId;
    private List<CartItemDTO> items;
}
