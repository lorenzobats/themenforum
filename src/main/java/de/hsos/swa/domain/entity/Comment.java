package de.hsos.swa.domain.entity;

import de.hsos.swa.domain.vo.Vote;
import de.hsos.swa.domain.vo.VoteType;

import java.time.LocalDate;
import java.util.*;

public class Comment {
    private UUID id;
    private User user;
    private String text;
    private LocalDate publishedDate;

    private int upvotes;

    private List<Comment> replies = new ArrayList<>();

    private final Map<UUID, Vote> votes = new HashMap<>();

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


    public Map<UUID, Vote> getVotes() {
        return votes;
    }

    public Set<UUID> getUsersOfVotes() {
        return votes.keySet();
    }
    public void removeVote(UUID userId) {
        this.votes.remove(userId);
    }

    public Integer getVoteSum() {
        int voting = 0;
        for (Vote v : this.votes.values()){
            voting += (v.getVoteType().equals(VoteType.UP) ? 1 : -1);
        }
        return voting;
    }

    public void setVote(Vote vote) {
        this.votes.put(vote.getUser().getId(), vote);
    }

}
