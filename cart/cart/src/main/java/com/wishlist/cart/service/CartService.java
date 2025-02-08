package com.wishlist.cart.service;

import com.wishlist.cart.dto.cart.CartItemDTO;
import com.wishlist.cart.dto.cart.CartRequestDTO;
import com.wishlist.cart.dto.cart.CartResponseDTO;
import com.wishlist.cart.entity.Cart;
import com.wishlist.cart.entity.CartItem;
import com.wishlist.cart.repository.CartRepo;
import com.wishlist.cart.helper.IdentityService;
import com.wishlist.cart.helper.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
        String userId = identityService.getUserIdFromToken(token);
        Optional<Cart> cartOptional = cartRepository.findByUserId(userId);

        return cartOptional.map(this::convertToResponseDTO)
                .orElseGet(() -> createNewCart(userId));
    }

    @Transactional
    public CartResponseDTO addToCart(String token, CartRequestDTO cartRequestDTO) {
        String userId = identityService.getUserIdFromToken(token);
        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> createCart(userId));

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
    }

    private CartResponseDTO createNewCart(String userId) {
        Cart cart = createCart(userId);
        return convertToResponseDTO(cart);
    }

    private Cart createCart(String userId) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setCreatedDate(LocalDateTime.now());
        return cartRepository.save(cart);
    }

    private CartResponseDTO convertToResponseDTO(Cart cart) {
        CartResponseDTO cartResponseDTO = new CartResponseDTO();
        cartResponseDTO.setId(cart.getId());
        cartResponseDTO.setUserId(cart.getUserId());
        cartResponseDTO.setCreatedDate(cart.getCreatedDate());

        List<CartItemDTO> cartItemDTOS = cart.getItems().stream().map(item -> {
            CartItemDTO dto = new CartItemDTO();
            dto.setId(item.getId());
            dto.setProductId(item.getProductId());
            dto.setQuantity(item.getQuantity());
            return dto;
        }).collect(Collectors.toList());

        cartResponseDTO.setItems(cartItemDTOS);
        return cartResponseDTO;
    }
}
