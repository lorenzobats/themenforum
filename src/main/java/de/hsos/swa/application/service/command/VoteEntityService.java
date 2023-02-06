package de.hsos.swa.application.service.command;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.command.VoteEntityUseCase;
import de.hsos.swa.application.input.dto.in.VoteEntityCommand;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.output.auth.AuthorizationGateway;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.application.output.repository.dto.in.RepositoryResult;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.User;
import de.hsos.swa.domain.entity.Vote;
import de.hsos.swa.domain.service.VoteService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;


/**
 * Die UseCase Klasse VoteEntityService implementiert das Interface
 * VoteEntityUseCase der Boundary des Application Hexagons.
 * Es realisiert die Applikationslogik für das Voten von Kommentaren und Posts
 *
 * @author Oliver Schlüter
 * @author Lorenzo Battiston
 * @version 1.0
 * @see VoteEntityUseCase               Korrespondierende Input-Port für diesen Use Case
 * @see VoteEntityCommand               Korrespondierende Request DTO für diesen Use Case
 * @see VoteService                     Domain Service für das up/downvoten von Kommentaren und Posts
 * @see UserRepository                  Output-Port zum Laden des anfragenden Nutzers
 * @see PostRepository                  Output-Port zum Speichern des erzeugten Beitrags
 * @see AuthorizationGateway            Output-Port zum Speichern des Post-Inhabers für spätere Zugriffskontrolle
 */
@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class VoteEntityService implements VoteEntityUseCase {

    @Inject
    PostRepository postRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    VoteService voteService;

    @Inject
    AuthorizationGateway authorizationGateway;

    /**
     *
     * @param command            enthält ID der zu votenden Entität, sowie den Vote-Typ (UP/DOWN)
     *                           und Entity-Typ (Post/Comment)
     * @param requestingUser     enthält den Nutzernamen der Vote-Anfrage
     * @return ApplicationResult<Vote> enthält den getätigten Vote bzw. Fehlermeldung
     */
    @Override
    public ApplicationResult<Vote> vote(VoteEntityCommand command, String requestingUser) {
        RepositoryResult<User> userResult = this.userRepository.getUserByName(requestingUser);
        if (userResult.error())
            return ApplicationResult.exception("Cannot retrieve User");
        User user = userResult.get();

        RepositoryResult<Post> postResult = new RepositoryResult<>();
        Optional<Vote> optionalVote = Optional.empty();
        switch (command.entityType()) {
            case COMMENT -> {
                postResult = this.postRepository.getPostByCommentId(UUID.fromString(command.entityId()));
                if (postResult.ok()) {
                    Optional<Comment> optionalComment = postResult.get().findCommentById(UUID.fromString(command.entityId()));
                    if (optionalComment.isPresent()) {
                        optionalVote = this.voteService.vote(optionalComment.get(), user, command.voteType());
                        if (optionalVote.isEmpty())
                            return ApplicationResult.notValid("Comment could not be voted");
                    }
                }
            }
            case POST -> {
                postResult = this.postRepository.getPostById(UUID.fromString(command.entityId()),true);
                optionalVote = this.voteService.vote(postResult.get(), user, command.voteType());
                if (optionalVote.isEmpty())
                    return ApplicationResult.notValid("Post could not be voted");
            }
        }

        RepositoryResult<Post> updatePostResult = this.postRepository.updatePost(postResult.get());
        if (updatePostResult.ok() && optionalVote.isPresent()){
            if(authorizationGateway.addOwnership(requestingUser, optionalVote.get().getId()).denied())
                return ApplicationResult.exception("Cannot create voting");
            return ApplicationResult.ok(optionalVote.get());
        }

        return ApplicationResult.exception("Something went wrong during voting");
    }
}
