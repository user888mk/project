# Kursy

### struktura
- 3-warstwowa
- Controller - przyjmowanie zapytań i wypluwanie odpowiedzi
- Service - logika biznesowa
- Repository - połączenie z bazą danych
- każde entity będzie miało swoje 3-warstwowe flow
- Nauczyciel (id, imie, nazwisko, lista jezykow)
- Kursant (id, imie, nazwisko, jezyk)
- Lekcja (id, Kursant, Nauczyciel, termin)

### funkcjonalności
- wylistowanie nauczycieli GIT
- wylistowanie kursantów GIT
- wylistowanie lekcji GIT
- dodawanie nauczyciela (dodając nauczyciela, chcemy mieć możliwość wybrania kilku języków jednocześnie) - guzik na liście nauczycieli GIT
- dodawanie kursanta (wybór nauczyciela z listy dostępnych - nie pozwalany na przypisanie nauczyciela, który nie uczy danego języka) - guzik na liście kursantów GIT
- dodawanie lekcji (nie pozwalamy na zaplanowanie lekcji w przeszłości && nie pozwalamy na zaplanowanie lekcji w terminie, który będzie się pokrywał z inna lekcją danego nauczyciela) - guzik na liście lekcji oraz guzik przy kursancie
- usuwanie nauczyciela (soft delete) - guzik na liście nauczycieli GIT
- usuwanie kursanta (soft delete) - guzik na liście kursantów  GIT
- usuwanie lekcji (nie usuwamy lekcji, która już się zaczęła) - guzik na liście lekcji GIT
- zmiana terminu lekcji (nie pozwalamy na przypisanie terminu, który jest niedostępny dla nauczyciela, ani nie pozwalamy na zaplanowanie jej w przeszłości) - guzik na liście lekcji
- zmiana nauczyciela dla kursanta (walidujemy/sprawdzamy język) - guzik na liście kursantów

## TODO
- testy jednostkowe dla warstwy serwisowej
- poczytać o 4 stopniach dojrzałości Richardsona (zwrócić uwagę na mapowania endpointów)
- @SQLDelete

## Mapowania restowe
- GET (brak mapowania)  - pobranie wszystkich zasobów
- GET /{id}  - pobranie zasobu o konkretnym id
- POST (brak mapowania)  - utworzenie nowego zasobu
- PUT /{id}  - update całościowy zasobu o konkretnym id
- PATCH /{id}  - update częściowy zasobu o konkretnym id
- DELETE /{id}  - usunięcie zasobu o konkretnym id
