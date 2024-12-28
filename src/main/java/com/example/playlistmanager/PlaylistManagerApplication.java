package com.example.playlistmanager;

import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PlaylistManagerApplication {
    public static void main(String[] args) {
        // Uruchomienie Spring Boot
        SpringApplication.run(PlaylistManagerApplication.class, args);

        // Uruchomienie JavaFX
        Application.launch(MainApp.class, args);
    }
}
