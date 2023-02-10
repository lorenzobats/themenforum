package de.hsos.swa.actors.rest;

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
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
@TestHTTPEndpoint(TopicsRessource.class)
public class TopicRessourceTest {

    @Test
    public void getAllTopics() {
        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .contentType("application/json")
                .get()
                .then()
                .statusCode(200);
    }

    @Test
    public void getTopicById() {
        String topicId = "a70d18cf-7b53-43fe-86de-1277fa476864";
        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .contentType("application/json")
                .auth().basic("admin", "admin")
                .get(topicId)
                .then()
                .statusCode(200);

        response.body("id", is(topicId));
        response.body("title", is("Autos"));
        response.body("description", is("Deutschland das Autoland"));
    }

    @Test
    public void createTopic(){
        Map<String, String> topicData = new HashMap<>();

        topicData.put("title", "TestTopic");
        topicData.put("description", "TestDescription");
        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .contentType("application/json")
                .auth().basic("lbattist", "lbattist")
                .body(topicData)
                .post()
                .then()
                .statusCode(200);
    }

    @Test
    public void createTopicInvalidTitle(){
        Map<String, String> topicData = new HashMap<>();

        topicData.put("title", "w");
        topicData.put("description", "TestDescription");
        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .contentType("application/json")
                .auth().basic("lbattist", "lbattist")
                .body(topicData)
                .post()
                .then()
                .statusCode(400);

        JsonPath jsonPath = response.extract().jsonPath();
        List<Map<String, String>> errors = jsonPath.getList("errors");

        Assertions.assertEquals(errors.get(0).get("message"), "Invalid request");
    }

    @Test
    public void createTopicBlank(){
        Map<String, String> topicData = new HashMap<>();

        topicData.put("title", " ");
        topicData.put("description", " ");
        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .contentType("application/json")
                .auth().basic("lbattist", "lbattist")
                .body(topicData)
                .post()
                .then()
                .statusCode(400);

        JsonPath jsonPath = response.extract().jsonPath();
        List<Map<String, String>> errors = jsonPath.getList("errors");

        Assertions.assertEquals(errors.get(0).get("message"), "Invalid request");
    }

    @Test
    public void createTopicDuplicateTitle(){
        Map<String, String> topicData = new HashMap<>();

        topicData.put("title", "Autos");
        topicData.put("description", "TestDescription");
        ValidatableResponse response = given()
                .header("Content-Type", "application/json")
                .contentType("application/json")
                .auth().basic("lbattist", "lbattist")
                .body(topicData)
                .post()
                .then()
                .statusCode(400);

        JsonPath jsonPath = response.extract().jsonPath();
        List<Map<String, String>> errors = jsonPath.getList("errors");

        Assertions.assertEquals(errors.get(0).get("message"), "Invalid request");
    }
}
