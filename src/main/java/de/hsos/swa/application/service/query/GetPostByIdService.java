package de.hsos.swa.application.service.query;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.GetPostByIdUseCase;
import de.hsos.swa.application.input.dto.in.GetPostByIdQuery;
import de.hsos.swa.application.input.dto.out.Result;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.application.output.repository.dto.out.RepositoryResult;
import de.hsos.swa.application.service.query.params.OrderParams;
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
    public Result<Post> getPostById(GetPostByIdQuery request) {
        RepositoryResult<Post> postResult = postRepository.getPostById(UUID.fromString(request.id()), request.includeComments());

        if (postResult.ok()) {
            List<Comment> sortedComments = new ArrayList<>(postResult.get().getComments());

            switch (request.sortingParams()) {
                case VOTES -> {
                    sortReplies(sortedComments, request.orderParams() != OrderParams.ASC, new SortByUpvotes<>());
                }
                case DATE -> {
                    sortReplies(sortedComments, request.orderParams() != OrderParams.ASC, new SortByDate<>());
                }
                default -> throw new IllegalArgumentException("Cant sort comments");
            }

            postResult.get().setComments(sortedComments);

            return Result.success(postResult.get());
        }
        return Result.error("Cannot find Post");
    }

    private void sortReplies(List<Comment> comments, boolean reverse, Comparator<Comment> comparator) {
        Queue<Comment> queue = new LinkedList<>(comments);
        comments.sort(comparator);
        if (reverse) {
            Collections.reverse(comments);
        }
        while (!queue.isEmpty()) {
            Comment comment = queue.remove();
            comment.getReplies().sort(comparator);
            if (reverse) {
                Collections.reverse(comment.getReplies());
            }
            queue.addAll(comment.getReplies());
        }
    }
}
