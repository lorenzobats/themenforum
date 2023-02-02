package de.hsos.swa.application;

import de.hsos.swa.application.input.CreateTopicUseCase;
import de.hsos.swa.application.input.dto.in.CreateTopicCommand;
import de.hsos.swa.application.input.dto.out.ApplicationResult;

import de.hsos.swa.domain.entity.Topic;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;

import javax.inject.Inject;

public class TopicCreate extends ApplicationTestData {
    @Inject
    CreateTopicUseCase createTopicUseCase;


    ApplicationResult<Topic> createTopicResult;



    @Given("I provide all required data to create a new Topic")
    public void given() {
        CreateTopicCommand createTopicCommand = new CreateTopicCommand(
                "Mein Thema",
                "Meine Beschreibung",
                "oschluet");
        createTopicResult = this.createTopicUseCase.createTopic(createTopicCommand, "oschluet");
    }

    @Then("A new topic is created")
    public void then(){
        Assertions.assertNotNull(createTopicResult);
        Assertions.assertTrue(createTopicResult.ok());
        Assertions.assertEquals(ApplicationResult.Status.OK, createTopicResult.status());
        Assertions.assertEquals("", createTopicResult.message());
        Assertions.assertEquals("Mein Thema", createTopicResult.data().getTitle());
        Assertions.assertEquals("Meine Beschreibung", createTopicResult.data().getDescription());
        Assertions.assertEquals("oschluet", createTopicResult.data().getOwner().getName());
    }
}
