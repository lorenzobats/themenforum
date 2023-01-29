package de.hsos.swa.infrastructure.persistence.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import com.blazebit.persistence.view.MappingSubquery;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.infrastructure.persistence.model.CommentPersistenceModel;
import de.hsos.swa.infrastructure.persistence.view.subquery.PostCountSubqueryProvider;

import java.time.LocalDateTime;
import java.util.UUID;

@EntityView(CommentPersistenceModel.class)
public record CommentPersistenceView(
        @IdMapping UUID id,
        String text,
        LocalDateTime createdAt,
        @Mapping("userPersistenceModel") UserPersistenceView user,
        boolean active



){
    public static Comment toDomainEntity(CommentPersistenceView view) {
        return new Comment(view.id, view.createdAt, UserPersistenceView.toDomainEntity(view.user), view.text, view.active);
    }
}

