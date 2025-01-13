package com.example.playlistmanager.exceptions;

public class NameException extends LoginException {
    public NameException() {
        super("Imię nie może być puste. Wprowadź ponownie.");
    }
}
