package com.example.playlistmanager.controllers;

import com.example.playlistmanager.service.UserService;
import com.example.playlistmanager.utils.ValidationUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class MainPanel extends BaseController{

    @FXML
    private TextField emailTextField;
    @FXML
    private TextField nameTextField;

    @FXML
    private Button dodajplaylisteButton;
    @FXML
    private Button usunplaylisteButton;
    @FXML
    private Button dodajpiosenkebazaButton;
    @FXML
    private Button udostepnijButton;
    @FXML
    private Button wylogujButton;
    @FXML
    private Button dodajpiosenkeplaylistaButton;
    @FXML
    private Button usunpiosenkezplaylistyButton;

    @FXML
    private ComboBox playlistyComboBox;
    @FXML
    private ListView listapiosenekListView;

    public void onWylogujButton() {
        getMainApp().changeScene((Stage) wylogujButton.getScene().getWindow(), "/login-panel.fxml");
    }
}
