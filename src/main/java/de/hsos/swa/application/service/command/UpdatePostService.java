package de.hsos.swa.application.service.command;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.UpdatePostUseCase;
import de.hsos.swa.application.input.dto.in.UpdatePostCommand;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.application.output.repository.dto.out.RepositoryResult;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.User;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.UUID;

@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class UpdatePostService implements UpdatePostUseCase {

    @Inject
    UserRepository userRepository;

    @Inject
    PostRepository postRepository;

    @Override
    public ApplicationResult<Post> updatePost(UpdatePostCommand command, String requestingUser) {
        RepositoryResult<User> userResult = this.userRepository.getUserByName(requestingUser);
        //TODO Autorisierung des Nutzers
        if (userResult.error()) {
            return ApplicationResult.exception("Cannot find user " + requestingUser);
        }

        RepositoryResult<Post> postResult = this.postRepository.getPostById(UUID.fromString(command.postId()), true);
        if (postResult.error()) {
            switch (postResult.status()) {
                case ENTITY_NOT_FOUND -> {
                    return ApplicationResult.notFound("Post not found " + command.postId());
                }
                case EXCEPTION -> {
                    return ApplicationResult.exception();
                }
            }
        }
        Post post = postResult.get();

        if (command.title() != null) {
            post.setTitle(command.title());
        }

        if (command.content() != null) {
            post.setContent(command.content());
        }

        RepositoryResult<Post> updatePostResult = this.postRepository.updatePost(post);
        if (updatePostResult.error()) {
            return ApplicationResult.exception();
        }

        return ApplicationResult.ok(post);
    }
}
