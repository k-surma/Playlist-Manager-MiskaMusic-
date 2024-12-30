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
public class LoginPanel extends BaseController {
    @FXML
    private TextField mailTextField;
    @FXML
    private TextField hasloTextField;
    @FXML
    private Button zalogujButton;
    @FXML
    private Button anulujButton;
    @FXML
    private Button zarejestrujButton;

    @Autowired
    private UserService userService;

    public void onZalogujButton() {
        String mail = mailTextField.getText();
        String haslo = hasloTextField.getText();

        if (!ValidationUtils.isEmailValid(mail) || !ValidationUtils.isPasswordValid(haslo)) {
            System.out.println("Nieprawidłowy email lub hasło");
            return;
        }

        try {
            if (userService.authenticateUser(mail, haslo)) {
                System.out.println("Udało się zalogować pomyślnie");
                goToMainAppScreen();
            } else {
                System.out.println("Nieprawidłowe dane logowania");
            }
        } catch (Exception e) {
            System.out.println("Błąd logowania: " + e.getMessage());
        }
    }

    public void onZarejestrujButton() {
        getMainApp().changeScene((Stage) zarejestrujButton.getScene().getWindow(), "/rejestracja-panel.fxml");
    }

    public void goToMainAppScreen() {
        getMainApp().changeScene((Stage) zalogujButton.getScene().getWindow(), "/mainapp.fxml");
    }


    public void onAnulujButton() {
        closeStage((Stage) anulujButton.getScene().getWindow());
    }

}
