package com.example.playlistmanager.controllers;
//klasa bazowa dla wszystkich kontrolerów
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public abstract class BaseController {
    private MainApp mainApp;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    protected MainApp getMainApp() {
        return mainApp;
    }

    protected void closeStage(Stage stage) {
        if (stage != null) {
            stage.close();
        }
    }
}


