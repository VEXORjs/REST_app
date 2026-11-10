
package pas.v2026.pasrest.integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AllocationIntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void shouldCreateAllocationAndReturn201() {

        String clientId = given()
                .when()
                .get("/api/users?exact=kient_aktywny")
                .then()
                .statusCode(200)
                .extract()
                .path("[0].id");

        String resourceId = given()
                .when()
                .get("/api/resources")
                .then()
                .statusCode(200)
                .extract()
                .path("find { it.name == 'Cluster-AI-Alpha' }.id");

        Instant now = Instant.now();
        String json = """
                {
                    "clientId": "%s",
                    "resourceId": "%s",
                    "startTime": "%s",
                    "endTime": "%s"
                }
                """.formatted(
                        clientId,
                        resourceId,
                        now.plus(1, ChronoUnit.HOURS),
                        now.plus(3, ChronoUnit.HOURS)
                );

        given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("/api/allocations")
                .then()
                .log().all()
                .statusCode(201)
                .body("id", notNullValue());
    }
}
