package de.hsos.swa.application.service.query;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.GetFilteredPostsUseCase;
import de.hsos.swa.application.input.dto.in.GetFilteredPostQuery;
import de.hsos.swa.application.input.dto.out.Result;
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
    public Result<List<Post>> getFilteredPosts(GetFilteredPostQuery request) {
        RepositoryResult<List<Post>> postsResult = postRepository.getFilteredPosts(request.filterParams(), request.includeComments());

        if (postsResult.ok()) {
            List<Post> sortedPosts = new ArrayList<>(postsResult.get());

            switch (request.sortingParams()) {
                case VOTES -> {
                    sortPosts(sortedPosts, request.orderParams() == OrderParams.ASC, new SortByUpvotes<>());
                }
                case DATE -> {
                    sortPosts(sortedPosts, request.orderParams() == OrderParams.ASC, new SortByDate<>());
                }
                default -> throw new IllegalArgumentException("Cant sort posts");
            }
            return Result.success(sortedPosts);
        }

        return Result.error("Cannot find Posts");
    }

    private void sortPosts(List<Post> posts, boolean reversed, Comparator<Post> comparator) {
        posts.sort(comparator);

        if (reversed) {
            Collections.reverse(posts);
        }
    }
}
