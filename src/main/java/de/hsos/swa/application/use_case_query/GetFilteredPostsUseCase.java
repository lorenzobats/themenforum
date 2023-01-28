package de.hsos.swa.application.use_case_query;

import de.hsos.swa.application.input.GetFilteredPostsInputPort;
import de.hsos.swa.application.input.dto.in.GetFilteredPostInputPortRequest;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.service.SortByDate;
import de.hsos.swa.domain.service.SortByUpvotes;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
public class GetFilteredPostsUseCase implements GetFilteredPostsInputPort {

    @Inject
    PostRepository postRepository;

    @Override
    public Result<List<Post>> getFilteredPosts(GetFilteredPostInputPortRequest request) {
        Result<List<Post>> postsResult = postRepository.getAllFilteredPosts(request.filterParams(), request.includeComments());
        if (postsResult.isSuccessful()) {

            List<Post> sortedPosts = new ArrayList<>(postsResult.getData());

            switch (request.sortingParams()) {
                case VOTES -> sortedPosts.sort(new SortByUpvotes());
                case DATE -> sortedPosts.sort(new SortByDate());
            }
            return Result.success(sortedPosts);
        }

        return Result.error("Cannot find Posts");
    }
}
