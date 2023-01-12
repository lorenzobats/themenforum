package de.hsos.swa.entity;

import java.util.List;
import java.util.UUID;


public class Post {
    private UUID id;
    private String title;
    private User user;

    private Topic topic;
    private List<Comment> comments;
    private int upvotes;


    public Post(UUID id, String title) {
        this.id = id;
        this.title = title;
    }

    public Post(String title) {
        this.id = UUID.randomUUID();
        this.title = title;
    }

    public void addComment(Comment comment) {
        //some domainlogic...
        this.comments.add(comment);
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public User getUser() {
        return user;
    }
}
