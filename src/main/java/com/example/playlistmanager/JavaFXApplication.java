package com.example.playlistmanager;
//uruchamia JavaFX
import com.example.playlistmanager.controllers.MainApp;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import com.example.playlistmanager.utils.URLViewer;

public class JavaFXApplication extends Application {
    private MainApp mainApp;

    //tworzy instancje klasy MainApp i uruchamia kontekst SpringBoota ktory zarządza zależnościami i komponentami aplikacji
    @Override
    public void init() {
        mainApp = new MainApp();
        mainApp.setSpringContext(SpringApplication.run(PlaylistManagerApplication.class));
    }

    //ustawia początkową scenę (widok) na ekran logowania
    @Override
    public void start(Stage stage) throws Exception {
        mainApp.changeScene(stage, "/login-panel.fxml");
        stage.setTitle("Playlist Manager");
        stage.show();
    }

    //zamyka kontekst przy zamykaniu aplikacji
    @Override
    public void stop() {
        URLViewer.stopPlaylist();
        mainApp.getSpringContext().close();
    }
}