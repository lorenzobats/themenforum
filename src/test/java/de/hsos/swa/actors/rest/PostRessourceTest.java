package de.hsos.swa.actors.rest;

import de.hsos.swa.actors.rest.dto.out.PostDto;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.*;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
@TestHTTPEndpoint(PostsRessource.class)
public class PostRessourceTest {

    @Test
    public void getAllPosts() {
        ValidatableResponse response = given()
                .header("Content-Type", "application/json") // Auth?
                .contentType("application/json")
                .get()
                .then()
                .statusCode(200);
    }

    @Test
    public void getPostById() {
        String postId = "55a7409f-c0eb-453e-b7ef-89f905963ce9";

        ValidatableResponse response = given()
                .header("Content-Type", "application/json") // Auth?
                .contentType("application/json")
                .auth().basic("admin", "admin")
                .get(postId)
                .then()
                .statusCode(200);

        response.body("id", is(postId));
        response.body("title", is("Olivers Post 1"));
        response.body("content", is("Das hier ist Olivers erster Post"));
        response.body("creator", is("oschluet"));
    }

    @Test
    public void createPost() {
        Map<String, String> postData = new HashMap<>();
        postData.put("title", "TestPostTitle");
        postData.put("content", "TestContent");
        postData.put("topicId", "a70d18cf-7b53-43fe-86de-1277fa476864");


        ValidatableResponse response = given()
                .header("Content-Type", "application/json") // Auth?
                .contentType("application/json")
                .auth().basic("lbattist", "lbattist")
                .body(postData)
                .post()
                .then()
                .statusCode(200);

        response.body("creator", is("lbattist"));
        response.body("title", is(postData.get("title")));
        response.body("content", is(postData.get("content")));

        Assertions.assertEquals(response.extract().as(PostDto.class).topic.id, postData.get("topic"));
    }
}
