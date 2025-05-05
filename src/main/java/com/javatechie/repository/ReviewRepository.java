package com.javatechie.repository;

import com.javatechie.entity.Product;
import com.javatechie.entity.Review;
import com.javatechie.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    Page<Review> findByProduct(Product product, Pageable pageable);
    
    List<Review> findByUser(User user);
    
    List<Review> findByProductAndIsApproved(Product product, boolean isApproved);
    
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product = :product AND r.isApproved = true")
    Double getAverageRatingForProduct(@Param("product") Product product);
    
    @Query("SELECT COUNT(r) FROM Review r WHERE r.product = :product AND r.isApproved = true")
    Long getReviewCountForProduct(@Param("product") Product product);
    
    List<Review> findByIsApproved(boolean isApproved);
    
    @Query("SELECT r FROM Review r WHERE r.product = :product AND r.user = :user")
    Review findByProductAndUser(@Param("product") Product product, @Param("user") User user);
}