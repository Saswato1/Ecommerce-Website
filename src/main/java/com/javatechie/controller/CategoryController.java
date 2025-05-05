package com.javatechie.controller;

import com.javatechie.entity.Category;
import com.javatechie.entity.Role;
import com.javatechie.entity.User;
import com.javatechie.service.CategoryService;
import com.javatechie.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
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

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Category> getCategoryByName(@PathVariable String name) {
        return ResponseEntity.ok(categoryService.getCategoryByName(name));
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        // Check if user is admin
        if (!isCurrentUserAdmin()) {
            return Res