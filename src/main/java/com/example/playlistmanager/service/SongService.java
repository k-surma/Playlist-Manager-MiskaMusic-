package com.example.playlistmanager.service;

import com.example.playlistmanager.models.Song;
import com.example.playlistmanager.repositories.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongService {

    @Autowired
    private SongRepository songRepository;

    public void initializeDatabase(String playlistName) {
        songRepository.initializeDatabase(playlistName);
    }

    public void addSongToPlaylist(String playlistName, Song song) {
        if (song.getTitle() == null || song.getTitle().isBlank() ||
                song.getArtist() == null || song.getArtist().isBlank() ||
                song.getPath() == null || song.getPath().isBlank()) {
            throw new IllegalArgumentException("Dane piosenki są nieprawidłowe.");
        }
        songRepository.save(playlistName, song);
    }


    public List<Song> getSongsInPlaylist(String playlistName) {
        return songRepository.findAll(playlistName);
    }

    public void deleteSongFromPlaylist(String playlistName, int songId) {
        songRepository.delete(playlistName, songId);
    }
}
