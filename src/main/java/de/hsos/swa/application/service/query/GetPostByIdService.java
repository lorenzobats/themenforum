package de.hsos.swa.application.service.query;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.GetPostByIdUseCase;
import de.hsos.swa.application.input.dto.in.GetPostByIdQuery;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.application.service.query.params.OrderParams;
import de.hsos.swa.application.input.dto.out.Result;
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
        Result<Post> postResult = postRepository.getPostById(UUID.fromString(request.id()), request.includeComments());

        if (postResult.isSuccessful()) {
            List<Comment> sortedComments = new ArrayList<>(postResult.getData().getComments());

            switch (request.sortingParams()) {
                case VOTES -> {
                    sortReplies(sortedComments, request.orderParams() != OrderParams.ASC, new SortByUpvotes<>());
                }
                case DATE -> {
                    sortReplies(sortedComments, request.orderParams() != OrderParams.ASC, new SortByDate<>());
                }
                default -> throw new IllegalArgumentException("Cant sort comments");
            }

            postResult.getData().setComments(sortedComments);

            return Result.success(postResult.getData());
        }
        return Result.error("Cannot find Post");
    }

    private void sortReplies(List<Comment> comments, boolean reverse, Comparator<Comment> comparator) {
        Queue<Comment> queue = new LinkedList<>(comments);
        // Sort the top-level comments
        comments.sort(comparator);
        // Reverse the list if the reverse option is true
        if (reverse) {
            Collections.reverse(comments);
        }
        // While there are comments in the queue
        while (!queue.isEmpty()) {
            // Dequeue a comment
            Comment comment = queue.remove();
            // Sort the replies
            comment.getReplies().sort(comparator);
            // Reverse the list if the reverse option is true
            if (reverse) {
                Collections.reverse(comment.getReplies());
            }
            // Add the replies to the queue
            queue.addAll(comment.getReplies());
        }
    }
}
