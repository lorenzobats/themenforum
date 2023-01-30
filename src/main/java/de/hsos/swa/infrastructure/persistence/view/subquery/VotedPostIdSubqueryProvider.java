package de.hsos.swa.infrastructure.persistence.view.subquery;

import com.blazebit.persistence.SubqueryInitiator;
import com.blazebit.persistence.view.SubqueryProvider;
import de.hsos.swa.infrastructure.persistence.model.PostPersistenceModel;

public class VotedPostIdSubqueryProvider implements SubqueryProvider {

    @Override
    public <T> T createSubquery(SubqueryInitiator<T> subqueryBuilder) {
        return subqueryBuilder.from(PostPersistenceModel.class, "votedPost")
                .select("id")
                .where("votedPost.votes.id").eqExpression("EMBEDDING_VIEW(id)")
                .end();
    }
}