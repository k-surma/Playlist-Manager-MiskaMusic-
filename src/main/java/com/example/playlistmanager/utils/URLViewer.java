package com.example.playlistmanager.utils;

import com.example.playlistmanager.models.Song;

import java.util.List;

public class URLViewer {

    public static void openUrl(String url) {
        try {
            //URI uri = new URI(url);
            Runtime.getRuntime().exec(new String[] {"cmd", "/c", "start", url});
        } catch (Exception e) {
            System.err.println("Nie udało się otworzyć URL: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public static void playPlaylist(List<Song> songs) {
        if (songs == null || songs.isEmpty()) {
            System.err.println("Brak piosenek w playliście.");
            return;
        }

        new Thread(() -> {
            for (Song song : songs) {
                try {
                    String videoUrl = song.getPath();
                    if (videoUrl != null && !videoUrl.isBlank()) {
                        // Otwórz link w przeglądarce
                        openUrl(videoUrl);

                        // Czekaj np. 3 minuty (180 000 ms) przed otwarciem następnego
                        Thread.sleep(180_000); // Czas trwania utworu w milisekundach
                    }
                } catch (InterruptedException e) {
                    System.err.println("Odtwarzanie playlisty przerwane.");
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    System.err.println("Błąd podczas otwierania linku: " + e.getMessage());
                }
            }
        }).start();
    }

}
