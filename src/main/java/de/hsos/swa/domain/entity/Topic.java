package de.hsos.swa.domain.entity;

import java.time.LocalDate;
import java.util.*;

public class Topic {
    private UUID id;
    private String title;
    private String description;
    //private LocalDate createdAt;  // TODO: Datum
    private User owner;

    public Topic(UUID id, String title, String description, User owner) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.owner = owner;
    }

    public Topic(String title, String description, User owner) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.description = description;
        this.owner = owner;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public User getOwner() {
        return owner;
    }
}
