package com.example.playlistmanager.service;

import com.example.playlistmanager.models.Playlist;
import com.example.playlistmanager.models.SharedPlaylist;
import com.example.playlistmanager.repositories.PlaylistRepository;
import com.example.playlistmanager.repositories.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class PlaylistService {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private SharedPlaylistService sharedPlaylistService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;


    @Autowired
    public PlaylistService(PlaylistRepository playlistRepository, SongRepository songRepository,
                           SharedPlaylistService sharedPlaylistService, NotificationService notificationService,
                           UserService userService) {
        this.playlistRepository = playlistRepository;
        this.songRepository = songRepository;
        this.sharedPlaylistService = sharedPlaylistService;
        this.notificationService = notificationService;
        this.userService = userService;

        // Inicjalizuje bazę danych przy uruchomieniu serwisu
        playlistRepository.initializeDatabase();
    }


    public void createPlaylist(Playlist playlist) {
        playlistRepository.save(playlist); // Zapisuje playlistę w playlists.db
        songRepository.initializeDatabase(playlist.getName()); // Inicjalizuje bazę danych dla playlisty
        System.out.println("Zainicjalizowano bazę danych dla playlisty: " + playlist.getName());
    }



    public List<Playlist> getPlaylistsForUser(int userId) {
        return playlistRepository.findByUserId(userId); // Retrieve playlists for the user
    }

    public void deletePlaylist(int playlistId, String playlistName) {
        playlistRepository.delete(playlistId); // Remove from playlists.db
        File playlistFile = new File(playlistName + ".db");
        if (playlistFile.exists() && playlistFile.isFile()) {
            if (!playlistFile.delete()) {
                System.err.println("Failed to delete playlist database file: " + playlistFile.getAbsolutePath());
            }
        } else {
            System.err.println("Playlist file not found: " + playlistFile.getAbsolutePath());
        }
    }

    // Nowa metoda do udostępniania playlist bez accessLevel
    public void sharePlaylist(int playlistId, String recipientEmail) {
        // Znajdź użytkownika odbiorcy po emailu
        com.example.playlistmanager.models.User recipient = userService.findUserByEmail(recipientEmail);
        if (recipient == null) {
            throw new RuntimeException("Użytkownik z podanym emailem nie istnieje.");
        }

        // Utwórz wpis w shared_playlists
        sharedPlaylistService.sharePlaylist(playlistId, recipient.getId().intValue());

        // Utwórz wiadomość powiadomienia
        String message = "Użytkownik " + userService.findUserById(userService.getLoggedInUserId()).getName()
                + " udostępnił Ci playlistę.";

        // Dodaj powiadomienie (bez accessLevel)
        notificationService.addNotification(recipient.getId().intValue(),
                userService.getLoggedInUserId().intValue(),
                playlistId,
                message);
    }

    // Dodana metoda do pobierania udostępnionych playlist dla użytkownika
    public List<Playlist> getSharedPlaylistsForUser(int userId) {
        List<SharedPlaylist> sharedPlaylists = sharedPlaylistService.getSharedPlaylistsForUser(userId);
        List<Playlist> playlists = new ArrayList<>();

        for (SharedPlaylist sharedPlaylist : sharedPlaylists) {
            Playlist playlist = playlistRepository.findById(sharedPlaylist.getPlaylistId());
            if (playlist != null) {
                // Opcjonalnie możesz dodać informacje o poziomie dostępu do obiektu Playlist
                // np. poprzez rozszerzenie modelu Playlist lub utworzenie specjalnego DTO
                playlists.add(playlist);
            }
        }

        return playlists;
    }


    public Playlist getPlaylistById(int playlistId) {
        // Zakładam, że PlaylistRepository ma metodę findById
        return playlistRepository.findById(playlistId);
    }

    public Playlist getPlaylistByName(String playlistName) {
        return playlistRepository.findByName(playlistName);
    }

}
