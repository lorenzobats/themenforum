package de.hsos.swa.domain.entity;

import de.hsos.swa.domain.vo.Vote;
import de.hsos.swa.domain.vo.VoteType;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.*;

// TODO: UUID Validation
public class Post {
    private UUID id;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    //@PastOrPresent
    //private LocalDate publishedDate;      // TODO: Date

    @Valid
    private Topic topic;
    @Valid
    private final User creator;

    private final List<Comment> comments = new ArrayList<>();

    private final Map<UUID, Vote> votes = new HashMap<>();

    public Post(UUID id, String title, String content, Topic topic, User creator) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.topic = topic;
        this.creator = creator;
    }

    public Post(String title, String content, Topic topic, User creator) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.content = content;
        this.topic = topic;
        this.creator = creator;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
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


    public void setId(UUID id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public void setTopic(Topic topic) {
        this.topic = topic;
    }


    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

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

    public Map<UUID, Vote> getVotes() {
        return votes;
    }

    public Set<UUID> getUsersOfVotes() {
        return votes.keySet();
    }
    public void removeVote(UUID userId) {
        this.votes.remove(userId);
    }

    public void setVote(Vote vote) {
        this.votes.put(vote.getUser().getId(), vote);
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
