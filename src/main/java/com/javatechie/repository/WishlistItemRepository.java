package com.javatechie.repository;

import com.javatechie.entity.Product;
import com.javatechie.entity.User;
import com.javatechie.entity.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {
    
    List<WishlistItem> findByUser(User user);
    
    Optional<WishlistItem> findByUserAndProduct(User user, Product product);
    
    boolean existsByUserAndProduct(User user, Product product);
    
    void deleteByUserAndProduct(User user, Product product);
}