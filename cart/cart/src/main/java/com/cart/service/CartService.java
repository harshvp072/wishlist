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

    public CartResponseDTO addToCart(String token, CartRequestDTO cartRequestDTO) {
        try {
            String userId = identityService.getUserIdFromToken(token);
            Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> createCart(userId));

            if (cart.getItems() == null) {
                cart.setItems(new ArrayList<>());
            }

            for (CartItemDTO itemDTO : cartRequestDTO.getItems()) {
                productService.validateProduct(itemDTO.getProductId()); // ✅ Validate product before adding

                // 🔹 Check if product is already in the cart
                Optional<CartItem> existingItem = cart.getItems().stream()
                        .filter(item -> item.getProductId().equals(itemDTO.getProductId()))
                        .findFirst();

                if (existingItem.isPresent()) {
                    // 🔹 If exists, update quantity
                    existingItem.get().setQuantity(existingItem.get().getQuantity() + itemDTO.getQuantity());
                } else {
                    // 🔹 Else, create a new cart item
                    CartItem cartItem = new CartItem();
                    cartItem.setCart(cart);
                    cartItem.setProductId(itemDTO.getProductId());
                    cartItem.setQuantity(itemDTO.getQuantity());
                    cart.getItems().add(cartItem);
                }
            }

            cartRepository.save(cart);
            return convertToResponseDTO(cart);
        } catch (Exception e) {
            throw new RuntimeException("Error adding item to cart: " + e.getMessage(), e);
        }
    }

    public CartResponseDTO removeCartItem(String token, String productId) {
        try {
            productService.validateProduct(productId); // ✅ Validate product before removing

            String userId = identityService.getUserIdFromToken(token);
            Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Cart not found"));

            boolean removed = cart.getItems().removeIf(item -> item.getProductId().equals(productId));

            if (!removed) {
                throw new RuntimeException("Product not found in cart");
            }

            cartRepository.save(cart);
            return convertToResponseDTO(cart);
        } catch (Exception e) {
            throw new RuntimeException("Error removing item from cart: " + e.getMessage(), e);
        }
    }

    // Order response
    public List<CartItemDTO> getCartItemsForOrder(String token) {
        String userId = identityService.getUserIdFromToken(token);
        Optional<Cart> cartOptional = cartRepository.findByUserId(userId);

        return cartOptional.map(cart -> {
            List<CartItemDTO> cartItemDTOList = new ArrayList<>();

            for (CartItem item : cart.getItems()) {
                CartItemDTO dto = new CartItemDTO(item.getProductId(), item.getQuantity());
                cartItemDTOList.add(dto);
            }

            return cartItemDTOList;
        }).orElseThrow(() -> new RuntimeException("Cart not found for user"));

    }

    public void clearCart(String token) {
        String userId = identityService.getUserIdFromToken(token);
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Cart not found"));
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    private CartResponseDTO createNewCart(String userId) {
        Cart cart = createCart(userId);
        return convertToResponseDTO(cart);
    }

    private Cart createCart(String userId) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setCreatedDate(LocalDateTime.now());
        cart.setItems(new ArrayList<>());
        return cartRepository.save(cart);
    }

    private CartResponseDTO convertToResponseDTO(Cart cart) {
        CartResponseDTO cartResponseDTO = new CartResponseDTO();
        cartResponseDTO.setCartId(cart.getId());
        cartResponseDTO.setUserId(cart.getUserId());
        cartResponseDTO.setCreatedDate(cart.getCreatedDate());

        List<CartItemDTO> cartItemDTOS = cart.getItems().stream().map(item -> {
            CartItemDTO dto = new CartItemDTO();
            dto.setProductId(item.getProductId());
            dto.setQuantity(item.getQuantity());
            return dto;
        }).collect(Collectors.toList());

        cartResponseDTO.setItems(cartItemDTOS);
        return cartResponseDTO;
    }
}
