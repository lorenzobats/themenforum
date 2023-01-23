package de.hsos.swa.infrastructure.persistence.dto.out;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import de.hsos.swa.domain.value_object.Vote;
import de.hsos.swa.domain.value_object.VoteType;
import de.hsos.swa.infrastructure.persistence.model.PostVotePersistenceModel;

@EntityView(PostVotePersistenceModel.class)
public record PostVotePersistenceView(
        @IdMapping Long id,
        @Mapping("userPersistenceModel") UserPersistenceView user,
        VoteType voteType
) {
    public static Vote toDomainEntity(PostVotePersistenceView view) {
        return new Vote(UserPersistenceView.toDomainEntity(view.user), view.voteType);
    }
}
