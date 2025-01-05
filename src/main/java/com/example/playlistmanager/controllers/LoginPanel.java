package com.example.playlistmanager.controllers;

import com.example.playlistmanager.exceptions.AuthenticationFailedException;
import com.example.playlistmanager.exceptions.InvalidEmailException;
import com.example.playlistmanager.exceptions.InvalidPasswordException;
import com.example.playlistmanager.exceptions.LoginException;
import com.example.playlistmanager.service.UserService;
import com.example.playlistmanager.models.User;
import com.example.playlistmanager.utils.ValidationUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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

        try {
            if (!ValidationUtils.isEmailValid(mail)) {
                throw new InvalidEmailException();
            }
            if (!ValidationUtils.isPasswordValid(haslo)) {
                throw new InvalidPasswordException();
            }

            if (userService.authenticateUser(mail, haslo)) {
                System.out.println("Udało się zalogować pomyślnie");
                goToMainAppScreen();
            } else {
                throw new AuthenticationFailedException();
            }
        } catch (InvalidEmailException e) {
            showErrorDialog("Nieprawidłowy adres e-mail.");
        } catch (InvalidPasswordException e) {
            showErrorDialog("Nieprawidłowe hasło. Upewnij się, że spełnia wymagania.");
        } catch (AuthenticationFailedException e) {
            showErrorDialog("Nieprawidłowe dane logowania.");
        //} catch (LoginException e) {
        //    showErrorDialog("Wystąpił problem z logowaniem: " + e.getMessage());
        //} catch (Exception e) {
        //    showErrorDialog("Nieoczekiwany błąd: " + e.getMessage());
        }
    }


    public void onZarejestrujButton() {
        getMainApp().changeScene((Stage) zarejestrujButton.getScene().getWindow(), "/rejestracja-panel.fxml");
    }

    public void goToMainAppScreen() {
        getMainApp().changeScene((Stage) zalogujButton.getScene().getWindow(), "/main-panel.fxml");
    }


    public void onAnulujButton() {
        closeStage((Stage) anulujButton.getScene().getWindow());
    }

    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd logowania");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
