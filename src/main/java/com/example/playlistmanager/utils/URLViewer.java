package com.example.playlistmanager.utils;
import com.example.playlistmanager.models.Song;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.io.IOException;

public class URLViewer {
    private static Process currentBrowserProcess = null;
    private static volatile boolean isPlaying = false;
    private static Thread playlistThread = null;

    public static void openUrl(String url) {
        try {
            // Najpierw zamknij poprzedni proces przeglądarki
            closeCurrentBrowser();

            // Utwórz nowy proces przeglądarki
            ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "start", "chrome", url);
            currentBrowserProcess = pb.start();

        } catch (IOException e) {
            System.err.println("Nie udało się otworzyć URL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void closeCurrentBrowser() {
        if (currentBrowserProcess != null) {
            try {
                // Zamknij poprzednią kartę
                ProcessBuilder taskkill = new ProcessBuilder("taskkill", "/F", "/IM", "chrome.exe", "/T");
                Process killProcess = taskkill.start();
                killProcess.waitFor(); // Poczekaj na zakończenie procesu zamykania
                currentBrowserProcess.destroy();
                currentBrowserProcess = null;
                Thread.sleep(1000); // Krótka pauza dla pewności
            } catch (Exception e) {
                System.err.println("Błąd podczas zamykania przeglądarki: " + e.getMessage());
            }
        }
    }

    public static void stopPlaylist() {
        isPlaying = false;
        if (playlistThread != null) {
            playlistThread.interrupt();
            playlistThread = null;
        }
        closeCurrentBrowser();
    }

    public static void playPlaylist(List<Song> songs) {
        if (songs == null || songs.isEmpty()) {
            System.err.println("Brak piosenek w playliście.");
            return;
        }

        // Zatrzymaj aktualnie odtwarzaną playlistę
        stopPlaylist();
        isPlaying = true;

        playlistThread = new Thread(() -> {
            for (Song song : songs) {
                if (!isPlaying) {
                    break;
                }
                try {
                    String videoUrl = song.getPath();
                    if (videoUrl != null && !videoUrl.isBlank()) {
                        System.out.println("Odtwarzanie: " + song.getTitle());
                        openUrl(videoUrl);

                        // Czekaj określony czas przed następnym utworem
                        for (int i = 0; i < 180 && isPlaying; i++) {
                            Thread.sleep(1000); // Czekaj po sekundzie, żeby móc szybciej przerwać
                        }
                    }
                } catch (InterruptedException e) {
                    System.out.println("Odtwarzanie playlisty przerwane.");
                    break;
                } catch (Exception e) {
                    System.err.println("Błąd podczas otwierania linku: " + e.getMessage());
                }
            }
            isPlaying = false;
            System.out.println("Playlista zakończona.");
            closeCurrentBrowser();
        });

        playlistThread.setDaemon(true);
        playlistThread.start();
    }
}