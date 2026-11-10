package pas.v2026.pasrest.integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import pas.v2026.pasrest.dto.request.user.CreateClientDto;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

// Losowy port aby nie blokowac 8080
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserIntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void shouldReturnAllUsers() {
        given()
                .when()
                .get("/api/users")
                .then()
                .statusCode(200) // Sprawdzamy czy API zwraca 200
                .body("size()", greaterThan(0)) // Sprawdzamy czy lista nie jest pusta
                .body("[0].login", notNullValue()); // Sprawdzamy czy pierwszy user ma login
    }

    @Test
    public void shouldCreateNewClientAndReturn201() {
        CreateClientDto newClient = new CreateClientDto();
        newClient.setLogin("nowy_testowy_klient");
        newClient.setPassword("TrudneHaslo123");

        given()
                .contentType(ContentType.JSON)
                .body(newClient)
                .when()
                .post("/api/users")
                .then()
                .statusCode(201) // Oczekujemy kodu 201
                .body("login", equalTo("nowy_testowy_klient"))
                .body("active", equalTo(false));
    }

    @Test
    public void shouldReturn409WhenLoginIsAlreadyTaken() {
        CreateClientDto duplicatedClient = new CreateClientDto();
        duplicatedClient.setLogin("klient_aktywny");
        duplicatedClient.setPassword("InneHaslo123");

        given()
                .contentType(ContentType.JSON)
                .body(duplicatedClient)
                .when()
                .post("/api/users")
                .then()
                .statusCode(201);

        CreateClientDto newClient = new CreateClientDto();
        newClient.setLogin("klient_aktywny");
        newClient.setPassword("InneHaslo123");

        given()
                .contentType(ContentType.JSON)
                .body(newClient)
                .when()
                .post("/api/users")
                .then()
                .statusCode(409)
                .body(containsString("'klient_aktywny' już istnieje"));
    }
}