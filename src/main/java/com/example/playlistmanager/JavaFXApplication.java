package com.example.playlistmanager;

import com.example.playlistmanager.controllers.MainApp;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;

public class JavaFXApplication extends Application {
    private MainApp mainApp;

    @Override
    public void init() {
        mainApp = new MainApp();
        mainApp.setSpringContext(SpringApplication.run(PlaylistManagerApplication.class));
    }

    @Override
    public void start(Stage stage) throws Exception {
        mainApp.changeScene(stage, "/login-panel.fxml");
        stage.setTitle("Playlist Manager");
        stage.show();
    }

    @Override
    public void stop() {
        mainApp.getSpringContext().close();
    }
}