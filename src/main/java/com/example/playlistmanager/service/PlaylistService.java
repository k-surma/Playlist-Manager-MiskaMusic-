package com.example.playlistmanager.service;

import com.example.playlistmanager.models.Playlist;
import com.example.playlistmanager.repositories.PlaylistRepository;
import com.example.playlistmanager.repositories.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class PlaylistService {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private SongRepository songRepository;

    public void initializeDatabase() {
        playlistRepository.initializeDatabase();
    }

    public void createPLaylist(Playlist playlist) {
        playlistRepository.save(playlist); //zapisuje nowa playliste dla konkretnego uzytkownika
        songRepository.initializeDatabase(playlist.getName()); //inicjalizuje <playlist_name>.db
    }

    public List<Playlist> getPlaylistsForUser(int userId) {
        // pobiera playlisty danego usera
        return playlistRepository.findByUserId(userId);
    }

    public void deletePlaylist(int playlistId, String playlistName) {
        playlistRepository.delete(playlistId);  // usuwa z playlists.db
        // Delete the corresponding <playlist_name>.db file
        File playlistFile = new File(playlistName + ".db");
        if (playlistFile.exists() && playlistFile.isFile()) {
            if (!playlistFile.delete()) {
                throw new RuntimeException("Failed to delete playlist database file: " + playlistFile.getAbsolutePath());
            }
        }
    }
}
