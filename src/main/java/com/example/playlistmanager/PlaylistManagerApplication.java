package com.example.playlistmanager;
//punkt startowy aplikacji
import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//uruchamia klase javaFX
@SpringBootApplication
public class PlaylistManagerApplication {
    public static void main(String[] args) {
        Application.launch(JavaFXApplication.class, args);
    }
}