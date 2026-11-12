package pas.v2026.pasrest.integration;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ResourceIntegrationTest {

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

    @Test
    public void shouldPerformCrudOperationsOnResource() {
        String createJson = """
                {
                    "type": "DATA_CENTER",
                    "name": "Nowe DC Kraków",
                    "description": "DC na południu",
                    "price": 2500.0,
                    "location": "Kraków",
                    "area": 120.5,
                    "powerCapacity": 500
                }
                """;

        String createdId = given()
                .spec(requestSpec)
                .body(createJson)
                .when()
                .post("/api/resources")
                .then()
                .log().all()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", equalTo("Nowe DC Kraków"))
                .extract().path("id");

        given()
                .spec(requestSpec)
                .when()
                .get("/api/resources/" + createdId)
                .then()
                .log().all()
                .statusCode(200)
                .body("location", equalTo("Kraków"));

        String updateJson = """
                {
                    "type": "DATA_CENTER",
                    "name": "Zaktualizowane DC Kraków",
                    "description": "Większe DC",
                    "price": 3000.0,
                    "location": "Kraków Północ",
                    "area": 200.0,
                    "powerCapacity": 800
                }
                """;

        given()
                .spec(requestSpec)
                .body(updateJson)
                .when()
                .put("/api/resources/" + createdId)
                .then()
                .log().all()
                .statusCode(200)
                .body("name", equalTo("Zaktualizowane DC Kraków"))
                .body("price", equalTo(3000.0f));

        given()
                .spec(requestSpec)
                .when()
                .delete("/api/resources/" + createdId)
                .then()
                .log().all()
                .statusCode(204);

        given()
                .spec(requestSpec)
                .when()
                .get("/api/resources/" + createdId)
                .then()
                .log().all()
                .statusCode(404);
    }

    @Test
    public void shouldReturn400WhenResourceDataIsInvalid() {
        String invalidResourceJson = """
                {
                    "type": "SERVER",
                    "name": "",
                    "description": "Serwer bez nazwy",
                    "price": -100.0,
                    "manufacturer": "Dell",
                    "nameOfCpu": "Intel",
                    "numberOfCpus": 4,
                    "ram": 16,
                    "storageCapacity": 500
                }
                """;

        given()
                .spec(requestSpec)
                .body(invalidResourceJson)
                .when()
                .post("/api/resources")
                .then()
                .log().all()
                .statusCode(400)
                .body("name", notNullValue())
                .body("price", notNullValue());
    }
}