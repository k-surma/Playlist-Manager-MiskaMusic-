package com.example.playlistmanager.controllers;

import com.example.playlistmanager.service.UserService;
import com.example.playlistmanager.utils.ValidationUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


@Controller
public class RejestracjaPanel extends BaseController {
    @FXML
    private TextField mailTextField;
    @FXML
    private TextField hasloTextField;
    @FXML
    private TextField potwierdzonehasloTextField;
    @FXML
    private Button zarejestrujButton;
    @FXML
    private Button anulujButton;

    @Autowired
    private UserService userService;

    public void onZarejestrujButton() {
        String mail = mailTextField.getText();
        String haslo = hasloTextField.getText();
        String potwierdzonehaslo = potwierdzonehasloTextField.getText();

        if (!ValidationUtils.isEmailValid(mail)) {
            System.out.println("Nieprawidłowy format emaila");
            return;
        }

        if (!ValidationUtils.isPasswordValid(haslo)) {
            System.out.println("Hasło nie spełnia wymagań");
            return;
        }

        if (!haslo.equals(potwierdzonehaslo)) {
            System.out.println("Hasła nie są identyczne");
            return;
        }

        try {
            userService.registerUser(mail, haslo);
            System.out.println("Zarejestrowano pomyślnie");
            goToMainAppScreen();
        } catch (Exception e) {
            System.out.println("Błąd rejestracji: " + e.getMessage());
        }
    }

    public void onAnulujButton() {
        Stage stage = (Stage) anulujButton.getScene().getWindow();
        closeStage(stage); // Close the current window
    }

    public void goToMainAppScreen() {
        getMainApp().changeScene((Stage) zarejestrujButton.getScene().getWindow(), "/mainapp.fxml");
    }
}
