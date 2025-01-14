package com.example.playlistmanager.models;

public class SharedPlaylist {
    private int id;
    private int playlistId;
    private int sharedWithUserId;

    public SharedPlaylist() {}

    public SharedPlaylist(int id, int playlistId, int sharedWithUserId) {
        this.id = id;
        this.playlistId = playlistId;
        this.sharedWithUserId = sharedWithUserId;
    }

    // Gettery i settery
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPlaylistId() { return playlistId; }
    public void setPlaylistId(int playlistId) { this.playlistId = playlistId; }

    public int getSharedWithUserId() { return sharedWithUserId; }
    public void setSharedWithUserId(int sharedWithUserId) { this.sharedWithUserId = sharedWithUserId; }

}
