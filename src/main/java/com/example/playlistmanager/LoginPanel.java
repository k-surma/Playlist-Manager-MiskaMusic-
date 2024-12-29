package com.example.playlistmanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.util.regex.Pattern;

public class LoginPanel {
    @FXML
    private TextField mailTextField;
    @FXML
    private TextField hasloTextField;
    @FXML
    private Button zalogujButton;
    @FXML
    private Button anulujButton;

    private MainApp mainApp;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public boolean IsEmailValid(String email) {
        // Sprawdzenie poprawności e-mailu
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }
    public boolean IsPasswordValid(String password) {
        // Sprawdzenie poprawności hasła -> co najmniej 8 liter: małych i dużych, znaków specjalnych lub liter
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";
        Pattern pattern = Pattern.compile(passwordRegex);
        return pattern.matcher(password).matches();
    }

    public void onZalogujButton(){
        String mail = mailTextField.getText();
        String haslo = hasloTextField.getText();

        if (!IsEmailValid(mail)) {
            System.out.println("Niepoprawny mail. Proszę spróbować ponownie.");
            return;
        }

        if (!IsPasswordValid(haslo)) {
            System.out.println("Niepoprawne hasło. Proszę spróbować ponownie.");
            return;
        }
        System.out.println("Poprawne logowanie użytkownika: " + mail);

        // Przejście do głównego panelu
        goToMainAppScreen();
    }

    public void onAnulujButton(){
        Stage stage = (Stage) anulujButton.getScene().getWindow();
        stage.close();
    }
    public void goToMainAppScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainapp.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) zalogujButton.getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
