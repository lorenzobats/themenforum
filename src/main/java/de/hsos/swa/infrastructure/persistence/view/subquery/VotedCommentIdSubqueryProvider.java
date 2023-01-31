package de.hsos.swa.infrastructure.persistence.view.subquery;

import com.blazebit.persistence.SubqueryInitiator;
import com.blazebit.persistence.view.SubqueryProvider;
import de.hsos.swa.infrastructure.persistence.model.CommentPersistenceModel;

public class VotedCommentIdSubqueryProvider implements SubqueryProvider {

    @Override
    public <T> T createSubquery(SubqueryInitiator<T> subqueryBuilder) {
        return subqueryBuilder.from(CommentPersistenceModel.class, "votedComment")
                .select("id")
                .where("votedComment.votes.id").eqExpression("EMBEDDING_VIEW(id)")
                .end();
    }
}