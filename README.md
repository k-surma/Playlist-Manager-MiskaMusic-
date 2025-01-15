# Playlist Manager

Aplikacja **Playlist Manager** umożliwia zarządzanie playlistami muzycznymi. Póki co lokalnie, jednak będzie rozbudowywany o funkcjonalność w formie aplikacji webowej.

Aplikacja umożliwia:
- Rejestrowanie i logowanie użytkowników
- Tworzenie nowych playlist
- Dodawanie/Usuwanie piosenek
- Udostępnianie playlist innym użytkownikom
- Odtwarzanie utworów w przeglądarce (za pomocą JavaFX i `URLViewer`)

## Spis treści
1. [Technologie](#technologie)
2. [Struktura projektu](#struktura-projektu)
3. [Funkcjonalności](#funkcjonalności)
4. [Wymagania](#wymagania)
5. [Uruchomienie aplikacji](#uruchomienie-aplikacji)
6. [Autor](#autor)

---

## Technologie
- **JavaFX** – interfejs graficzny
- **Spring Boot** – kontekst, zarządzanie beanami i główny punkt startowy
- **SQLite** – przechowywanie danych (kilka baz: \*.db dla playlist, użytkowników, powiadomień itp.)
- **Maven** – zarządzanie zależnościami
- **PlantUML** – (opcjonalnie) do generowania diagramów
- **Chrome** – (opcjonalnie) do otwierania linków w `URLViewer`

---

## Struktura projektu
1. **`controllers`** – Zawiera klasy kontrolerów JavaFX (`MainPanel`, `LoginPanel`, `RejestracjaPanel`, `MainApp`) do obsługi widoków i zdarzeń użytkownika.
2. **`models`** – Proste klasy modelu (`Playlist`, `Song`, `User` itd.) przechowujące dane.
3. **`repositories`** – Warstwa dostępu do danych w bazach SQLite (np. `UserRepository`, `PlaylistRepository`).
4. **`service`** – Logika biznesowa (np. `UserService`, `SongService`, `PlaylistService`); łączy kontrolery z repozytoriami.
5. **`utils`** – Klasy pomocnicze (`URLViewer` do otwierania linków w przeglądarce, `ValidationUtils` do walidacji danych).
6. **`PlaylistManagerApplication`** – Główny punkt startowy (Spring Boot + JavaFX).
7. **`JavaFXApplication`** – Inicjuje uruchomienie interfejsu JavaFX, ładuje pierwszą scenę (ekran logowania).

---

## Funkcjonalności
1. **Rejestracja i logowanie**
    - Walidacja email i hasła (np. przy pomocy `ValidationUtils`).
    - Przechowywanie użytkowników w bazie `users.db`.

2. **Zarządzanie playlistami**
    - Tworzenie i usuwanie playlist (zapis w `playlists.db`).
    - Dedykowana baza SQLite (.db) dla każdej playlisty, przechowująca piosenki.

3. **Dodawanie / usuwanie piosenek**
    - Przechowuje piosenki (tytuł, artysta, ścieżka/URL) w bazie `.db` powiązanej z playlistą.

4. **Odtwarzanie piosenek**
    - `URLViewer` otwiera linki w przeglądarce Chrome.
    - Możliwość odtwarzania całej playlisty w osobnym wątku.

5. **Udostępnianie playlist**
    - Udostępnianie innym użytkownikom (SharedPlaylist).
    - Wysyłanie powiadomień (`Notification`) i akceptowanie/odrzucanie udostępnienia.

---

## Wymagania
- **Java 11+** (lub nowsza)
- **Maven** (do kompilacji)
- (opcjonalnie) **Chrome** – żeby `URLViewer` mógł otwierać linki

---

## Uruchomienie aplikacji

1. **Klonuj repozytorium**:
   ```bash
   git clone https://github.com/nazwa-uzytkownika/playlist-manager.git
   cd playlist-manager

2. **Zbuduj projekt**:
    ```bash
   mvn clean install

3. **Uruchom (np. z IntelliJ wybierając `PlaylistManagerApplication` → Run), albo:**
    ```bash
   java -jar target/playlistmanager-1.0-SNAPSHOT.jar
4. **Aplikacja JavaFX wystartuje i wyświetli *okno logowania*.**

---
## Autor

- Kinga Surma
- Martyna Peukert

