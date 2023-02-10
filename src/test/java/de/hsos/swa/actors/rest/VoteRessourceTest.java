package de.hsos.swa.actors.rest;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static io.restassured.RestAssured.given;

@QuarkusTest
@TestHTTPEndpoint(VotesRessource.class)
public class VoteRessourceTest {

    @Test
    public void getAllVotesUnauth() {
        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .contentType("application/json")
                .auth().basic("lbattist", "lbattist")
                .get()
                .then()
                .statusCode(401);
    }

    @Test
    public void getAllVotes() {
        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .contentType("application/json")
                .auth().basic("admin", "admin")
                .get()
                .then()
                .statusCode(200);
    }

    @Test
    public void votePost() {
        String postId = "55a7409f-c0eb-453e-b7ef-89f905963ce9";

        Map<String, String> voteData = new HashMap<>();
        voteData.put("voteType", "UP");
        voteData.put("entityId", postId);
        voteData.put("entityType", "POST");

        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .contentType("application/json")
                .auth().basic("lbattist", "lbattist")
                .body(voteData)
                .post()
                .then()
                .statusCode(200);

        response.body("user", is("lbattist"));
        response.body("voteType", is(voteData.get("voteType")));
    }

    @Test
    public void votePostInvalidVoteType() {
        String postId = "55a7409f-c0eb-453e-b7ef-89f905963ce9";

        Map<String, String> voteData = new HashMap<>();
        voteData.put("voteType", "XX");
        voteData.put("entityId", postId);
        voteData.put("entityType", "POST");

        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .contentType("application/json")
                .auth().basic("lbattist", "lbattist")
                .body(voteData)
                .post()
                .then()
                .statusCode(400);
    }

    @Test
    public void votePostInvalidEntityType() {
        String postId = "55a7409f-c0eb-453e-b7ef-89f905963ce9";

        Map<String, String> voteData = new HashMap<>();
        voteData.put("voteType", "DOWN");
        voteData.put("entityId", postId);
        voteData.put("entityType", "XXXX");

        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .contentType("application/json")
                .auth().basic("lbattist", "lbattist")
                .body(voteData)
                .post()
                .then()
                .statusCode(400);
    }

    @Test
    public void votePostBlankEntityType() {
        String postId = "55a7409f-c0eb-453e-b7ef-89f905963ce9";

        Map<String, String> voteData = new HashMap<>();
        voteData.put("voteType", "DOWN");
        voteData.put("entityId", postId);
        voteData.put("entityType", " ");

        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .contentType("application/json")
                .auth().basic("lbattist", "lbattist")
                .body(voteData)
                .post()
                .then()
                .statusCode(400);
    }

    @Test
    public void votePostBlankVoteType() {
        String postId = "55a7409f-c0eb-453e-b7ef-89f905963ce9";

        Map<String, String> voteData = new HashMap<>();
        voteData.put("voteType", " ");
        voteData.put("entityId", postId);
        voteData.put("entityType", "POST");

        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .contentType("application/json")
                .auth().basic("lbattist", "lbattist")
                .body(voteData)
                .post()
                .then()
                .statusCode(400);
    }
}
