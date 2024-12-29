package com.example.playlistmanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        //Button btn = new Button("Hello, Playlist Manager!");
        //btn.setOnAction(e -> System.out.println("Button clicked!"));

        //StackPane root = new StackPane();
        //root.getChildren().add(btn);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login-panel.fxml"));
        Parent root = loader.load();

        LoginPanel controller = loader.getController();
        controller.setMainApp(this);

        //Scene scene = new Scene(root, 800, 600);
        Scene scene = new Scene(root);
        stage.setTitle("Playlist Manager");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}