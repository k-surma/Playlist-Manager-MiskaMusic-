package com.example.playlistmanager.models;

public class Song {
    private int id;
    private String title;
    private String artist;
    //private String album;
    //private String genre;
    private int duration;
    private String path;


    public Song(int id, String title, String artist, int duration, String path) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.path = path;
    }

    public int getId() {return id;}
    public void setId(int id) {}
    public String getTitle() {return title;}
    public void setTitle(String title) {}
    public String getArtist() {return artist;}
    public void setArtist(String artist) {}
    public int getDuration() {return duration;}
    public void setDuration(int duration) {}
    public String getPath() {return path;}
    public void setPath(String path) {}

    ////IMPORTANT!!!! REMEMBER TO CREATE A METHOD FOR FORMATTING

}
