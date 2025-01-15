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
        this.sharedPlaylistRepository.initializeDatabase();
    }


    public void sharePlaylist(int playlistId, String recipientEmail) {
        User target_user = userService.findUserByEmail(recipientEmail);
        if (target_user == null) {
            throw new RuntimeException("Użytkownik z podanym emailem nie istnieje.");
        }

        // tworzy wpis w shared_playlists
        SharedPlaylist sharedPlaylist = new SharedPlaylist(0, playlistId, target_user.getId().intValue());
        sharedPlaylistRepository.save(sharedPlaylist);

        // tworzy i wysyła powiadomienie
        String message = "Użytkownik " + userService.findUserById(userService.getLoggedInUserId()).getName()
                + " udostępnił Ci playlistę.";
        notificationService.addNotification(
                target_user.getId().intValue(),
                userService.getLoggedInUserId().intValue(),
                playlistId,
                message
        );
    }

    // pobieranie udostępnionych playlist dla użytkownika docelowego
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