package com.example.playlistmanager.exceptions;

public class InvalidPasswordException extends LoginException {
    public InvalidPasswordException() {
        super("Nieprawidłowe hasło. Prosze spróbować ponownie.");
    }
}
