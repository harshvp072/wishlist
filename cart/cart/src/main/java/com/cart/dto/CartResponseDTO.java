package com.cart.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CartResponseDTO {
    private Long cartId;  // Renamed from id
    private String userId;
    private LocalDateTime createdDate;
    private List<CartItemDTO> items;
}

