package com.example.playlistmanager.models;

import java.util.ArrayList;
import java.util.List;

public class Playlist {
    private int id;
    private String name;
    private int userId;
    private List<Song> songs;

    public Playlist(int id, String name, int userId) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.songs = new ArrayList<>();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public List<Song> getSongs() { return songs; }

    @Override
    public String toString() {
        return name;
    }

}
