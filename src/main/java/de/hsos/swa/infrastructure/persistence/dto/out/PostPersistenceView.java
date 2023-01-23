package de.hsos.swa.infrastructure.persistence.dto.out;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.infrastructure.persistence.model.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@EntityView(PostPersistenceModel.class)
public record PostPersistenceView(
        @IdMapping UUID id,
        String title,
        String content,
        LocalDateTime createdAt,
        @Mapping("topicPersistenceModel") TopicPersistenceView topic,
        @Mapping("userPersistenceModel") UserPersistenceView user,
        List<CommentPersistenceView> comments

) {
    public static Post toDomainEntity(PostPersistenceView view) {
        List<Comment> comments = view.comments.stream().map(CommentPersistenceView::toDomainEntity).toList();
        return new Post(view.id, view.title, view.content, view.createdAt, TopicPersistenceView.toDomainEntity(view.topic), UserPersistenceView.toDomainEntity(view.user), comments);
    }
};

