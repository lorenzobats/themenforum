package de.hsos.swa.entity;


import java.util.UUID;

public class User {
    private UUID id;
    private String name;

    public User(String name) {
        this.name = name;
    }

    public User(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return this.id;
    }

    public String getName() {
        return name;
    }
}
