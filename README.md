Moja implementacja chmury na zdjęcia przy wykorzystaniu API dla serwisu https://cloudinary.com


aplikacja dostępna publicznie na platformie Heroku
https://photouploader-cloudinary.herokuapp.com/
(aplikacja po 30 minutach nieaktywności wyłącza się więc potrzeba więcej czasu na uruchomienie)

login: admin
hasło: admin

(Można utworzyć własnego użytkownika lecz admin widzi wszystkie zdjęcia w chmurze)


Funkcjonalności:
- rejestracja i logowanie użytkownika do bazy danych
- wysyłanie zdjęć do bazy danych
- wyświetlanie zdjęć z bazy danych dla konkretnego użytkownika
- aplikacja skaluje się na urządzenia mobilne oraz PC

Wykorzystane technologie:
- Java
- Spring
- Vaadin (https://vaadin.com)
- Cloudinary API (https://cloudinary.com/documentation/image_upload_api_reference)


Do zrobienia/błędy:
- na urządzeniach mobilnych żle wyświetla się podgląd zdjęcia
- główna strona logowania czasem źle się sformatuje
- pliki przesłane zapisujące się tymczasowo w projekcie aplikacji nie usuwają się
- brak automatycznego logowania po rejestracji użytkownika


