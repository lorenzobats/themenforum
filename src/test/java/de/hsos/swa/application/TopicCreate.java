package de.hsos.swa.application;

import de.hsos.swa.application.input.CreateTopicUseCase;
import de.hsos.swa.application.input.dto.in.CreateTopicCommand;
import de.hsos.swa.domain.entity.Topic;
import de.hsos.swa.domain.entity.User;
import de.hsos.swa.domain.factory.TopicFactory;
import de.hsos.swa.domain.factory.UserFactory;
import io.cucumber.java.en.Given;

import javax.inject.Inject;

public class TopicCreate extends ApplicationTestData {
    @Inject
    CreateTopicUseCase createTopicUseCase;

    protected User user;
    protected Topic topic;

    public void initTestData(){
        this.user = UserFactory.createUser("testuser1");
        this.topic = TopicFactory.createTopic("Mein Thema", "Beschreibung meines Themas", user);

        // TODO: Weitere Testdaten initialisieren
    }

    @Given("I provide all required data to create a new Topic")
            public void create_topic(){
            CreateTopicCommand testInput = new CreateTopicCommand(
                    "Mein Thema",
                    "Beschreibung meines Themas",
                    "testuser1");
            this.createTopicUseCase.createTopic(testInput);
            }
}
