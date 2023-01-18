package de.hsos.swa.domain.entity;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.*;


public class Post {
    private UUID id;

    @NotBlank
    private String title;

    @Valid
    private User user;

    @Valid
    private Topic topic;

    @PastOrPresent
    private LocalDate publishedDate;

    private List<Comment> comments = new ArrayList<>();

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
        //comment id nur unique in scope eines Posts?
        this.comments.add(comment);
    }

    public void addReplyToComment(String parentCommentId, Comment reply) {
//        Optional<Comment> parentComment = this.comments.stream().filter(comment -> comment.getId().toString().equals(parentCommentId)).findFirst();
//        parentComment.ifPresent(comment -> comment.addReply(reply));

        Optional<Comment> parentComment = findCommentById(parentCommentId);
        parentComment.ifPresent(comment -> comment.addReply(reply));

    }

    private Optional<Comment> findCommentById(String parentCommentId) {
        Deque<Comment> stack = new ArrayDeque<Comment>(this.comments);

        while (!stack.isEmpty()) {
            Comment comment = stack.pop();
            if (comment.getId().toString().equals(parentCommentId)) {
                return Optional.of(comment);
            }
            stack.addAll(comment.getReplies());
        }

        return Optional.empty();
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

    public List<Comment> getComments() {
        return comments;
    }

}
