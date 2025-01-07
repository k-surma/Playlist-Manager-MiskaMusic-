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

    @Autowired
    public PlaylistService(PlaylistRepository playlistRepository, SongRepository songRepository) {
        this.playlistRepository = playlistRepository;
        this.songRepository = songRepository;

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



}
