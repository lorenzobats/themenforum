package de.hsos.swa.domain.entity;

import de.hsos.swa.domain.vo.VoteType;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;
import java.util.*;

public class Post implements SortedEntity, VotedEntity {
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
    private User user;

    @NotNull
    private List<Comment> comments = new ArrayList<>();

    @NotNull
    private List<Vote> votes = new ArrayList<>();


    //------------------------------------------------------------------------------------------------------------------
    // CONSTRUCTORS

    // Für Persistence Adapter
    public Post(UUID id, String title, String content, LocalDateTime createdAt, Topic topic, User user) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.topic = topic;
        this.user = user;
    }

    // Für Factory
    public Post(String title, String content, LocalDateTime createdAt, Topic topic, User user) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.topic = topic;
        this.user = user;
    }


    //------------------------------------------------------------------------------------------------------------------
    // SIMPLE GETTER
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

    public User getUser() {
        return user;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    //------------------------------------------------------------------------------------------------------------------
    // COMMENT
    public void sortComments(boolean inDescendingOrder, Comparator<Comment> sortingComparator) {
        Queue<Comment> commentsQueue = new LinkedList<>(this.comments);
        this.comments.sort(sortingComparator);
        if (inDescendingOrder) {
            Collections.reverse(this.comments);
        }
        while (!commentsQueue.isEmpty()) {
            Comment comment = commentsQueue.remove();
            comment.getReplies().sort(sortingComparator);
            if (inDescendingOrder) {
                Collections.reverse(comment.getReplies());
            }
            commentsQueue.addAll(comment.getReplies());
        }
    }

    public Optional<Comment> findCommentById(UUID commentId) {
        Deque<Comment> stack = new ArrayDeque<>(this.comments);

        while (!stack.isEmpty()) {
            Comment comment = stack.pop();
            if (comment.getId().equals(commentId)) {
                return Optional.of(comment);
            }
            stack.addAll(comment.getReplies());
        }

        return Optional.empty();
    }

    public void add(Comment comment) {
        this.comments.add(comment);
    }
    public void delete(UUID commentId) {
        Optional<Comment> optionalComment = this.findCommentById(commentId);
        if(optionalComment.isPresent()){
            Comment comment = optionalComment.get();
            comment.disable();
        }
    }

    public boolean addReplyToComment(UUID parentCommentId, Comment reply) {
        Optional<Comment> parentComment = findCommentById(parentCommentId);
        if (parentComment.isPresent() && parentComment.get().isActive()) {
            parentComment.get().addReply(reply);
            return true;
        }
        return false;
    }


    //------------------------------------------------------------------------------------------------------------------
    // VOTES
    public void addVote(Vote vote) {
        this.votes.add(vote);
    }

    public void removeVote(Vote vote) {
        this.votes.remove(vote);
    }

    public Optional<Vote> findVoteByUser(UUID userId) {
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

    public Integer getTotalVotes() {
        return this.getUpvotes() - this.getDownvotes();
    }

    //------------------------------------------------------------------------------------------------------------------
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
