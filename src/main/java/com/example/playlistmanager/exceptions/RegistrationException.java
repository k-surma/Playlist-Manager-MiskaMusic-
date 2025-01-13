package com.example.playlistmanager.exceptions;

public class RegistrationException extends RuntimeException {
    public RegistrationException() {
        super("Rejestracja nowego użytkownika nie powiodła się.");
    }
}
