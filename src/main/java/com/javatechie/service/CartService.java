package com.javatechie.service;

import com.javatechie.entity.CartItem;
import com.javatechie.entity.Product;
import com.javatechie.entity.User;
import com.javatechie.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final UserService userService;
    private final ProductService productService;

    public List<CartItem> getCartItems(Long userId) {
        User user = userService.findById(userId);
        return cartItemRepository.findByUser(user);
    }

    public CartItem addToCart(Long userId, Long productId, Integer quantity) {
        User user = userService.findById(userId);
        Product product = productService.getProductById(productId);

        if (product.getStock() < quantity) {
            throw new RuntimeException("Not enough stock available");
        }

        Optional<CartItem> existingCartItem = cartItemRepository.findByUserAndProduct(user, product);

        if (existingCartItem.isPresent()) {
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setUnitPrice(product.getDiscountPrice() != null ? product.getDiscountPrice() : product.getPrice());
            return cartItemRepository.save(cartItem);
        } else {
            CartItem cartItem = CartItem.builder()
                    .user(user)
                    .product(product)
                    .quantity(quantity)
                    .unitPrice(product.getDiscountPrice() != null ? product.getDiscountPrice() : product.getPrice())
                    .build();
            return cartItemRepository.save(cartItem);
        }
    }

    public CartItem updateCartItemQuantity(Long userId, Long cartItemId, Integer quantity) {
        User user = userService.findById(userId);
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (!cartItem.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Cart item does not belong to user");
        }

        if (cartItem.getProduct().getStock() < quantity) {
            throw new RuntimeException("Not enough stock available");
        }

        cartItem.setQuantity(quantity);
        return cartItemRepository.save(cartItem);
    }

    @Transactional
    public void removeFromCart(Long userId, Long productId) {
        User user = userService.findById(userId);
        Product product = productService.getProductById(productId);
        cartItemRepository.deleteByUserAndProduct(user, product);
    }

    @Transactional
    public void clearCart(Long userId) {
        User user = userService.findById(userId);
        cartItemRepository.deleteByUser(user);
    }

    public Long getCartItemCount(Long userId) {
        User user = userService.findById(userId);
        return cartItemRepository.countByUser(user);
    }

    public BigDecimal getCartTotal(Long userId) {
        User user = userService.findById(userId);
        return cartItemRepository.getTotalPriceByUser(user);
    }
}