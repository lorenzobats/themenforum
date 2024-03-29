package de.hsos.swa.infrastructure.persistence.view;

import com.blazebit.persistence.view.*;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.infrastructure.persistence.model.PostPersistenceModel;

import java.time.LocalDateTime;
import java.util.UUID;

@EntityView(PostPersistenceModel.class)
public record PostPersistenceView(
        @IdMapping UUID id,
        String title,
        String content,
        LocalDateTime createdAt,
        @Mapping("topicPersistenceModel") TopicPersistenceView topic,
        @Mapping("userPersistenceModel") UserPersistenceView user
) {
    public static Post toDomainEntity(PostPersistenceView view) {
        return new Post(view.id, view.title, view.content, view.createdAt, TopicPersistenceView.toDomainEntity(view.topic), UserPersistenceView.toDomainEntity(view.user));
    }
}

