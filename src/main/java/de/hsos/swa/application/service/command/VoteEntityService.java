package de.hsos.swa.application.service.command;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.VoteEntityUseCase;
import de.hsos.swa.application.input.dto.in.VoteEntityCommand;
import de.hsos.swa.application.input.dto.out.Result;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.application.output.repository.dto.out.RepositoryResult;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.User;
import de.hsos.swa.domain.entity.Vote;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class VoteEntityService implements VoteEntityUseCase {

    @Inject
    PostRepository postRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    de.hsos.swa.domain.service.VoteEntityService voteEntityService;


    @Override
    public Result<Vote> vote(VoteEntityCommand request) {
        de.hsos.swa.application.output.repository.dto.out.RepositoryResult<User> userResult = this.userRepository.getUserByName(request.username());
        if (userResult.badResult()) {
            return Result.error("Cannot retrieve User");
        }
        User user = userResult.get();


        RepositoryResult<Post> postResult = new RepositoryResult<>();
        Optional<Vote> optionalVote = Optional.empty();
        switch (request.entityType()) {
            case COMMENT -> {
                postResult = this.postRepository.getPostByCommentId(UUID.fromString(request.entityId()));
                if (postResult.ok()) {
                    Optional<Comment> optionalComment = postResult.get().findCommentById(request.entityId());
                    if (optionalComment.isPresent()) {
                        optionalVote = this.voteEntityService.vote(optionalComment.get(), user, request.voteType());
                        if (optionalVote.isEmpty()) {
                            return Result.error("Comment could not be voted");
                        }
                    }
                }
            }
            case POST -> {
                postResult = this.postRepository.getPostById(UUID.fromString(request.entityId()),true);
                optionalVote = this.voteEntityService.vote(postResult.get(), user, request.voteType());
                if (optionalVote.isEmpty()) {
                    return Result.error("Post could not be voted");
                }
            }
        }


        RepositoryResult<Post> updatePostResult = this.postRepository.updatePost(postResult.get());

        if (updatePostResult.ok() && optionalVote.isPresent()) {
            return Result.success(optionalVote.get());
        }

        return Result.error("Something went wrong while voting Entity");
    }
}