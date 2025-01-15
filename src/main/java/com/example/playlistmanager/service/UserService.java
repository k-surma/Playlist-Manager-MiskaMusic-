package com.example.playlistmanager.service;

import com.example.playlistmanager.models.User;
import com.example.playlistmanager.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    private Long loggedInUserId;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.userRepository.initializeDatabase();
    }

    public void registerUser(String email, String password, String name) {
        if (email == null || email.isBlank() || password == null || password.isBlank() || name == null || name.isBlank()) {
            throw new RuntimeException("Email, hasło i imię nie mogą być puste.");
        }
        if (userRepository.findByEmail(email) != null) {
            throw new RuntimeException("Użytkownik już istnieje.");
        }
        User user = new User(email, password);
        user.setName(name);
        userRepository.save(user);
    }

    public boolean authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            loggedInUserId = user.getId();
            return true;
        }
        return false;
    }

    public Long getLoggedInUserId() {
        if (loggedInUserId == null) {
            throw new RuntimeException("Nie wybrano zalogowanego żadnego użytkownika.");
        }
        return loggedInUserId;
    }

    public User findUserById(Long id) {
        return userRepository.findById(id);
    }

    public User findUserByEmail(String email) {return userRepository.findByEmail(email);}

    public void logout() {
        loggedInUserId = null;
    }
}
