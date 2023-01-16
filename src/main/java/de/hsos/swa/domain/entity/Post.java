package de.hsos.swa.domain.entity;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


public class Post {
    @NotBlank
    private UUID id;

    @NotBlank
    private String title;

    @Valid
    private User user;

    @Valid
    private Topic topic;

    @PastOrPresent
    private LocalDate publishedDate;

    private List<Comment> comments;

    private int upvotes;


    public Post(UUID id, String title, User user) {
        this.id = id;
        this.title = title;
        this.user = user;
    }

    public Post(String title, User user) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.user = user;
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
