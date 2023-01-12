package de.hsos.swa.entity;

import java.util.UUID;

public class Post {
    private UUID id;
    private String title;

    public Post(String title) {
        this.id = UUID.randomUUID();
        this.title = title;
    }
    public UUID getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
}
