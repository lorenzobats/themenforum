package de.hsos.swa.application.service.query;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.GetFilteredPostsUseCase;
import de.hsos.swa.application.input.dto.in.GetFilteredPostQuery;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.application.output.repository.dto.out.RepositoryResult;
import de.hsos.swa.application.service.query.params.OrderParams;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.service.SortByDate;
import de.hsos.swa.domain.service.SortByUpvotes;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class GetFilteredPostsService implements GetFilteredPostsUseCase {

    @Inject
    PostRepository postRepository;

    @Override
    public ApplicationResult<List<Post>> getFilteredPosts(GetFilteredPostQuery query) {
        RepositoryResult<List<Post>> result = postRepository.getFilteredPosts(query.filterParams(), query.includeComments());
        if(result.error())
            return ApplicationResult.exception();


        List<Post> sortedPosts = new ArrayList<>(result.get());
            switch (query.sortingParams()) {
                case VOTES -> sortPosts(sortedPosts, query.orderParams() == OrderParams.ASC, new SortByUpvotes<>());
                case DATE -> sortPosts(sortedPosts, query.orderParams() == OrderParams.ASC, new SortByDate<>());
                default -> {
                    return ApplicationResult.notValid("Invalid sorting param: " + query.sortingParams());
                }
            }
            return ApplicationResult.ok(sortedPosts);
    }

    private void sortPosts(List<Post> posts, boolean reversed, Comparator<Post> comparator) {
        posts.sort(comparator);
        if (reversed) {
            Collections.reverse(posts);
        }
    }
}
