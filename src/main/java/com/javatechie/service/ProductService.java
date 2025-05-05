package com.javatechie.service;

import com.javatechie.entity.Category;
import com.javatechie.entity.Product;
import com.javatechie.repository.CategoryRepository;
import com.javatechie.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product productDetails) {
        Product product = getProductById(id);
        
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setDiscountPrice(productDetails.getDiscountPrice());
        product.setStock(productDetails.getStock());
        product.setImageUrls(productDetails.getImageUrls());
        product.setCategory(productDetails.getCategory());
        product.setActive(productDetails.isActive());
        
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Page<Product> getProductsByCategory(Long categoryId, Pageable pageable) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));
        return productRepository.findByCategory(category, pageable);
    }

    public Page<Product> searchProducts(String keyword, Pageable pageable) {
        return productRepository.searchProducts(keyword, pageable);
    }

    public Page<Product> filterProductsByPrice(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        return productRepository.findByPriceBetween(minPrice, maxPrice, pageable);
    }

    public Page<Product> filterProductsByCategoryAndPrice(
            Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));
        return productRepository.findByCategoryAndPriceBetween(category, minPrice, maxPrice, pageable);
    }

    public List<Product> getNewArrivals() {
        return productRepository.findTop10ByOrderByCreatedAtDesc();
    }

    public List<Product> getBestSellingProducts(Pageable pageable) {
        return productRepository.findBestSellingProducts(pageable);
    }
}