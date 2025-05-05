package com.javatechie.service;

import com.javatechie.entity.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrderService orderService;

    @Value("${payment.razorpay.key-id}")
    private String razorpayKeyId;

    @Value("${payment.razorpay.key-secret}")
    private String razorpayKeySecret;

    @Value("${payment.stripe.api-key}")
    private String stripeApiKey;

    public Map<String, Object> createRazorpayOrder(Long orderId) {
        try {
            Order order = orderService.getOrderById(orderId);
            
            RazorpayClient razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
            
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", order.getTotalAmount().multiply(java.math.BigDecimal.valueOf(100)).intValue()); // Convert to paise
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", order.getOrderNumber());
            
            com.razorpay.Order razorpayOrder = razorpayClient.orders.create(orderRequest);
            
            Map<String, Object> response = new HashMap<>();
            response.put("orderId", razorpayOrder.get("id"));
            response.put("amount", razorpayOrder.get("amount"));
            response.put("currency", razorpayOrder.get("currency"));
            response.put("receipt", razorpayOrder.get("receipt"));
            
            return response;
        } catch (RazorpayException e) {
            throw new RuntimeException("Error creating Razorpay order: " + e.getMessage(), e);
        }
    }

    public Map<String, Object> createStripePaymentIntent(Long orderId) {
        try {
            Order order = orderService.getOrderById(orderId);
            
            Stripe.apiKey = stripeApiKey;
            
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(order.getTotalAmount().multiply(java.math.BigDecimal.valueOf(100)).longValue()) // Convert to cents
                    .setCurrency("usd")
                    .setDescription("Payment for order #" + order.getOrderNumber())
                    .putMetadata("order_id", order.getId().toString())
                    .build();
            
            PaymentIntent paymentIntent = PaymentIntent.create(params);
            
            Map<String, Object> response = new HashMap<>();
            response.put("clientSecret", paymentIntent.getClientSecret());
            response.put("paymentIntentId", paymentIntent.getId());
            response.put("amount", paymentIntent.getAmount());
            response.put("currency", paymentIntent.getCurrency());
            
            return response;
        } catch (StripeException e) {
            throw new RuntimeException("Error creating Stripe payment intent: " + e.getMessage(), e);
        }
    }

    public Order handleRazorpayPaymentSuccess(String razorpayPaymentId, String razorpayOrderId, String razorpaySignature, Long orderId) {
        // In a real application, you would verify the signature here
        // For simplicity, we'll just update the order payment status
        return orderService.updatePaymentStatus(orderId, razorpayPaymentId);
    }

    public Order handleStripePaymentSuccess(String stripePaymentIntentId, Long orderId) {
        // In a real application, you would verify the payment status with Stripe
        // For simplicity, we'll just update the order payment status
        return orderService.updatePaymentStatus(orderId, stripePaymentIntentId);
    }
}