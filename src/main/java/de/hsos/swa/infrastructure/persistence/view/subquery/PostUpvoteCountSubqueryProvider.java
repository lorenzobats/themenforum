package de.hsos.swa.infrastructure.persistence.view.subquery;

import com.blazebit.persistence.SubqueryInitiator;
import com.blazebit.persistence.view.SubqueryProvider;
import de.hsos.swa.domain.entity.VoteType;
import de.hsos.swa.infrastructure.persistence.model.VotePersistenceModel;

public class PostUpvoteCountSubqueryProvider implements SubqueryProvider {

    @Override
    public <T> T createSubquery(SubqueryInitiator<T> subqueryBuilder) {
        return subqueryBuilder.from(VotePersistenceModel.class, "upVotedPosts")
                .select("COUNT(*)")
                .where("upVotedPosts.voteType").eq(VoteType.UP)
                .where("upVotedPosts.postVote.id").eqExpression("EMBEDDING_VIEW(id)")
                .end();
    }
}