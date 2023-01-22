package de.hsos.swa.domain.entity;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;
import java.util.*;

// TODO: Validierung spezifischer (zB. Titellänge, Descriptionlänge, etc.)

public class Topic {
    @Valid
    private UUID id;
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @PastOrPresent
    private LocalDateTime createdAt;
    @Valid
    private User owner;

    public Topic(UUID id, String title, String description, LocalDateTime createdAt, User owner) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.owner = owner;
    }

    public Topic(String title, String description, LocalDateTime createdAt, User owner) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public User getOwner() {
        return owner;
    }
}
