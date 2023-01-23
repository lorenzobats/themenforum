package de.hsos.swa.infrastructure.persistence.dto.out;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.infrastructure.persistence.model.CommentPersistenceModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@EntityView(CommentPersistenceModel.class)
public record SimpleCommentPersistenceView(
        @IdMapping UUID id,
        String text,
        LocalDateTime createdAt,
        @Mapping("userPersistenceModel") UserPersistenceView user
) {
    public static Comment toDomainEntity(SimpleCommentPersistenceView view) {
        return new Comment(view.id, view.createdAt, UserPersistenceView.toDomainEntity(view.user), view.text);
    }
};

