package com.example.playlistmanager.exceptions;

public class AuthenticationFailedException extends LoginException {
    public AuthenticationFailedException() {
        super("Nieprawidłowe dane logowania.");
    }
}
