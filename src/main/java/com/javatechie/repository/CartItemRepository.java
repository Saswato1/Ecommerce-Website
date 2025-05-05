package com.javatechie.repository;

import com.javatechie.entity.CartItem;
import com.javatechie.entity.Product;
import com.javatechie.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    
    List<CartItem> findByUser(User user);
    
    Optional<CartItem> findByUserAndProduct(User user, Product product);
    
    void deleteByUserAndProduct(User user, Product product);
    
    void deleteByUser(User user);
    
    @Query("SELECT COUNT(c) FROM CartItem c WHERE c.user = :user")
    Long countByUser(@Param("user") User user);
    
    @Query("SELECT SUM(c.totalPrice) FROM CartItem c WHERE c.user = :user")
    BigDecimal getTotalPriceByUser(@Param("user") User user);
}