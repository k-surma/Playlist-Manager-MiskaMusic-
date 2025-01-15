package com.example.playlistmanager.models;

public class Notification {
    private int id;
    private int userId; // odbiorca powiadomienia
    private int senderId; // nadawca powiadomienia
    private int playlistId;
    private String message;
    private String status; // PENDING, ACCEPTED, REJECTED

    public Notification() {}

    public Notification(int id, int userId, int senderId, int playlistId, String message, String status) {
        this.id = id;
        this.userId = userId;
        this.senderId = senderId;
        this.playlistId = playlistId;
        this.message = message;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getSenderId() { return senderId; }
    public void setSenderId(int senderId) { this.senderId = senderId; }

    public int getPlaylistId() { return playlistId; }
    public void setPlaylistId(int playlistId) { this.playlistId = playlistId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
