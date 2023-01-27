package de.hsos.swa.infrastructure.persistence.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import de.hsos.swa.domain.entity.Topic;
import de.hsos.swa.infrastructure.persistence.model.TopicPersistenceModel;

import java.time.LocalDateTime;
import java.util.UUID;

@EntityView(TopicPersistenceModel.class)
public record TopicPersistenceView(
        @IdMapping UUID id,
        String title,
        String description,
        LocalDateTime createdAt,
        @Mapping("userPersistenceModel") UserPersistenceView owner
) {
    public static Topic toDomainEntity(TopicPersistenceView view) {
        return new Topic(view.id, view.title, view.description, view.createdAt, UserPersistenceView.toDomainEntity(view.owner));
    }
};

