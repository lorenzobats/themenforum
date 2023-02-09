package de.hsos.swa.actors.rest;

import de.hsos.swa.actors.rest.dto.out.PostDto;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
@TestHTTPEndpoint(PostsRessource.class)
public class PostRessourceTest {

    @Test
    public void getAllPosts() {
        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .contentType("application/json")
                .get()
                .then()
                .statusCode(200);

        Assertions.assertEquals(response.extract().jsonPath().getList("").size(),6);
    }

    @Test
    public void getAllPostsByUser() {
        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .contentType("application/json")
                .queryParam("username", "oschluet")
                .get()
                .then()
                .statusCode(200);

        Assertions.assertEquals(response.extract().jsonPath().getList("").size(),3);
    }

    @Test
    public void getPostById() {
        String postId = "55a7409f-c0eb-453e-b7ef-89f905963ce9";

        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
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
    public void createPostNotAllowed() {
        Map<String, String> postData = new HashMap<>();
        postData.put("title", "TestPostTitle");
        postData.put("content", "TestContent");
        postData.put("topicId", "a70d18cf-7b53-43fe-86de-1277fa476864");


        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .contentType("application/json")
                .body(postData)
                .post()
                .then()
                .statusCode(401);
    }

    @Test
    public void createPostMissingTitle() {
        Map<String, String> postData = new HashMap<>();

        postData.put("content", "TestContent");
        postData.put("topicId", "a70d18cf-7b53-43fe-86de-1277fa476864");
        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .contentType("application/json")
                .auth().basic("lbattist", "lbattist")
                .body(postData)
                .post()
                .then()
                .statusCode(400);

        JsonPath jsonPath = response.extract().jsonPath();
        List<Map<String, String>> errors = jsonPath.getList("errors");

        Assertions.assertEquals(errors.get(0).get("message"), "Invalid request");
        Assertions.assertEquals(errors.get(0).get("detail"), "title is blank");
    }

    @Test
    public void createPostMissingEverything() {
        Map<String, String> postData = new HashMap<>();

        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .contentType("application/json")
                .auth().basic("lbattist", "lbattist")
                .body(postData)
                .post()
                .then()
                .statusCode(400);

        JsonPath jsonPath = response.extract().jsonPath();
        List<Map<String, String>> errors = jsonPath.getList("errors");

        Assertions.assertEquals(errors.size(), 3);
    }


    @Test
    public void createPostShortTitle() {
        Map<String, String> postData = new HashMap<>();
        postData.put("title", "T");
        postData.put("content", "TestContent");
        postData.put("topicId", "a70d18cf-7b53-43fe-86de-1277fa476864");


        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .contentType("application/json")
                .auth().basic("lbattist", "lbattist")
                .body(postData)
                .post()
                .then()
                .statusCode(400);

        JsonPath jsonPath = response.extract().jsonPath();
        List<Map<String, String>> errors = jsonPath.getList("errors");

        Assertions.assertEquals(errors.get(0).get("message"), "Invalid request");
        Assertions.assertEquals(errors.get(0).get("detail"), "Post Title must be between 2 and 40 characters");
    }

    @Test
    public void createPostShortContent() {
        Map<String, String> postData = new HashMap<>();
        postData.put("title", "TestPostTitle");
        postData.put("content", "T");
        postData.put("topicId", "a70d18cf-7b53-43fe-86de-1277fa476864");


        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .contentType("application/json")
                .auth().basic("lbattist", "lbattist")
                .body(postData)
                .post()
                .then()
                .statusCode(400);

        JsonPath jsonPath = response.extract().jsonPath();
        List<Map<String, String>> errors = jsonPath.getList("errors");

        Assertions.assertEquals(errors.get(0).get("message"), "Invalid request");
        Assertions.assertEquals(errors.get(0).get("detail"), "Post content must be between 2 and 250 characters");
    }

    @Test
    public void createPostMalformedTopicId() {
        Map<String, String> postData = new HashMap<>();
        postData.put("title", "TestPostTitle");
        postData.put("content", "TestContent");
        postData.put("topicId", "X70d18cf-7b53-43fe-86de-1277fa47686X");


        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .contentType("application/json")
                .auth().basic("lbattist", "lbattist")
                .body(postData)
                .post()
                .then()
                .statusCode(400);

        JsonPath jsonPath = response.extract().jsonPath();
        List<Map<String, String>> errors = jsonPath.getList("errors");

        Assertions.assertEquals(errors.get(0).get("message"), "Invalid request");
        Assertions.assertEquals(errors.get(0).get("detail"), "Invalid Id, ensure that the id matches the regular expression of a UUID-String [0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12");
    }

    @Test
    public void createPostForUnavailableTopicId() {
        Map<String, String> postData = new HashMap<>();
        postData.put("title", "TestPostTitle");
        postData.put("content", "TestContent");
        postData.put("topicId", "b70d18cf-7b53-43fe-86de-1277fa476864");


        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .contentType("application/json")
                .auth().basic("lbattist", "lbattist")
                .body(postData)
                .post()
                .then()
                .statusCode(400);

        JsonPath jsonPath = response.extract().jsonPath();
        List<Map<String, String>> errors = jsonPath.getList("errors");

        Assertions.assertEquals(errors.get(0).get("message"), "Invalid request");
        Assertions.assertEquals(errors.get(0).get("detail"), "Cannot find topic b70d18cf-7b53-43fe-86de-1277fa476864");
    }

    @Test
    public void createPost() {
        Map<String, String> postData = new HashMap<>();
        postData.put("title", "TestPostTitle");
        postData.put("content", "TestContent");
        postData.put("topicId", "a70d18cf-7b53-43fe-86de-1277fa476864");


        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .contentType("application/json")
                .auth().basic("lbattist", "lbattist")
                .body(postData)
                .post()
                .then()
                .statusCode(200);

        response.body("content", is(postData.get("content")));
        response.body("creator", is("lbattist"));
        response.body("downvoteCount", is(0));
        response.body("title", is(postData.get("title")));
        response.body("upvoteCount", is(0));

        response.body("createdAt", notNullValue());
        response.body("id", notNullValue());

        response.body("topic.createdAt", is("2023-01-21T21:49:37.855505"));
        response.body("topic.description", is("Deutschland das Autoland"));
        response.body("topic.id", is("a70d18cf-7b53-43fe-86de-1277fa476864"));
        response.body("topic.owner", is("oschluet"));
        response.body("topic.title", is("Autos"));
    }

    @Test
    public void deletePostForbidden() {
        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .contentType("application/json")
                .auth().basic("lbattist", "lbattist")
                .delete("/4cfe8a99-f71a-476c-a2cb-dae7cb86872e")
                .then()
                .statusCode(403);

        JsonPath jsonPath = response.extract().jsonPath();
        List<Map<String, String>> errors = jsonPath.getList("errors");

        Assertions.assertEquals(errors.get(0).get("message"), "Access to ressource forbidden");
        Assertions.assertEquals(errors.get(0).get("detail"), "Access cannot be granted");
    }

    @Test
    public void deletePostAllowedNoContent() {
        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .contentType("application/json")
                .auth().basic("oschluet", "oschluet")
                .delete("/4cfe8a99-f71a-476c-a2cb-dae7cb86872e")
                .then()
                .statusCode(200);

        response.body("title", is("Olivers Post 2"));


        given()
                .header("Content-Type", "application/json")
                .contentType("application/json")
                .auth().basic("lbattist", "lbattist")
                .delete("/4cfe8a99-f71a-476c-a2cb-dae7cb86872e")
                .then()
                .statusCode(204);

        given()
                .header("Content-Type", "application/json")
                .contentType("application/json")
                .auth().basic("oschluet", "oschluet")
                .delete("/4cfe8a99-f71a-476c-a2cb-dae7cb86872e")
                .then()
                .statusCode(204);
    }
}
