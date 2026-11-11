package pas.v2026.pasrest.integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import pas.v2026.pasrest.dto.request.user.CreateClientDto;
import pas.v2026.pasrest.dto.request.user.UpdateUserDto;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

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
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("[0].login", notNullValue());
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
                .statusCode(201)
                .body("login", equalTo("nowy_testowy_klient"))
                .body("active", equalTo(false));
    }

    @Test
    public void shouldUpdateUserPasswordAndActivateStatus() {
        CreateClientDto newClient = new CreateClientDto();
        newClient.setLogin("klient_do_edycji");
        newClient.setPassword("Haslo123");

        String userId = given()
                .contentType(ContentType.JSON)
                .body(newClient)
                .when()
                .post("/api/users")
                .then()
                .statusCode(201)
                .extract().path("id");

        UpdateUserDto updateDto = new UpdateUserDto();
        updateDto.setPassword("NoweTrudneHaslo123");

        given()
                .contentType(ContentType.JSON)
                .body(updateDto)
                .when()
                .put("/api/users/" + userId)
                .then()
                .statusCode(200);

        given()
                .when()
                .patch("/api/users/" + userId + "/status?active=true")
                .then()
                .statusCode(200)
                .body("active", equalTo(true));
    }

    @Test
    public void shouldReturn409WhenLoginIsAlreadyTaken() {
        String uniqueLogin = "unikalny_login_test_409";

        CreateClientDto firstClient = new CreateClientDto();
        firstClient.setLogin(uniqueLogin);
        firstClient.setPassword("InneHaslo123");


        given()
                .contentType(ContentType.JSON)
                .body(firstClient)
                .when()
                .post("/api/users")
                .then()
                .statusCode(201);

        CreateClientDto duplicatedClient = new CreateClientDto();
        duplicatedClient.setLogin(uniqueLogin);
        duplicatedClient.setPassword("InneHaslo123");


        given()
                .contentType(ContentType.JSON)
                .body(duplicatedClient)
                .when()
                .post("/api/users")
                .then()
                .statusCode(409)
                .body("error", containsString("już istnieje"));
    }

    @Test
    public void shouldReturn400WhenUserSyntaxIsInvalid() {
        CreateClientDto invalidClient = new CreateClientDto();
        invalidClient.setLogin("test_walidacji");
        invalidClient.setPassword("123");

        given()
                .contentType(ContentType.JSON)
                .body(invalidClient)
                .when()
                .post("/api/users")
                .then()
                .statusCode(400)
                .body("password", containsString("minimum 6 znaków"));
    }
}