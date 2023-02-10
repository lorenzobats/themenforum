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
@TestHTTPEndpoint(CommentsRessource.class)
public class CommentRessourceTest {

    @Test
    public void getAllComments() {
        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .contentType("application/json")
                .get()
                .then()
                .statusCode(200);
    }

    @Test
    public void commentPostNotRegistered() {
        String postId = "55a7409f-c0eb-453e-b7ef-89f905963ce9";

        Map<String, String> commentData = new HashMap<>();
        commentData.put("postId", postId);
        commentData.put("text", "TestText");

        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .contentType("application/json")
                .body(commentData)
                .post()
                .then()
                .statusCode(401);
    }

    @Test
    public void commentPost() {
        String postId = "55a7409f-c0eb-453e-b7ef-89f905963ce9";

        Map<String, String> commentData = new HashMap<>();
        commentData.put("postId", postId);
        commentData.put("text", "TestText");

        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .contentType("application/json")
                .auth().basic("lbattist", "lbattist")
                .body(commentData)
                .post()
                .then()
                .statusCode(200);

        response.body("text", is(commentData.get("text")));
    }

    @Test
    public void replyToComment() {
        String commentId = "9ac4c6fd-68e1-4b41-97fd-817284290411";

        Map<String, String> replyData = new HashMap<>();
        replyData.put("commentId", commentId);
        replyData.put("text", "ReplyText");

        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .contentType("application/json")
                .auth().basic("lbattist", "lbattist")
                .body(replyData)
                .post(commentId)
                .then()
                .statusCode(200);
    }

    @Test
    public void deleteComment() {
        String commentId = "9ac4c6fd-68e1-4b41-97fd-817284290411";
        ValidatableResponse response = given()
                .header("Content-Type", "application/json") // Auth?
                .contentType("application/json")
                .auth().basic("admin", "admin")
                .delete(commentId)
                .then()
                .statusCode(200);

        response.body("id", is(commentId));
        response.body("text", is("<DELETED>"));

        //Wenn Comment erneut gelöscht wird NO_CONTENT
        given()
                .header("Content-Type", "application/json") // Auth?
                .contentType("application/json")
                .auth().basic("admin", "admin")
                .delete(commentId)
                .then()
                .statusCode(204);
    }
}
