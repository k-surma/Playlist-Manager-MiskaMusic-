package com.example.playlistmanager.models;

import java.util.ArrayList;
import java.util.List;

public class Playlist {
    private int id;                // Unique identifier for the playlist (primary key in playlists.db)
    private String name;
    private int userId;            // Owner of the playlist (foreign key to users.db)
    private List<Song> songs;      // In-memory list of songs for this playlist

    // Constructor
    public Playlist(int id, String name, int userId) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.songs = new ArrayList<>();
    }

    // Getters and Setters
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public int getUserId() {return userId;}
    public void setUserId(int userId) {this.userId = userId;}
    public List<Song> getSongs() {return songs;}

    //IMPORTANT!!!! REMEMBER TO CREATE A METHOD FOR FORMATTING


}
