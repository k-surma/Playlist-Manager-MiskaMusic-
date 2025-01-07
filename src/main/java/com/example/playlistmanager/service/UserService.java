package com.example.playlistmanager.service;

import com.example.playlistmanager.models.User;
import com.example.playlistmanager.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    private Long loggedInUserId; // ID of the currently logged-in user

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.userRepository.initializeDatabase();
    }

    public void registerUser(String email, String password, String name) {
        if (email == null || email.isBlank() || password == null || password.isBlank() || name == null || name.isBlank()) {
            throw new RuntimeException("Email, password, and name must not be empty.");
        }
        if (userRepository.findByEmail(email) != null) {
            throw new RuntimeException("User already exists.");
        }
        User user = new User(email, password);
        user.setName(name); // Set the name
        userRepository.save(user);
    }

    public boolean authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            loggedInUserId = user.getId(); // Set the logged-in user ID
            return true;
        }
        return false;
    }

    public Long getLoggedInUserId() {
        if (loggedInUserId == null) {
            throw new RuntimeException("No user is currently logged in.");
        }
        return loggedInUserId;
    }

    public User findUserById(Long id) {
        return userRepository.findById(id);
    }

    public void logout() {
        loggedInUserId = null; // Clear the logged-in user ID
    }
}
