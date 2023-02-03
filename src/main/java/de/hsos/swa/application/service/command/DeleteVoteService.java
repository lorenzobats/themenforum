package de.hsos.swa.application.service.command;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.DeleteVoteUseCase;
import de.hsos.swa.application.input.dto.in.DeleteVoteCommand;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.output.repository.dto.in.VoteQueryDto;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.application.output.repository.VoteRepository;
import de.hsos.swa.application.output.repository.dto.out.RepositoryResult;
import de.hsos.swa.domain.entity.*;
import de.hsos.swa.domain.service.VoteEntityService;
import org.jboss.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class DeleteVoteService implements DeleteVoteUseCase {

    @Inject
    VoteRepository voteRepository;

    @Inject
    PostRepository postRepository;

    @Inject
    VoteEntityService voteEntityService;

    @Inject
    UserRepository userRepository;

    @Inject
    Logger log;

    @Override
    public ApplicationResult<Optional<Vote>> deleteVote(DeleteVoteCommand command, String requestingUser) {
        de.hsos.swa.application.output.repository.dto.out.RepositoryResult<User> userResult = this.userRepository.getUserByName(command.username());
        if (userResult.error()) {
            return ApplicationResult.exception("Cannot retrieve User");
        }
        User user = userResult.get();


        de.hsos.swa.application.output.repository.dto.out.RepositoryResult<VoteQueryDto> voteResult = this.voteRepository.getVoteById(UUID.fromString(command.voteId()));
        if (voteResult.error()) {
            return ApplicationResult.exception("Cannot find Vote");
        }
        VoteQueryDto vote = voteResult.get();

        RepositoryResult<Post> postResult = new RepositoryResult<>();
        Optional<Vote> optionalVote = Optional.empty();
        switch (vote.votedEntityType()) {
            case COMMENT -> {
                postResult = this.postRepository.getPostByCommentId(vote.votedEntityId());
                if (postResult.ok()) {
                    Optional<Comment> optionalComment = postResult.get().findCommentById(vote.votedEntityId());
                    if (optionalComment.isPresent()) {
                        optionalVote = this.voteEntityService.deleteVote(optionalComment.get(), user);
                    }
                }
            }
            case POST -> {
                postResult = this.postRepository.getPostById(vote.votedEntityId(),true);
                if (postResult.ok()) {
                    optionalVote = this.voteEntityService.deleteVote(postResult.get(), user);
                }
            }
        }

        if (optionalVote.isEmpty()) {
            return ApplicationResult.noContent(Optional.empty());
        }

        RepositoryResult<Post> updatePostResult = this.postRepository.updatePost(postResult.get());

        if (updatePostResult.error()) {
            return ApplicationResult.exception("Cannot update post ");
        }

        return ApplicationResult.ok(optionalVote);
    }
}
