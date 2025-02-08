package com.cart.service;

import com.cart.dto.CartItemDTO;
import com.cart.dto.CartRequestDTO;
import com.cart.dto.CartResponseDTO;
import com.cart.entity.Cart;
import com.cart.entity.CartItem;
import com.cart.repository.CartRepo;
import com.cart.helper.IdentityService;
import com.cart.helper.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepo cartRepository;
    private final IdentityService identityService;
    private final ProductService productService;

    public CartResponseDTO getCart(String token) {
        try {
            String userId = identityService.getUserIdFromToken(token);
            Optional<Cart> cartOptional = cartRepository.findByUserId(userId);
            return cartOptional.map(this::convertToResponseDTO)
                    .orElseGet(() -> createNewCart(userId));
        } catch (Exception e) {
            throw new RuntimeException("Error fetching cart: " + e.getMessage(), e);
        }
    }

    @Transactional
    public CartResponseDTO addToCart(String token, CartRequestDTO cartRequestDTO) {
        try {
            String userId = identityService.getUserIdFromToken(token);
            Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> createCart(userId));

            if (cart.getItems() == null) {
                cart.setItems(new ArrayList<>()); // Prevent NullPointerException
            }

            for (CartItemDTO itemDTO : cartRequestDTO.getItems()) {
                productService.validateProduct(itemDTO.getProductId());  // Check product validity

                CartItem cartItem = new CartItem();
                cartItem.setCart(cart);
                cartItem.setProductId(itemDTO.getProductId());
                cartItem.setQuantity(itemDTO.getQuantity());

                cart.getItems().add(cartItem);
            }

            cartRepository.save(cart);
            return convertToResponseDTO(cart);
        } catch (Exception e) {
            throw new RuntimeException("Error adding item to cart: " + e.getMessage(), e);
        }
    }

    private CartResponseDTO createNewCart(String userId) {
        Cart cart = createCart(userId);
        return convertToResponseDTO(cart);
    }

    private Cart createCart(String userId) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setCreatedDate(LocalDateTime.now());
        cart.setItems(new ArrayList<>());  // Ensure items list is initialized
        return cartRepository.save(cart);
    }

    private CartResponseDTO convertToResponseDTO(Cart cart) {
        CartResponseDTO cartResponseDTO = new CartResponseDTO();
        cartResponseDTO.setCartId(cart.getId()); // Use cartId instead of id
        cartResponseDTO.setUserId(cart.getUserId());
        cartResponseDTO.setCreatedDate(cart.getCreatedDate());

        List<CartItemDTO> cartItemDTOS = cart.getItems().stream().map(item -> {
            CartItemDTO dto = new CartItemDTO();
            dto.setCartItemId(item.getId()); // Use cartItemId instead of id
            dto.setProductId(item.getProductId());
            dto.setQuantity(item.getQuantity());
            return dto;
        }).collect(Collectors.toList());

        cartResponseDTO.setItems(cartItemDTOS);
        return cartResponseDTO;
    }

}
