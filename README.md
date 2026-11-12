# PAS REST API – System Alokacji Zasobów (MongoDB API)

Aplikacja REST API realizująca system zarządzania i wyłącznej alokacji zasobów w określonych przedziałach czasowych (rezerwacja zasobów infrastruktury IT). Projekt został zrealizowany w architekturze kontener-komponent w oparciu o framework **Spring Boot** z wykorzystaniem **czystego sterownika MongoDB Java API Sync** (`mongodb-driver-sync` / `BSON`) bez użycia warstw abstrakcji typu JPA / ORM czy Spring Data MongoDB.

---

## Model Biznesowy i Dziedzina Polimorficzna

Projekt odwzorowuje trzy kluczowe obszary dziedzinowe z unikalnymi identyfikatorami biznesowymi oraz systemowymi kluczami głównymi, które w bazie są natywnymi **ObjectId**, a w aplikacji są obsługiwane jako typ `String`:

### 1. Hierarchia Użytkowników (`User`)
Obsługuje 3 poziomy uprawnień biznesowych oraz flagę aktywności konta (`active`):
- **`UserAdmin`** – poziom administratora użytkowników systemowych.
- **`ResourceManager`** – poziom zarządcy zasobów infrastruktury.
- **`Client`** – poziom użytkownika końcowego uprawnionego do alokacji zasobów.
- **Wymagane atrybuty:** `_id` (String - natywne ObjectId), `login` (wymagany, unikalny w całej hierarchii), `password`, `active` (boolean), `type` (`CLIENT` | `ADMIN` | `MANAGER`).

### 2. Hierarchia Zasobów (`Resource`)
Polimorficzny model zasobów infrastruktury IT:
- **`Server`** – parametry: `manufacturer`, `cpuName`, `numberOfCpus`, `ram`, `storageCapacity`.
- **`DataCenter`** – parametry: `location`, `area`, `powerCapacity`.
- **`Cluster`** – parametry: `numberOfServers`, `architecture`.
- **Wymagane atrybuty:** `_id` (String - natywne ObjectId), `type` (`SERVER` | `DATA_CENTER` | `CLUSTER`), `price` (precyzyjny typ `Decimal128`), `available` (boolean).

### 3. Klasa Alokacji Zasobów (`ResourceAllocation`)
Reprezentuje wyłączny dwustronny związek czasowy pomiędzy Klientem a Zasobem:
- **Wymagane atrybuty:** `_id` (String - natywne ObjectId), `clientId` (String), `resourceId` (String), `startTime` (Date/Instant), opcjonalne `endTime` (Date/Instant).

---

## Wymagania Techniczne

| Obszar | Wykorzystana technologia / Strategia |
| :--- | :--- |
| **Framework** | Spring Boot 4.0 (Spring Web MVC, Spring Context) |
| **Baza danych** | MongoDB (uruchamiana w kontenerze Docker) |
| **Baza danych API** | Powiązanie bezpośrednie z API `MongoDatabase` i `MongoCollection<Document>` |
| **Nietrwałość bazy (Volatile DB)** | Reset bazy (`database.drop()`) przy każdym starcie aplikacji i odtwarzanie schematów, walidatorów i mocków |
| **Ograniczenie Not-Null** | Wymuszane natywnie w MongoDB za pomocą reguł walidacji **JSON Schema** (`$jsonSchema`) |
| **Ograniczenie Unikalności** | Natywny indeks unikalny MongoDB na polu `login` w kolekcji `users` |
| **Testy Integracyjne** | **JUnit 5** + **REST Assured** (weryfikacja testów pozytywnych i negatywnych na wystawionej usłudze HTTP) |

---

## Struktura Warstwowa Aplikacji

Aplikacja przestrzega zasady pojedynczej odpowiedzialności (SRP) i rozdzielenia warstw:

```text
src/main/java/pas/v2026/pasrest/
├── config/
│   ├── MongoConfig.java                # Definicja Beanów MongoClient i MongoDatabase
│   └── MongoDatabaseInitializer.java   # Database drop, inicjalizacja JSON Schema, indeksów i danych testowych
├── controllers/                        # REST Controllers (REST API, ResponseEntity, DTOs, Validation)
│   ├── UserController.java
│   ├── ResourceController.java
│   └── AllocationController.java
├── exceptions/                         # Dedykowane wyjątki biznesowe i bazy danych
│   └── GlobalExceptionHandler.java     # Mapowanie na statusy 400, 404, 409, 422
├── model/                              # Model dziedzinowy (User, Resource, ResourceAllocation)
├── repositories/                       # Bezpośredni dostęp do MongoDB via Document API
│   ├── MongoUserRepository.java
│   ├── MongoResourceRepository.java
│   └── MongoResourceAllocationRepository.java
└── services/                           # Logika biznesowa, walidacje stanu, współbieżność
    ├── UserService.java
    ├── ResourceService.java
    └── ResourceAllocationService.java
```

---

## Ograniczenia i Walidacje Bazy Danych (JSON Schema)

Dla kolekcji podczas uruchamiania aplikacji nakładane są natywne walidatory bazy danych `$jsonSchema`:

```java
// Kolekcja Użytkowników
Document usersSchema = Document.parse("""
    {
      "$jsonSchema": {
        "bsonType": "object",
        "required": ["_id", "login", "password", "type", "active"]
      }
    }
""");

// Kolekcja Zasobów
Document resourcesSchema = Document.parse("""
    {
      "$jsonSchema": {
        "bsonType": "object",
        "required": ["_id", "type", "name", "price", "available"]
      }
    }
""");

// Kolekcja Alokacji
Document allocationsSchema = Document.parse("""
    {
      "$jsonSchema": {
        "bsonType": "object",
        "required": ["_id", "clientId", "resourceId", "startTime"]
      }
    }
""");

// Indeks unikalny dla identyfikatora biznesowego (login)
database.getCollection("users").createIndex(
    Indexes.ascending("login"),
    new IndexOptions().unique(true)
);
```

