package de.hsos.swa.application.service.query;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.GetPostByIdUseCase;
import de.hsos.swa.application.input.dto.in.GetPostByIdQuery;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.application.output.repository.dto.in.RepositoryResult;
import de.hsos.swa.application.service.query.params.OrderParams;
import de.hsos.swa.application.service.query.params.SortingParams;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.service.SortByDate;
import de.hsos.swa.domain.service.SortByUpvotes;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.*;

@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class GetPostByIdService implements GetPostByIdUseCase {

    @Inject
    PostRepository postRepository;

    @Override
    public ApplicationResult<Post> getPostById(GetPostByIdQuery query) {
        RepositoryResult<Post> postResult = postRepository.getPostById(UUID.fromString(query.id()), query.includeComments());

        if (postResult.ok()) {
            Comparator<Comment> sortComparator = new SortByDate<>();    // Im Standard wird nach Date Sortiert
            if (SortingParams.valueOf(query.sortingParams()) == SortingParams.VOTES)
                sortComparator = new SortByUpvotes<>();
            boolean descending = OrderParams.valueOf(query.orderParams()) == OrderParams.DESC;

            postResult.get().sortComments(descending, sortComparator);
            return ApplicationResult.ok(postResult.get());
        }

        switch (postResult.status()) {
            case ENTITY_NOT_FOUND -> {
                return ApplicationResult.notFound(query.id() + "not found");
            }
        }
        return ApplicationResult.exception("Cannot find Post");
    }


}
