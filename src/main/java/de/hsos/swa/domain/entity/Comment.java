package de.hsos.swa.domain.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Comment {
    private UUID id;
    private User user;
    private String text;
    private LocalDate publishedDate;

    private int upvotes;

    private List<Comment> replies = new ArrayList<>();

    private Comment parentComment;

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

    public UUID getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getText() {
        return text;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public List<Comment> getReplies() {
        return replies;
    }

    public void addReply(Comment reply) {
        reply.parentComment = this;
        this.replies.add(reply);
    }
    public Comment getParentComment() {
        return this.parentComment;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", user=" + user +
                ", text='" + text + '\'' +
                ", publishedDate=" + publishedDate +
                ", upvotes=" + upvotes +
                ", replies=" + replies +
                ", parentComment=" + parentComment +
                '}';
    }


}
