package com.example.playlistmanager.controllers;

import com.example.playlistmanager.models.Notification;
import com.example.playlistmanager.models.Playlist;
import com.example.playlistmanager.models.Song;
import com.example.playlistmanager.service.*;
import com.example.playlistmanager.utils.URLViewer;
import com.example.playlistmanager.utils.ValidationUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class MainPanel extends BaseController {

    @FXML
    private TextField emailTextField;
    @FXML
    private TextField nameTextField;

    @FXML
    private Button wylogujButton;

    @FXML
    private ComboBox<Playlist> playlistyComboBox;
    @FXML
    private ListView<Song> listapiosenekListView;

    @Autowired
    private UserService userService;

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private SharedPlaylistService sharedPlaylistService;

    @Autowired
    private SongService songService;

    @Autowired
    private NotificationService notificationService;

    private Long loggedInUserId;

    @FXML
    private void initialize() {
        Platform.runLater(() -> {
            try {
                loggedInUserId = userService.getLoggedInUserId();
                var loggedInUser = userService.findUserById(loggedInUserId);

                if (loggedInUser == null) {
                    System.err.println("Nie znaleziono zalogowanego użytkownika!");
                    return;
                }

                emailTextField.setText(loggedInUser.getEmail());
                nameTextField.setText(loggedInUser.getName());

                loadAllPlaylists();
                loadNotifications();
                playlistyComboBox.setOnAction(event -> loadSongs());
            } catch (RuntimeException e) {
                System.err.println("Błąd przy inicjalizacji MainPanel: " + e.getMessage());
                e.printStackTrace();
            }
        });

        listapiosenekListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Song selectedSong = listapiosenekListView.getSelectionModel().getSelectedItem();
                if (selectedSong != null) {
                    URLViewer.stopPlaylist();
                    String videoUrl = selectedSong.getPath();
                    URLViewer.openUrl(videoUrl);
                } else {
                    showError("Nie wybrano piosenki do odtworzenia.");
                }
            }
        });
    }

    private void loadAllPlaylists() {
        try {
            List<Playlist> userPlaylists = playlistService.getPlaylistsForUser(loggedInUserId.intValue());
            List<Playlist> sharedPlaylists = sharedPlaylistService.getSharedPlaylistsForUser(loggedInUserId.intValue());

            if (userPlaylists != null && !userPlaylists.isEmpty()) {
                playlistyComboBox.getItems().addAll(userPlaylists);
            }

            if (sharedPlaylists != null && !sharedPlaylists.isEmpty()) {
                for (Playlist sharedPlaylist : sharedPlaylists) {
                    if (!playlistyComboBox.getItems().contains(sharedPlaylist)) {
                        playlistyComboBox.getItems().add(sharedPlaylist);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Błąd przy załadowaniu playlist: " + e.getMessage());
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
            System.err.println("Błąd przy załadowywaniu piosenek: " + e.getMessage());
        }
    }

    public void stopPlaying() {
        URLViewer.stopPlaylist();
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
            loadAllPlaylists();
            playlistyComboBox.setValue(playlist);
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
                    loadAllPlaylists();
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
        URLViewer.playPlaylist(songs);
    }

    @FXML
    private void onWylogujButton() {
        userService.logout();
        getMainApp().changeScene((Stage) wylogujButton.getScene().getWindow(), "/login-panel.fxml");
    }

    @FXML
    private void onUdostepnijPlaylisteButton() {
        Playlist selectedPlaylist = playlistyComboBox.getValue();
        if (selectedPlaylist == null) {
            showError("Nie wybrano playlisty do udostępnienia.");
            return;
        }

        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Udostępnij Playlistę");
        dialog.setHeaderText("Udostępnianie playlisty: " + selectedPlaylist.getName());

        ButtonType udostepnijButtonType = new ButtonType("Udostępnij", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(udostepnijButtonType, ButtonType.CANCEL);

        // ustawienie zawartości dialogu
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField emailField = new TextField();
        emailField.setPromptText("Email odbiorcy");

        grid.add(new Label("Email odbiorcy:"), 0, 0);
        grid.add(emailField, 1, 0);

        dialog.getDialogPane().setContent(grid);

        // konwertowanie wyniku dialogu na email
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == udostepnijButtonType) {
                return emailField.getText();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(userEmail -> {
            if (userEmail.isBlank() || !ValidationUtils.isEmailValid(userEmail)) {
                showError("Proszę podać prawidłowy adres email.");
                return;
            }

            sharedPlaylistService.sharePlaylist(selectedPlaylist.getId(), userEmail);
            showInfo("Playlista została udostępniona.");

        });
    }

    private void loadNotifications() {
        try {
            List<Notification> notifications = notificationService.getPendingNotificationsForUser(loggedInUserId.intValue());
            for (Notification notification : notifications) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Nowa Playlistę");
                alert.setHeaderText(notification.getMessage());
                alert.setContentText("Czy chcesz zaakceptować udostępnienie playlisty?");

                ButtonType acceptButton = new ButtonType("Akceptuj");
                ButtonType rejectButton = new ButtonType("Odrzuć", ButtonBar.ButtonData.CANCEL_CLOSE);

                alert.getButtonTypes().setAll(acceptButton, rejectButton);

                alert.showAndWait().ifPresent(response -> {
                    if (response == acceptButton) {
//                        Playlist sharedPlaylist = playlistService.getPlaylistById(notification.getPlaylistId());
//                        if (sharedPlaylist != null) {
//                            playlistyComboBox.getItems().add(sharedPlaylist);
//                        }
                        notificationService.updateNotificationStatus(notification.getId(), "ACCEPTED");
                        showInfo("Playlista została dodana do Twoich playlist.");
                    } else if (response == rejectButton) {
                        notificationService.updateNotificationStatus(notification.getId(), "REJECTED");
                        showInfo("Udostępnienie playlisty zostało odrzucone.");
                    }
                });
            }
        } catch (Exception e) {
            System.err.println("Error loading notifications: " + e.getMessage());
            e.printStackTrace();
        }
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

    private void showInfo(String message) {
        if (message == null || message.isBlank()) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle("Informacja");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
