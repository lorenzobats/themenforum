package de.hsos.swa.application.use_case_query;

import de.hsos.swa.application.input.GetPostByIdInputPort;
import de.hsos.swa.application.input.dto.in.GetPostByIdInputPortRequest;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.service.SortByDate;
import de.hsos.swa.domain.service.SortByUpvotes;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
public class GetPostByIdUseCase implements GetPostByIdInputPort {

    @Inject
    PostRepository postRepository;

    @Override
    public Result<Post> getPostById(GetPostByIdInputPortRequest request) {
        Result<Post> postResult = postRepository.getPostById(UUID.fromString(request.id()), request.includeComments());

        if (postResult.isSuccessful()) {
            List<Comment> sortedComments = new ArrayList<>(postResult.getData().getComments());

            switch (request.sortingParams()) {
                case VOTES -> {
                    if (request.orderParams() == OrderParams.ASC) {
                        sortedComments.sort(new SortByUpvotes<Comment>());
                    } else {
                        sortedComments.sort(new SortByUpvotes<Comment>().reversed());
                    }
                }
                case DATE -> {
                    if (request.orderParams() == OrderParams.ASC) {
                        sortedComments.sort(new SortByDate<Comment>());
                    } else {
                        sortedComments.sort(new SortByDate<Comment>().reversed());
                    }
                }
                default -> throw new IllegalArgumentException("Cant sort comments");
            }

            postResult.getData().setComments(sortedComments);

            return Result.success(postResult.getData());
        }
        return Result.error("Cannot find Post");
    }
}