---

## Obsługa Wielowątkowości i Bezpieczeństwa Współbieżnego

1. **Współdzielenie DAO / Repozytoriów:** Singletony Springowe (`@Repository`) bezpiecznie operują na trwającym połączeniu `MongoCollection<Document>` sterownika MongoDB Sync Driver, który wewnętrznie zarządza pulą połączeń (Connection Pool).
2. **Bezpieczeństwo operacji zasilających i weryfikacyjnych:** Metody repozytorium wykonujące odczyt i zapytanie sprawdzające (np. unikalność loginu przed edycją lub brak aktywnych alokacji zasobu przed usunięciem) stosują sekcje synchronizowane oraz atomowe zapytania MongoDB (`Filters.and(...)`), aby zapobiec hazardowi przy przeplotach żądań HTTP.

---

## REST API – Endpointy i Kody Statusów HTTP

### 1. Użytkownicy (`/api/users`)
- `GET /api/users` – Pobranie listy wszystkich użytkowników (`200 OK`)
- `GET /api/users/{id}` – Pobranie użytkownika po ID (`200 OK` / `404 Not Found`)
- `GET /api/users/login/exact?login={login}` – Ścisłe wyszukiwanie po loginie (`200 OK` / `404 Not Found`)
- `GET /api/users/login/match?part={text}` – Wyszukiwanie częściowe po loginie (`200 OK`)
- `POST /api/users` – Rejestracja nowego użytkownika (`201 Created` / `400 Bad Request` / `409 Conflict`)
- `PUT /api/users/{id}` – Modyfikacja danych użytkownika (`200 OK` / `409 Conflict`)
- `POST /api/users/{id}/activate` – Aktywacja konta (`200 OK`)
- `POST /api/users/{id}/deactivate` – Deaktywacja konta (`200 OK`)

### 2. Zasoby (`/api/resources`)
- `GET /api/resources` – Lista wszystkich zasobów (`200 OK`)
- `GET /api/resources/{id}` – Pobranie zasobu po ID (`200 OK` / `404 Not Found`)
- `POST /api/resources` – Utworzenie nowego zasobu (`201 Created` / `400 Bad Request`)
- `PUT /api/resources/{id}` – Modyfikacja zasobu (`200 OK`)
- `DELETE /api/resources/{id}` – Usunięcie zasobu (dozwolone tylko, gdy zasób nie posiada żadnych alokacji, ani historycznych, ani bieżących: `204 No Content` / `409 Conflict` / `404 Not Found`)

### 3. Alokacje (`/api/allocations`)
- `GET /api/allocations` – Pobranie wszystkich alokacji (`200 OK`)
- `GET /api/allocations/client/{clientId}/current` – Bieżące alokacje wskazanego klienta (`200 OK`)
- `GET /api/allocations/client/{clientId}/past` – Zakończone alokacje klienta (`200 OK`)
- `GET /api/allocations/resource/{resourceId}/current` – Bieżące alokacje danego zasobu (`200 OK`)
- `GET /api/allocations/resource/{resourceId}/past` – Minione alokacje zasobu (`200 OK`)
- `POST /api/allocations` – Tworzenie nowej alokacji (`201 Created` / `400 Bad Request` / `409 Conflict` w przypadku braku aktywności klienta lub kolizji terminów)
- `POST /api/allocations/{id}/end` – Ustawienie czasu zakończenia alokacji (`200 OK` / `400 Bad Request`)
- `DELETE /api/allocations/{id}` – Usunięcie niezakończonej alokacji (`204 No Content` / `400 Bad Request`)

---

## Testy Integracyjne (JUnit 5 + REST Assured)

Projekt zawiera pakiet automatycznych testów integracyjnych weryfikujących działanie wystawionego REST API:

```text
src/test/java/pas/v2026/pasrest/
└── integration/
    ├── UserIntegrationTest.java
    ├── ResourceIntegrationTest.java
    └── AllocationIntegrationTest.java
```

### Pokryte Scenariusze Testowe:
- **Testy Pozytywne:**
  - Pełny cykl CRU na użytkownikach oraz CRUD na zasobach.
  - Pomyślne utworzenie alokacji zasobu dla aktywnego Klienta i zweryfikowanie jej na liście bieżących alokacji.
- **Testy Negatywne:**
  - Odrzucenie żądania z błędami składniowymi (`400 Bad Request` – np. brak wymaganych pól DTO / naruszenie Not-Null).
  - Próba utworzenia użytkownika z istniejącym już w bazie loginem (`409 Conflict`).
  - Próba nakładającej się alokacji zasobu, który jest już zaalokowany przez innego klienta (`409 Conflict`).
  - Próba usunięcia zasobu powiązanego z historią alokacji (`409 Conflict`).

---

## Uruchomienie Projektu Lokalnie

### 1. Wymagania wstępne
- **JDK 21+** (rekomendowane JDK 25 / 26)
- **Docker Desktop**
- **Apache Maven**

### 2. Uruchomienie bazy MongoDB w kontenerze
```bash
docker run -d -p 27017:27017 --name pas-mongodb mongo:latest
```

### 3. Budowa i uruchomienie aplikacji Spring Boot
```bash
mvn clean install
mvn spring-boot:run
```
Aplikacja wystawi interfejs REST API pod adresem: `http://localhost:8080`.

### 4. Uruchomienie pakietu testów integracyjnych
```bash
mvn test
```