package com.example.playlistmanager.controllers;

import com.example.playlistmanager.exceptions.*;
import com.example.playlistmanager.service.UserService;
import com.example.playlistmanager.utils.ValidationUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class RejestracjaPanel extends BaseController {
    @FXML
    private TextField nameTextField;
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

    @FXML
    public void onZarejestrujButton() {
        String name = nameTextField.getText();
        String email = mailTextField.getText();
        String password = hasloTextField.getText();
        String confirmPassword = potwierdzonehasloTextField.getText();

        try {
            if (name == null || name.isBlank()) {
                throw new NameException();
            }
            if (!ValidationUtils.isEmailValid(email)) {
                throw new InvalidEmailException();
            }
            if (!ValidationUtils.isPasswordValid(password)) {
                throw new InvalidPasswordException();
            }
            if (!password.equals(confirmPassword)) {
                throw new DifferentPasswordException();
            }

            userService.registerUser(email, password, name);

            if (userService.authenticateUser(email, password)) {
                showSuccessDialog("Rejestracja zakończona sukcesem.");
                goToMainAppScreen();
            } else {
                throw new RegistrationException();
            }
        } catch (Exception e) {
            showErrorDialog("Błąd rejestracji: " + e.getMessage());
        }
    }


    @FXML
    public void onAnulujButton() {
        Stage stage = (Stage) anulujButton.getScene().getWindow();
        closeStage(stage);
    }

    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sukces");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void goToMainAppScreen() {
        getMainApp().changeScene((Stage) zarejestrujButton.getScene().getWindow(), "/main-panel.fxml");
    }
}
