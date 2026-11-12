package pas.v2026.pasrest.integration;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AllocationIntegrationTest {

    @LocalServerPort
    private int port;

    private RequestSpecification requestSpec;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
    }

    private String createActiveClientAndGetId(String login) {
        String id = given().spec(requestSpec)
                .body("""
                      { "role": "CLIENT", "login": "%s", "password": "Password123" }
                      """.formatted(login))
                .when().post("/api/users").then().log().all().statusCode(201).extract().path("id");

        given().spec(requestSpec).when().patch("/api/users/" + id + "/status?active=true").then().log().all().statusCode(200);
        return id;
    }

    private String createResourceAndGetId(String name) {
        return given().spec(requestSpec)
                .body("""
                      { "type": "CLUSTER", "name": "%s", "description": "Opis", "price": 100.0, "numberOfServers": 5, "architecture": "x64" }
                      """.formatted(name))
                .when().post("/api/resources").then().log().all().statusCode(201).extract().path("id");
    }

    @Test
    public void shouldCreateAllocationSuccessfully() {
        String clientId = createActiveClientAndGetId("alokacja_klient_1");
        String resourceId = createResourceAndGetId("Alokacja_Klaster_1");

        Instant now = Instant.now();
        String startTime = DateTimeFormatter.ISO_INSTANT.format(now.plus(1, ChronoUnit.HOURS));
        String endTime = DateTimeFormatter.ISO_INSTANT.format((now.plus(5, ChronoUnit.HOURS)));
        String json = """
                {
                    "clientId": "%s",
                    "resourceId": "%s",
                    "startTime": "%s",
                    "endTime": "%s"
                }
                """.formatted(clientId, resourceId, startTime, endTime);

        given()
                .spec(requestSpec)
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allocations")
                .then()
                .log().all()
                .statusCode(201)
                .body("id", notNullValue());
    }

    @Test
    public void shouldReturn409WhenResourceIsAlreadyAllocated() {
        String clientId = createActiveClientAndGetId("alokacja_klient_2");
        String resourceId = createResourceAndGetId("Alokacja_Klaster_2");

        Instant now = Instant.now();
        String startTime = DateTimeFormatter.ISO_INSTANT.format(now.plus(1, ChronoUnit.HOURS));
        String endTime = DateTimeFormatter.ISO_INSTANT.format((now.plus(5, ChronoUnit.HOURS)));
        String json = """
                {
                    "clientId": "%s",
                    "resourceId": "%s",
                    "startTime": "%s",
                    "endTime": "%s"
                }
                """.formatted(clientId, resourceId, startTime, endTime);

        given().spec(requestSpec).body(json).when().post("/api/allocations").then().log().all().statusCode(201);

        given()
                .spec(requestSpec)
                .body(json)
                .when()
                .post("/api/allocations")
                .then()
                .log().all()
                .statusCode(409)
                .body("error", containsString("jest już zaalokowany"));
    }

    @Test
    public void shouldReturn403WhenClientIsInactive() {

        String inactiveClientId = given().spec(requestSpec)
                .body("""
                      { "role": "CLIENT", "login": "inactive_klient", "password": "Password123" }
                      """)
                .when().post("/api/users").then().log().all().statusCode(201).extract().path("id");

        String resourceId = createResourceAndGetId("Alokacja_Klaster_3");

        Instant now = Instant.now();
        String startTime = DateTimeFormatter.ISO_INSTANT.format(now.plus(1, ChronoUnit.HOURS));
        String endTime = DateTimeFormatter.ISO_INSTANT.format((now.plus(5, ChronoUnit.HOURS)));
        String json = """
                {
                    "clientId": "%s",
                    "resourceId": "%s",
                    "startTime": "%s",
                    "endTime": "%s"
                }
                """.formatted(inactiveClientId, resourceId, startTime, endTime);

        given()
                .spec(requestSpec)
                .body(json)
                .when()
                .post("/api/allocations")
                .then()
                .log().all()
                .statusCode(403)
                .body("error", containsString("jest nieaktywny"));
    }
}