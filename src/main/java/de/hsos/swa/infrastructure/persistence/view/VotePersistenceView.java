package de.hsos.swa.infrastructure.persistence.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import com.blazebit.persistence.view.MappingSubquery;
import de.hsos.swa.application.output.repository.dto.in.VoteQueryDto;
import de.hsos.swa.domain.vo.VotedEntityType;
import de.hsos.swa.domain.entity.Vote;
import de.hsos.swa.domain.vo.VoteType;
import de.hsos.swa.infrastructure.persistence.model.VotePersistenceModel;
import de.hsos.swa.infrastructure.persistence.view.subquery.VotedCommentIdSubqueryProvider;
import de.hsos.swa.infrastructure.persistence.view.subquery.VotedPostIdSubqueryProvider;

import java.time.LocalDateTime;
import java.util.UUID;

@EntityView(VotePersistenceModel.class)
public record VotePersistenceView(
        @IdMapping UUID id,
        @Mapping("userPersistenceModel") UserPersistenceView user,
        LocalDateTime createdAt,
        VoteType voteType,

        @MappingSubquery(VotedPostIdSubqueryProvider.class)
        UUID votedPostId,

        @MappingSubquery(VotedCommentIdSubqueryProvider.class)
        UUID votedCommentId
) {
    public static Vote toDomainEntity(VotePersistenceView view) {
        return new Vote(view.id, UserPersistenceView.toDomainEntity(view.user), view.voteType, view.createdAt);
    }

    public static VoteQueryDto toOutputPortDto(VotePersistenceView view) {
        if (view.votedCommentId != null){
            return new VoteQueryDto(VotePersistenceView.toDomainEntity(view), VotedEntityType.COMMENT, view.votedCommentId);
        }
        return new VoteQueryDto(VotePersistenceView.toDomainEntity(view), VotedEntityType.POST, view.votedPostId);
    }
}
