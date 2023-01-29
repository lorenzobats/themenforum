package de.hsos.swa.infrastructure.persistence.view.subquery;

import com.blazebit.persistence.SubqueryInitiator;
import com.blazebit.persistence.view.SubqueryProvider;
import de.hsos.swa.infrastructure.persistence.model.PostPersistenceModel;

public class PostCountSubqueryProvider implements SubqueryProvider {

    @Override
    public <T> T createSubquery(SubqueryInitiator<T> subqueryBuilder) {
        return subqueryBuilder.from(PostPersistenceModel.class, "topicPosts")
                .select("COUNT(*)")
                .where("topicPosts.topicPersistenceModel.id").eqExpression("EMBEDDING_VIEW(id)")
                .end();
    }
}