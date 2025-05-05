package com.javatechie.controller;

import com.javatechie.entity.CartItem;
import com.javatechie.entity.User;
import com.javatechie.service.CartService;
import com.javatechie.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    // For demo purposes - in a real app, you would get the user from the session/token
    private User getCurrentUser() {
        // This is a placeholder - in a real app, you would get the user from the session/token
        // For now, we'll just return a mock user with ID 1
        return userService.findById(1L);
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getCartItems() {
        User user = getCurrentUser();
        return ResponseEntity.ok(cartService.getCartItems(user.getId()));
    }

    @PostMapping("/add")
    public ResponseEntity<CartItem> addToCart(
            @RequestParam Long productId,
            @RequestParam Integer quantity
    ) {
        User user = getCurrentUser();
        return ResponseEntity.ok(cartService.addToCart(user.getId(), productId, quantity));
    }

    @PutMapping("/update/{cartItemId}")
    public ResponseEntity<CartItem> updateCartItemQuantity(
            @PathVariable Long cartItemId,
            @RequestParam Integer quantity
    ) {
        User user = getCurrentUser();
        return ResponseEntity.ok(cartService.updateCartItemQuantity(user.getId(), cartItemId, quantity));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeFromCart(
            @RequestParam Long productId
    ) {
        User user = getCurrentUser();
        cartService.removeFromCart(user.getId(), productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart() {
        User user = getCurrentUser();
        cartService.clearCart(user.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getCartItemCount() {
        User user = getCurrentUser();
        return ResponseEntity.ok(Map.of("count", cartService.getCartItemCount(user.getId())));
    }

    @GetMapping("/total")
    public ResponseEntity<Map<String, BigDecimal>> getCartTotal() {
        User user = getCurrentUser();
        return ResponseEntity.ok(Map.of("total", cartService.getCartTotal(user.getId())));
    }
}
