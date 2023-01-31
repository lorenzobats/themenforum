package de.hsos.swa.application.input.dto.out;

import de.hsos.swa.application.annotations.InputPortDTO;
import de.hsos.swa.domain.entity.User;
import de.hsos.swa.domain.entity.Vote;
import de.hsos.swa.domain.vo.VotedEntityType;

import java.time.LocalDateTime;
import java.util.UUID;

@InputPortDTO
public class UserInputPortDto {
    // TODO: nutzen für Admin ansicht
    public final User user;
    public final boolean deleted;
    public final  Long createdPosts;
    public final  Long createdTopics;
    public final  Long createdComments;
    public final  Long votedPosts;
    public final  Long votedArticles;
    public final LocalDateTime registeredAt;

    public UserInputPortDto(User user, boolean deleted, Long createdPosts, Long createdTopics, Long createdComments, Long votedPosts, Long votedArticles, LocalDateTime registeredAt) {
        this.user = user;
        this.deleted = deleted;
        this.createdPosts = createdPosts;
        this.createdTopics = createdTopics;
        this.createdComments = createdComments;
        this.votedPosts = votedPosts;
        this.votedArticles = votedArticles;
        this.registeredAt = registeredAt;
    }
}
