package de.hsos.swa.domain.entity;

import de.hsos.swa.domain.value_object.Vote;
import de.hsos.swa.domain.value_object.VoteType;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;
import java.util.*;

// TODO: Validierung spezifischer (zB. Titellänge, Descriptionlänge, etc.)
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
    private final Map<UUID, Vote> votes = new HashMap<>();


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

    public Map<UUID, Vote> getVotes() {
        return votes;
    }


    // SETTER

    // COMMENTS
    // TODO: in Comment Service auslagern?
    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    // TODO: in Comment Service auslagern?
    public void addReplyToComment(String parentCommentId, Comment reply) {
        Optional<Comment> parentComment = findCommentById(parentCommentId);
        parentComment.ifPresent(comment -> comment.addReply(reply));
    }

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


    // VOTES
    public void setVote(Vote vote) {
        this.votes.put(vote.getUser().getId(), vote);
    }
    public void removeVote(UUID userId) {
        this.votes.remove(userId);
    }

    public Integer getDownVotes() {
        int voting = 0;
        for (Vote v : this.votes.values()){
            voting += (v.getVoteType().equals(VoteType.DOWN) ? 1 : 0);
        }
        return voting;
    }

    public Integer getUpVotes() {
        int voting = 0;
        for (Vote v : this.votes.values()){
            voting += (v.getVoteType().equals(VoteType.UP) ? 1 : 0);
        }
        return voting;
    }
}
