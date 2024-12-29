package com.example.playlistmanager.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.util.regex.Pattern;

public class RejestracjaPanel extends BaseController{
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


//    public boolean IsEmailValid(String email) {
//        // Sprawdzenie poprawności e-mailu
//        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
//        Pattern pattern = Pattern.compile(emailRegex);
//        return pattern.matcher(email).matches();
//    }
//
//    public boolean IsPasswordValid(String password) {
//        // Sprawdzenie poprawności hasła -> co najmniej 8 liter: małych i dużych, znaków specjalnych lub liter
//        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";
//        Pattern pattern = Pattern.compile(passwordRegex);
//        return pattern.matcher(password).matches();
//    }

    public void onZarejestrujButton() {
        String mail = mailTextField.getText();
        String haslo = hasloTextField.getText();
        String potwierdzonehaslo = potwierdzonehasloTextField.getText();

        if (!ValidationUtils.isEmailValid(mail)) {
            System.out.println("Niepoprawny mail. Proszę spróbować ponownie.");
            return;
        }

        if (!ValidationUtils.isPasswordValid(haslo)) {
            System.out.println("Niepoprawne hasło. Proszę spróbować ponownie.");
            return;
        }

        if(!haslo.equals(potwierdzonehaslo)) {
            System.out.println("Błąd! Proszę spróbować napisać hasło ponownie.");
            return;
        }
        System.out.println("Poprawne logowanie użytkownika: " + mail);

        goToMainAppScreen();
    }

    public void onAnulujButton() {
        closeStage((Stage) anulujButton.getScene().getWindow());
    }

    public void goToMainAppScreen() {
        navigateToScreen((Stage) zarejestrujButton.getScene().getWindow(), "/mainapp.fxml");
    }
}
