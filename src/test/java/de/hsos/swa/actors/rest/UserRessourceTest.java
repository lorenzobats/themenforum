package de.hsos.swa.actors.rest;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
@TestHTTPEndpoint(UsersRessource.class)
public class UserRessourceTest {


    @Test
    public void testCreateUser() {

        Map<String,String> regData = new HashMap<>();
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
    public void getAllUsers() {

        Map<String,String> regData = new HashMap<>();
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
    public void testCreateUserDuplicate() {

        Map<String,String> regData = new HashMap<>();
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

}
