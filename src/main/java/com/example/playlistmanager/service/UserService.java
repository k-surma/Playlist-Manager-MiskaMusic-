package com.example.playlistmanager.service;
//logika biznesowa aplikacji (powiązanie z kontrolerami)
import com.example.playlistmanager.models.User;
import com.example.playlistmanager.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    //wstrzykuje UserRepository i inicjalizuje bazę danych
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.userRepository.initializeDatabase();
    }

    //sprawdza czy użytkownik już istnieje i dodaje nowego użytkownika
    public void registerUser(String email, String password) {
        if (userRepository.findByEmail(email) != null) {
            throw new RuntimeException("User already exists");
        }
        User user = new User(email, password);
        userRepository.save(user);
    }

    //weryfikacja poprawnosci danych (czy uzytkownik o danym emailu istnieje i haslo sie zgadza)
    public boolean authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        return user != null && user.getPassword().equals(password);
    }
}
