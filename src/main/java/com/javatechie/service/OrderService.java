package com.javatechie.service;

import com.javatechie.entity.*;
import com.javatechie.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final ProductService productService;
    private final UserService userService;

    @Transactional
    public Order createOrder(Long userId, Long shippingAddressId, Long billingAddressId, Order.PaymentMethod paymentMethod) {
        User user = userService.findById(userId);
        List<CartItem> cartItems = cartService.getCartItems(userId);
        
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }
        
        // Find shipping and billing addresses
        Address shippingAddress = user.getAddresses().stream()
                .filter(address -> address.getId().equals(shippingAddressId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Shipping address not found"));
        
        Address billingAddress = billingAddressId != null ? 
                user.getAddresses().stream()
                        .filter(address -> address.getId().equals(billingAddressId))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Billing address not found")) : 
                shippingAddress;
        
        // Calculate total amount
        BigDecimal totalAmount = cartItems.stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Create order
        Order order = Order.builder()
                .orderNumber(generateOrderNumber())
                .user(user)
                .shippingAddress(shippingAddress)
                .billingAddress(billingAddress)
                .totalAmount(totalAmount)
                .status(Order.OrderStatus.PENDING)
                .paymentMethod(paymentMethod)
                .isPaid(false)
                .isDelivered(false)
                .build();
        
        // Create order items
        List<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> {
                    Product product = cartItem.getProduct();
                    
                    // Check stock
                    if (product.getStock() < cartItem.getQuantity()) {
                        throw new RuntimeException("Not enough stock for product: " + product.getName());
                    }
                    
                    // Update product stock
                    product.setStock(product.getStock() - cartItem.getQuantity());
                    productService.updateProduct(product.getId(), product);
                    
                    // Create order item
                    return OrderItem.builder()
                            .order(order)
                            .product(product)
                            .quantity(cartItem.getQuantity())
                            .unitPrice(cartItem.getUnitPrice())
                            .totalPrice(cartItem.getTotalPrice())
                            .build();
                })
                .collect(Collectors.toList());
        
        // Add order items to order
        orderItems.forEach(order::addOrderItem);
        
        // Save order
        Order savedOrder = orderRepository.save(order);
        
        // Clear cart
        cartService.clearCart(userId);
        
        return savedOrder;
    }
    
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
    }
    
    public Order getOrderByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("Order not found with order number: " + orderNumber));
    }
    
    public Page<Order> getUserOrders(Long userId, Pageable pageable) {
        User user = userService.findById(userId);
        return orderRepository.findByUser(user, pageable);
    }
    
    public List<Order> getOrdersByStatus(Order.OrderStatus status) {
        return orderRepository.findByStatus(status);
    }
    
    @Transactional
    public Order updateOrderStatus(Long orderId, Order.OrderStatus status) {
        Order order = getOrderById(orderId);
        order.setStatus(status);
        
        if (status == Order.OrderStatus.DELIVERED) {
            order.setDelivered(true);
            order.setDeliveredAt(LocalDateTime.now());
        }
        
        return orderRepository.save(order);
    }
    
    @Transactional
    public Order updatePaymentStatus(Long orderId, String paymentId) {
        Order order = getOrderById(orderId);
        order.setPaymentId(paymentId);
        order.setPaid(true);
        order.setPaidAt(LocalDateTime.now());
        order.setStatus(Order.OrderStatus.PROCESSING);
        
        return orderRepository.save(order);
    }
    
    private String generateOrderNumber() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
    }
}