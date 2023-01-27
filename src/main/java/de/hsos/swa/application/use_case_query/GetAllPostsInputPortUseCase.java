package de.hsos.swa.application.use_case_query;

import de.hsos.swa.application.input.GetAllPostsInputPort;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Post;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
public class GetAllPostsInputPortUseCase implements GetAllPostsInputPort {

    @Inject
    PostRepository postRepository;

    @Override
    public Result<List<Post>> getAllPosts(boolean includeComments) {
        Result<List<Post>> postsResult = postRepository.getAllPosts(includeComments);
        if (postsResult.isSuccessful()) {
            return Result.success(postsResult.getData());
        }
        return Result.error("Cannot find Posts");
    }
}
