package de.hsos.swa.actors.rest;

import de.hsos.swa.actors.rest.dto.out.UserDto;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
@TestHTTPEndpoint(UsersRessource.class)
public class UserRessourceTest {


    @Test
    public void createUser() {
        Map<String, String> regData = new HashMap<>();
        regData.put("username", "availableuser");
        regData.put("password", "availableuser");

        ValidatableResponse response = given()
                .header("Content-Type", "application/json") // Auth?
                .contentType("application/json")
                .body(regData)
                .post()
                .then()
                .statusCode(200);

        response.body("username", is("availableuser"));
    }

    @Test
    public void createUserInvalidName() {
        Map<String, String> regData = new HashMap<>();
        regData.put("username", "a");
        regData.put("password", "asddsa");

        ValidatableResponse response = given()
                .header("Content-Type", "application/json") // Auth?
                .contentType("application/json")
                .body(regData)
                .post()
                .then()
                .statusCode(400);
    }

    @Test
    public void createUserInvalidPassword() {
        Map<String, String> regData = new HashMap<>();
        regData.put("username", "testUser");
        regData.put("password", " ");

        ValidatableResponse response = given()
                .header("Content-Type", "application/json") // Auth?
                .contentType("application/json")
                .body(regData)
                .post()
                .then()
                .statusCode(400);
    }

    @Test
    public void createDuplicateUser() {

        Map<String, String> regData = new HashMap<>();
        regData.put("username", "oschluet");
        regData.put("password", "oschluet");

        ValidatableResponse response = given()
                .header("Content-Type", "application/json") // Auth?
                .contentType("application/json")
                .body(regData)
                .post()
                .then()
                .statusCode(400);
        //response.body("username", is("availableuser"));
    }

    @Test
    public void getAllUsersUnauth() {
        ValidatableResponse response = given()
                .header("Content-Type", "application/json") // Auth?
                .contentType("application/json")
                .get()
                .then()
                .statusCode(401);
    }

    @Test
    public void getAllUsersAuth() {
        ValidatableResponse response = given()
                .header("Content-Type", "application/json") // Auth?
                .contentType("application/json")
                .auth().basic("admin", "admin")
                .get()
                .then()
                .statusCode(200);

        int userCount = response.extract().as(UserDto[].class).length;
        Assertions.assertEquals(userCount, 10);
    }

    @Test
    public void deleteUser() {
        String userId = "78188b10-4733-4fca-8dfd-e07aee389383";

        ValidatableResponse response = given()
                .header("Content-Type", "application/json") // Auth?
                .contentType("application/json")
                .auth().basic("admin", "admin")
                .delete(userId)
                .then()
                .statusCode(200);

        response.body("id", is(userId));
        response.body("username", is("<DISABLED>"));

        //Wenn User erneut gelöscht wird NO_CONTENT
        given()
                .header("Content-Type", "application/json") // Auth?
                .contentType("application/json")
                .auth().basic("admin", "admin")
                .delete(userId)
                .then()
                .statusCode(204);

    }
}
