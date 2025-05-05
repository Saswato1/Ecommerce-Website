package com.javatechie.controller;

import com.javatechie.entity.Order;
import com.javatechie.entity.User;
import com.javatechie.service.PaymentService;
import com.javatechie.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final UserService userService;

    // For demo purposes - in a real app, you would get the user from the session/token
    private User getCurrentUser() {
        // This is a placeholder - in a real app, you would get the user from the session/token
        // For now, we'll just return a mock user with ID 1
        return userService.findById(1L);
    }

    @PostMapping("/razorpay/create/{orderId}")
    public ResponseEntity<Map<String, Object>> createRazorpayOrder(
            @PathVariable Long orderId
    ) {
        // In a real app, you would verify that the order belongs to the current user
        return ResponseEntity.ok(paymentService.createRazorpayOrder(orderId));
    }

    @PostMapping("/stripe/create/{orderId}")
    public ResponseEntity<Map<String, Object>> createStripePaymentIntent(
            @PathVariable Long orderId
    ) {
        // In a real app, you would verify that the order belongs to the current user
        return ResponseEntity.ok(paymentService.createStripePaymentIntent(orderId));
    }

    @PostMapping("/razorpay/success/{orderId}")
    public ResponseEntity<Order> handleRazorpayPaymentSuccess(
            @PathVariable Long orderId,
            @RequestParam String razorpayPaymentId,
            @RequestParam String razorpayOrderId,
            @RequestParam String razorpaySignature
    ) {
        return ResponseEntity.ok(paymentService.handleRazorpayPaymentSuccess(
                razorpayPaymentId, razorpayOrderId, razorpaySignature, orderId));
    }

    @PostMapping("/stripe/success/{orderId}")
    public ResponseEntity<Order> handleStripePaymentSuccess(
            @PathVariable Long orderId,
            @RequestBody Map<String, String> payload
    ) {
        String paymentIntentId = payload.get("paymentIntentId");
        if (paymentIntentId == null || paymentIntentId.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(paymentService.handleStripePaymentSuccess(paymentIntentId, orderId));
    }

    // This endpoint would be called by Razorpay webhook
    @PostMapping("/razorpay/webhook")
    public ResponseEntity<Void> razorpayWebhook(@RequestBody String payload) {
        // In a real application, you would verify the webhook signature and process the event
        // For simplicity, we'll just return a success response
        return ResponseEntity.ok().build();
    }

    // This endpoint would be called by Stripe webhook
    @PostMapping("/stripe/webhook")
    public ResponseEntity<Void> stripeWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String signature) {
        // In a real application, you would verify the webhook signature and process the event
        // For simplicity, we'll just return a success response
        return ResponseEntity.ok().build();
    }
}
