package com.example.playlistmanager.exceptions;

public class InvalidEmailException extends LoginException {
    public InvalidEmailException() {
        super("Nieprawidłowy email.");
    }
}
