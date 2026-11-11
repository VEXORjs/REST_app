# PAS REST API - System Zarządzania i Alokacji Zasobów Data Center

Aplikacja typu REST. System służy do zarządzania użytkownikami, zasobami fizycznymi (serwerami, centrami danych, klastrami) oraz procesem ich alokacji. 

Kluczowym założeniem architektonicznym projektu jest **bezpośrednie wykorzystanie natywnego sterownika MongoDB** (z pominięciem narzędzi typu JPA / Spring Data MongoDB) oraz implementacja bazy danych jako środowiska **nietrwałego** (przywracanego do stanu początkowego przy każdym uruchomieniu).

## Tech Stack

* **Język:** Java 26
* **Framework:** Spring Boot 4.0.0
* **Baza danych:** MongoDB
* **Sterownik bazy:** MongoDB Java Driver Sync
* **Narzędzia:** Maven, Docker

## Architektura

1. **Bezpośrednie użycie API bazy danych:** Warstwa dostępu do danych (Repositories) jest oparta wyłącznie na obiektach `Document` oraz zapytaniach BSON za pośrednictwem `MongoClient`. ORM został wykluczony.
2. **Nietrwałość:** Przy każdym uruchomieniu aplikacji baza jest czyszczona (`database.drop()`), a schemat kolekcji, indeksy i dane początkowe są ładowane od nowa.
3. **Gwarancja unikalności:** Nałożono natywne ograniczenia bazy danych (indeksy `unique`) na krytyczne pola, takie jak loginy użytkowników.
4. **Gwarancja niepustości:** Ograniczenia `NOT NULL` są realizowane bezpośrednio po stronie bazy przy użyciu reguł walidacji **JSON Schema**, które odrzucają na poziomie serwera bazy dokumenty pozbawione kluczowych atrybutów.
5. **Niestandardowe klucze główne:** System korzysta z typu `UUID`, który jest poprawnie rzutowany na natywne pole `_id` w dokumentach MongoDB.
6. **Dziedziczenie i Polimorfizm:** Architektura wspiera różne typy użytkowników (`Client`, `UserAdmin`, `ResourceManager`) oraz typy zasobów (`Server`, `DataCenter`, `Cluster`).

## Wymagania wstępne

Aby uruchomić aplikację, potrzebujesz:
* Zainstalowanego środowiska **Java 26** (JDK)
* Narzędzia **Maven**
* Działającego środowiska **Docker** (środowisko uruchomieniowe bazy danych)

## Uruchomienie lokalne

1. **Uruchom serwer MongoDB w kontenerze Docker:**
   ```bash
   docker run -d -p 27017:27017 --name pas-mongodb mongo:latest
   ```
