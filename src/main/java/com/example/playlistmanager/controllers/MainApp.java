package com.example.playlistmanager.controllers;
//zarządzanie scenami w aplikacji
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class MainApp extends BaseController {
    private ConfigurableApplicationContext springContext; //przechowuje kontekst aby móc zarządzać zależnościami

    public ConfigurableApplicationContext getSpringContext() {
        return springContext;
    }

    public void setSpringContext(ConfigurableApplicationContext context) {
        this.springContext = context;
    }

    //Ładuje nowy widok FXML, przypisuje do niego kontroler i ustawia go jako aktywny widok w danym oknie.
    public void changeScene(Stage stage, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setControllerFactory(springContext::getBean);
            Parent root = loader.load();

            BaseController controller = loader.getController();
            controller.setMainApp(this);

            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
