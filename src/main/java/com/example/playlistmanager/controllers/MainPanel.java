package com.example.playlistmanager.controllers;

import com.example.playlistmanager.models.Playlist;
import com.example.playlistmanager.models.Song;
import com.example.playlistmanager.service.PlaylistService;
import com.example.playlistmanager.service.SongService;
import com.example.playlistmanager.service.UserService;
import com.example.playlistmanager.utils.URLViewer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.List;

@Controller
public class MainPanel extends BaseController {

    @FXML
    private TextField emailTextField; // Displays user's email
    @FXML
    private TextField nameTextField; // Displays user's name

    @FXML
    private Button dodajplaylisteButton;
    @FXML
    private Button usunplaylisteButton;
    @FXML
    private Button dodajpiosenkeplaylistaButton;
    @FXML
    private Button usunpiosenkezplaylistyButton;
    @FXML
    private Button wylogujButton;
    @FXML
    private Button odtworzPlaylisteButton;

    @FXML
    private ComboBox<Playlist> playlistyComboBox; // Displays playlists
    @FXML
    private ListView<Song> listapiosenekListView; // Displays songs in a playlist

    @Autowired
    private UserService userService;

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private SongService songService;

    private Long loggedInUserId; // ID of the currently logged-in user

    @FXML
    private void initialize() {
        Platform.runLater(() -> {
            try {
                loggedInUserId = userService.getLoggedInUserId();
                var loggedInUser = userService.findUserById(loggedInUserId);

                if (loggedInUser == null) {
                    System.err.println("Logged-in user data not found.");
                    return;
                }

                emailTextField.setText(loggedInUser.getEmail());
                nameTextField.setText(loggedInUser.getName());

                loadPlaylists();
                playlistyComboBox.setOnAction(event -> loadSongs());
            } catch (RuntimeException e) {
                System.err.println("Error during MainPanel initialization: " + e.getMessage());
                e.printStackTrace();
                // Avoid showing duplicate dialogs
            }
        });
        listapiosenekListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Song selectedSong = listapiosenekListView.getSelectionModel().getSelectedItem();
                if (selectedSong != null) {
                    String videoUrl = selectedSong.getPath();
                    // Otwieranie linku w przeglądarce
                    URLViewer.openUrl(videoUrl);
                } else {
                    showError("Nie wybrano piosenki do odtworzenia.");
                }
            }
        });
    }


    private void loadPlaylists() {
        try {
            List<Playlist> playlists = playlistService.getPlaylistsForUser(loggedInUserId.intValue());
            if (playlists == null || playlists.isEmpty()) {
                System.out.println("No playlists found for the user.");
            }
            playlistyComboBox.setItems(FXCollections.observableArrayList(playlists));
        } catch (Exception e) {
            System.err.println("Error loading playlists: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void loadSongs() {
        Playlist selectedPlaylist = playlistyComboBox.getValue();
        if (selectedPlaylist == null) {
            listapiosenekListView.getItems().clear();
            return;
        }

        try {
            List<Song> songs = songService.getSongsInPlaylist(selectedPlaylist.getName());
            listapiosenekListView.setItems(FXCollections.observableArrayList(songs));
        } catch (Exception e) {
            System.err.println("Error loading songs: " + e.getMessage());
        }
    }


    @FXML
    private void onDodajPlaylisteButton() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Dodaj Playlistę");
        dialog.setHeaderText("Podaj nazwę nowej playlisty:");
        dialog.setContentText("Nazwa playlisty:");

        dialog.showAndWait().ifPresent(name -> {
            if (name.isBlank()) {
                showError("Nazwa playlisty nie może być pusta.");
                return;
            }
            Playlist playlist = new Playlist(0, name, loggedInUserId.intValue());
            playlistService.createPlaylist(playlist);
            loadPlaylists(); // Odśwież listę playlist
            playlistyComboBox.setValue(playlist); // Wybierz nowo dodaną playlistę
        });
    }

    @FXML
    private void onUsunPlaylisteButton() {
        Playlist selectedPlaylist = playlistyComboBox.getValue();
        if (selectedPlaylist != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Czy na pewno chcesz usunąć tę playlistę?", ButtonType.YES, ButtonType.NO);
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    playlistService.deletePlaylist(selectedPlaylist.getId(), selectedPlaylist.getName());
                    loadPlaylists();
                    listapiosenekListView.getItems().clear();
                }
            });
        } else {
            showError("Nie wybrano playlisty do usunięcia.");
        }
    }

    @FXML
    private void onDodajPiosenkePlaylistaButton() {
        Playlist selectedPlaylist = playlistyComboBox.getValue();
        if (selectedPlaylist != null) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Dodaj Piosenkę");
            dialog.setHeaderText("Podaj szczegóły piosenki:");
            dialog.setContentText("Format: tytuł,artysta,ścieżka");

            dialog.showAndWait().ifPresent(input -> {
                String[] details = input.split(",");
                if (details.length == 3) {
                    try {
                        Song song = new Song(0, details[0].trim(), details[1].trim(), details[2].trim());
                        songService.addSongToPlaylist(selectedPlaylist.getName(), song);
                        loadSongs(); // Refresh the list of songs
                    } catch (RuntimeException e) {
                        showError("Wystąpił błąd podczas dodawania piosenki: " + e.getMessage());
                    }
                } else {
                    showError("Nieprawidłowy format danych piosenki. Użyj formatu: tytuł,artysta,ścieżka");
                }
            });
        } else {
            showError("Nie wybrano playlisty.");
        }
    }

    @FXML
    private void onUsunPiosenkeZPlaylistyButton() {
        Playlist selectedPlaylist = playlistyComboBox.getValue();
        Song selectedSong = listapiosenekListView.getSelectionModel().getSelectedItem();
        if (selectedPlaylist != null && selectedSong != null) {
            songService.deleteSongFromPlaylist(selectedPlaylist.getName(), selectedSong.getId());
            loadSongs();
        } else {
            showError("Nie wybrano piosenki do usunięcia.");
        }
    }
    @FXML
    private void onOdtworzPlaylisteButton() {
        List<Song> songs = listapiosenekListView.getItems();
        if (songs == null || songs.isEmpty()) {
            showError("Brak piosenek w playliście.");
            return;
        }
        //URLViewer.stopPlaylist(); // Zatrzymaj poprzednią playlistę
        URLViewer.playPlaylist(songs);
    }

    @FXML
    private void onWylogujButton() {
        userService.logout();
        getMainApp().changeScene((Stage) wylogujButton.getScene().getWindow(), "/login-panel.fxml");
    }

    private void showError(String message) {
        if (message == null || message.isBlank()) {
            System.err.println("Attempted to show an empty error message.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle("Błąd");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
