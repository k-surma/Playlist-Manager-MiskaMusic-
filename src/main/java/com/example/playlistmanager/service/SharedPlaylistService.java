package com.example.playlistmanager.service;

import com.example.playlistmanager.models.SharedPlaylist;
import com.example.playlistmanager.repositories.SharedPlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SharedPlaylistService {

    private final SharedPlaylistRepository sharedPlaylistRepository;

    @Autowired
    public SharedPlaylistService(SharedPlaylistRepository sharedPlaylistRepository) {
        this.sharedPlaylistRepository = sharedPlaylistRepository;
        this.sharedPlaylistRepository.initializeDatabase();
    }

    public void sharePlaylist(int playlistId, int sharedWithUserId) {
        SharedPlaylist sharedPlaylist = new SharedPlaylist(0, playlistId, sharedWithUserId);
        sharedPlaylistRepository.save(sharedPlaylist);
    }

    public List<SharedPlaylist> getSharedPlaylistsForUser(int userId) {
        return sharedPlaylistRepository.findByUserId(userId);
    }
}
