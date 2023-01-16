package de.hsos.swa.domain.entity;

import java.util.UUID;

public class Comment {
    private UUID id;
    private User user;
    private String text;
    //private int upvotes;


    public Comment(UUID id, User user, String text) {
        this.id = id;
        this.user = user;
        this.text = text;
    }

    public Comment(User user, String text) {
        this.id = UUID.randomUUID();
        this.user = user;
        this.text = text;
    }
}
