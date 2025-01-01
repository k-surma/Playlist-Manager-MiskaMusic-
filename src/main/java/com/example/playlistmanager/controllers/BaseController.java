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
    //przełącza widok na nowy plik FXML, obsługując Spring Dependency Injection (DI), by wstrzyknąć odpowiednie kontrolery
    protected void navigateToScreen(Stage stage, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setControllerFactory(mainApp.getSpringContext()::getBean); // zapewnia DI
            Parent root = loader.load();

            BaseController controller = loader.getController();
            controller.setMainApp(mainApp); // przekazywanie dostępu do MainApp do kontrolera, co pozwala na zarządzanie widokami

            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


