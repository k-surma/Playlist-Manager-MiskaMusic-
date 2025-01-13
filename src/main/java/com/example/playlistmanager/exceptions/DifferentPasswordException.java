package com.example.playlistmanager.exceptions;

public class DifferentPasswordException extends RuntimeException {
    public DifferentPasswordException() {
        super("Niezgodne hasła. Proszę spróbować ponownie.");
    }
}
