package com.javatechie.service;

import com.javatechie.entity.Role;
import com.javatechie.entity.User;
import com.javatechie.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // Simple password encoder implementation to replace Spring Security's PasswordEncoder
    private String encodePassword(String password) {
        // This is a very basic implementation - in a real app, use a proper hashing library
        return "encoded_" + password;
    }

    public User loadUserByUsername(String username) {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + username));
    }

    public User registerUser(String name, String email, String password, String phoneNumber) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already in use");
        }

        User user = User.builder()
                .name(name)
                .email(email)
                .password(encodePassword(password))
                .phoneNumber(phoneNumber)
                .role(Role.USER)
                .isEnabled(true)
                .build();

        return userRepository.save(user);
    }

    public User registerAdmin(String name, String email, String password, String phoneNumber) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already in use");
        }

        User admin = User.builder()
                .name(name)
                .email(email)
                .password(encodePassword(password))
                .phoneNumber(phoneNumber)
                .role(Role.ADMIN)
                .isEnabled(true)
                .build();

        return userRepository.save(admin);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User processOAuthPostLogin(String email, String name, String googleId) {
        User existingUser = userRepository.findByEmail(email).orElse(null);

        if (existingUser == null) {
            User newUser = User.builder()
                    .name(name)
                    .email(email)
                    .googleId(googleId)
                    .password(encodePassword(""))
                    .role(Role.USER)
                    .isEnabled(true)
                    .build();

            return userRepository.save(newUser);
        }

        return existingUser;
    }
}
