package de.hsos.swa.domain.entity;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;
import java.util.*;

public class Post {
    @Valid
    private UUID id;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @PastOrPresent
    private LocalDateTime createdAt;

    @Valid
    private Topic topic;

    @Valid
    private User creator;

    @NotNull
    private List<Comment> comments = new ArrayList<>();

    @NotNull
    private List<Vote> votes = new ArrayList<>();


    public Post(UUID id, String title, String content, LocalDateTime createdAt, Topic topic, User creator) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.topic = topic;
        this.creator = creator;
    }

    public Post(UUID id, String title, String content, LocalDateTime createdAt, Topic topic, User creator, List<Comment> comments) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.topic = topic;
        this.creator = creator;
        this.comments = comments;
    }

    public Post(String title, String content, LocalDateTime createdAt, Topic topic, User creator) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.topic = topic;
        this.creator = creator;
    }


    // GETTER
    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Topic getTopic() {
        return topic;
    }

    public User getCreator() {
        return creator;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    // SETTER
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }



    // COMMENTS
    public Optional<Comment> findCommentById(String commentId) {
        Deque<Comment> stack = new ArrayDeque<>(this.comments);

        while (!stack.isEmpty()) {
            Comment comment = stack.pop();
            if (comment.getId().toString().equals(commentId)) {
                return Optional.of(comment);
            }
            stack.addAll(comment.getReplies());
        }

        return Optional.empty();
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public boolean addReplyToComment(String parentCommentId, Comment reply) {
        Optional<Comment> parentComment = findCommentById(parentCommentId);
        if (parentComment.isPresent() && parentComment.get().isActive()) {
            parentComment.get().addReply(reply);
            return true;
        }
        return false;
    }

    public int getCommentCount() {
        Deque<Comment> stack = new ArrayDeque<>(this.comments);
        int count = 0;

        while(!stack.isEmpty()) {
            Comment comment = stack.pop();
            count++;
            stack.addAll(comment.getReplies());
        }
        return count;
    }



    // VOTES
    public void addVote(Vote vote) {
        this.votes.add(vote);
    }

    public void removeVote(Vote vote) {
        this.votes.remove(vote);
    }

    public Optional<Vote> findVoteByUserId(UUID userId) {
        for (Vote vote : this.votes) {
            if(vote.getUser().getId().equals(userId)){
                return Optional.of(vote);
            }
        }
        return Optional.empty();
    }

    public Integer getDownvotes() {
        int voting = 0;
        for (Vote v : this.votes){
            if(v.getVoteType().equals(VoteType.DOWN)){
                voting ++;
            }
        }
        return voting;
    }

    public Integer getUpvotes() {
        int voting = 0;
        for (Vote v : this.votes){
            if(v.getVoteType().equals(VoteType.UP)){
                voting ++;
            }
        }
        return voting;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post post)) return false;
        return getId().equals(post.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}
