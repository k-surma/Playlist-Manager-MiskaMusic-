package com.example.playlistmanager.service;

import com.example.playlistmanager.models.Playlist;
import com.example.playlistmanager.models.SharedPlaylist;
import com.example.playlistmanager.models.User;
import com.example.playlistmanager.repositories.SharedPlaylistRepository;
import com.example.playlistmanager.repositories.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SharedPlaylistService {

    private final SharedPlaylistRepository sharedPlaylistRepository;
    private final PlaylistRepository playlistRepository;
    private final NotificationService notificationService;
    private final UserService userService;

    @Autowired
    public SharedPlaylistService(SharedPlaylistRepository sharedPlaylistRepository,
                                 PlaylistRepository playlistRepository,
                                 NotificationService notificationService,
                                 UserService userService) {
        this.sharedPlaylistRepository = sharedPlaylistRepository;
        this.playlistRepository = playlistRepository;
        this.notificationService = notificationService;
        this.userService = userService;

// Inicjalizacja bazy danych
        this.sharedPlaylistRepository.initializeDatabase();
    }

    // Udostępnianie playlisty
    public void sharePlaylist(int playlistId, String recipientEmail) {
// Znajdź użytkownika odbiorcę po emailu
        User recipient = userService.findUserByEmail(recipientEmail);
        if (recipient == null) {
            throw new RuntimeException("Użytkownik z podanym emailem nie istnieje.");
        }

// Utwórz wpis w shared_playlists
        SharedPlaylist sharedPlaylist = new SharedPlaylist(0, playlistId, recipient.getId().intValue());
        sharedPlaylistRepository.save(sharedPlaylist);

// Utwórz i wyślij powiadomienie
        String message = "Użytkownik " + userService.findUserById(userService.getLoggedInUserId()).getName()
                + " udostępnił Ci playlistę.";
        notificationService.addNotification(
                recipient.getId().intValue(),
                userService.getLoggedInUserId().intValue(),
                playlistId,
                message
        );
    }

    // Pobieranie udostępnionych playlist dla użytkownika
    public List<Playlist> getSharedPlaylistsForUser(int userId) {
        List<SharedPlaylist> sharedPlaylists = sharedPlaylistRepository.findByUserId(userId);
        List<Playlist> playlists = new ArrayList<>();

        for (SharedPlaylist sharedPlaylist : sharedPlaylists) {
            Playlist playlist = playlistRepository.findById(sharedPlaylist.getPlaylistId());
            if (playlist != null) {
                playlists.add(playlist);
            }
        }
        return playlists;
    }
}