//package com.example.playlistmanager;
//
//import com.example.playlistmanager.controllers.MainApp;
//import javafx.application.Application;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.domain.EntityScan;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//
//@SpringBootApplication
//@EnableJpaRepositories
//@EntityScan("com.example.playlistmanager.models")
//public class PlaylistManagerApplication {
//    public static void main(String[] args) {
//        // Uruchomienie Spring Boot
//        //SpringApplication.run(PlaylistManagerApplication.class, args);
//
//        // Uruchomienie JavaFX
//        Application.launch(MainApp.class, args);
//    }
//}


package com.example.playlistmanager;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PlaylistManagerApplication {
    public static void main(String[] args) {
        Application.launch(JavaFXApplication.class, args);
    }
}

