package com.example.playlistmanager.models;

public class Song {
    private int id;
    private String title;
    private String artist;
    //private int duration;
    private String path;

    public Song(int id, String title, String artist, String path) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        //this.duration = duration;
        this.path = path;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }
    //public int getDuration() { return duration; }
    //public void setDuration(int duration) { this.duration = duration; }
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    @Override
    public String toString() {
        return "\"" + title + "\" - " + artist;
    }

}
