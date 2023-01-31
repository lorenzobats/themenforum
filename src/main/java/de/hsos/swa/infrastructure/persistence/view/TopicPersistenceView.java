package de.hsos.swa.infrastructure.persistence.view;

import com.blazebit.persistence.view.*;
import de.hsos.swa.application.input.dto.out.TopicInputPortDto;
import de.hsos.swa.domain.entity.Topic;
import de.hsos.swa.infrastructure.persistence.model.TopicPersistenceModel;
import de.hsos.swa.infrastructure.persistence.view.subquery.PostCountSubqueryProvider;

import java.time.LocalDateTime;
import java.util.UUID;

@EntityView(TopicPersistenceModel.class)
public record TopicPersistenceView(
        @IdMapping UUID id,
        String title,
        String description,
        LocalDateTime createdAt,
        @Mapping("userPersistenceModel")
        UserPersistenceView owner,
        @MappingSubquery(PostCountSubqueryProvider.class)
        Long posts
) {

    // TODO: (QUELLE) https://persistence.blazebit.com/documentation/1.6/entity-view/manual/en_US/#anchor-subquery-mappings

    public static Topic toDomainEntity(TopicPersistenceView view) {
        return new Topic(view.id, view.title, view.description, view.createdAt, UserPersistenceView.toDomainEntity(view.owner));
    }

    public static TopicInputPortDto toOutputPortDto(TopicPersistenceView view) {
        Topic topic = new Topic(view.id, view.title, view.description, view.createdAt, UserPersistenceView.toDomainEntity(view.owner));
        return new TopicInputPortDto(topic, view.posts());
    }
};

