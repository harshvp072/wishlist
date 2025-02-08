package com.wishlist.cart.dto.cart;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CartResponseDTO {
    private Long id;
    private String userId;
    private LocalDateTime createdDate;
    private List<CartItemDTO> items;
}
