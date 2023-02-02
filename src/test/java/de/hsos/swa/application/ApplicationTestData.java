package de.hsos.swa.application;

import de.hsos.swa.domain.entity.*;
import de.hsos.swa.domain.factory.TopicFactory;
import de.hsos.swa.domain.factory.UserFactory;

import java.util.ArrayList;
import java.util.List;

public class ApplicationTestData {
    protected User user;
    protected List<User> users = new ArrayList<>();
    protected Topic topic;
    protected List<Post> topics = new ArrayList<>();

    protected Vote commentVote;
    protected Comment comment;
    protected Post post;

    protected Vote postVote;
    protected List<Post> posts = new ArrayList<>();



    public void initTestData(){
        this.user = UserFactory.createUser("testuser1");
        this.users.add(user);
        this.topic = TopicFactory.createTopic("Mein Thema", "Beschreibung meines Themas", user);

        // TODO: Weitere Testdaten initialisieren

    }
}
