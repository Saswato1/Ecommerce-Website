package com.javatechie.controller;

import com.javatechie.entity.Order;
import com.javatechie.entity.Role;
import com.javatechie.entity.User;
import com.javatechie.service.OrderService;
import com.javatechie.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    // For demo purposes - in a real app, you would get the user from the session/token
    private User getCurrentUser() {
        // This is a placeholder - in a real app, you would get the user from the session/token
        // For now, we'll just return a mock user with ID 1
        return userService.findById(1L);
    }

    // Helper method to check if current user is admin
    private boolean isCurrentUserAdmin() {
        User user = getCurrentUser();
        return user != null && user.getRole() == Role.ADMIN;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(
            @RequestParam Long shippingAddressId,
            @RequestParam(required = false) Long billingAddressId,
            @RequestParam Order.PaymentMethod paymentMethod
    ) {
        User user = getCurrentUser();
        return ResponseEntity.ok(orderService.createOrder(user.getId(), shippingAddressId, billingAddressId, paymentMethod));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);
        User user = getCurrentUser();

        // Check if the order belongs to the user or if the user is an admin
        if (!order.getUser().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(order);
    }

    @GetMapping("/number/{orderNumber}")
    public ResponseEntity<Order> getOrderByOrderNumber(@PathVariable String orderNumber) {
        Order order = orderService.getOrderByOrderNumber(orderNumber);
        User user = getCurrentUser();

        // Check if the order belongs to the user or if the user is an admin
        if (!order.getUser().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(order);
    }

    @GetMapping("/my-orders")
    public ResponseEntity<Page<Order>> getUserOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "orderDate") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        User user = getCurrentUser();
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        return ResponseEntity.ok(orderService.getUserOrders(user.getId(), pageable));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Order>> getOrdersByStatus(@PathVariable Order.OrderStatus status) {
        // Check if user is admin
        if (!isCurrentUserAdmin()) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(orderService.getOrdersByStatus(status));
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam Order.OrderStatus status
    ) {
        // Check if user is admin
        if (!isCurrentUserAdmin()) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, status));
    }

    @PutMapping("/{orderId}/payment")
    public ResponseEntity<Order> updatePaymentStatus(
            @PathVariable Long orderId,
            @RequestBody Map<String, String> payload
    ) {
        String paymentId = payload.get("paymentId");
        if (paymentId == null || paymentId.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(orderService.updatePaymentStatus(orderId, paymentId));
    }
}
