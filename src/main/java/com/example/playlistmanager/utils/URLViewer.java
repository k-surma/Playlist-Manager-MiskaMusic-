package com.example.playlistmanager.utils;
import com.example.playlistmanager.models.Song;
import java.util.List;
import java.io.IOException;

public class URLViewer {
    private static Process currentBrowserProcess = null;
    private static volatile boolean isPlaying = false;
    private static Thread playlistThread = null; // wątek, który działą w tle; odpowiedzialny za odtwarzanie playlisty

    public static void openUrl(String url) {
        try {
            closeCurrentBrowser();

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
                // /F - wymusza zamknięcie procesu, /T - zamyka proces główny i wszystkie potomne, /IM - zamyka plik exe procesu
                ProcessBuilder taskkill = new ProcessBuilder("taskkill", "/F", "/IM", "chrome.exe", "/T");
                Process killProcess = taskkill.start();
                killProcess.waitFor(); // czeka na zakończenie procesu zamykania
                currentBrowserProcess.destroy();
                currentBrowserProcess = null;
                Thread.sleep(1000); // krótka pauza dla pewności
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

                        // 3 minuty między utworami
                        for (int i = 0; i < 180 && isPlaying; i++) {
                            Thread.sleep(1000);
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
        });

        playlistThread.setDaemon(true); // pozwala na dalszą pracę głównego programu (zapobiega blokowaniu do skończenia wątku)
        playlistThread.start();
    }

}