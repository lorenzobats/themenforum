package de.hsos.swa.infrastructure.persistence.view.subquery;

import com.blazebit.persistence.SubqueryInitiator;
import com.blazebit.persistence.view.SubqueryProvider;
import de.hsos.swa.domain.vo.VoteType;
import de.hsos.swa.infrastructure.persistence.model.VotePersistenceModel;

public class PostDownvoteCountSubqueryProvider implements SubqueryProvider {

    @Override
    public <T> T createSubquery(SubqueryInitiator<T> subqueryBuilder) {
        return subqueryBuilder.from(VotePersistenceModel.class, "downVotedPosts")
                .select("COUNT(*)")
                .where("downVotedPosts.voteType").eq(VoteType.DOWN)
                .where("downVotedPosts.postVote.id").eqExpression("EMBEDDING_VIEW(id)")
                .end();
    }
}