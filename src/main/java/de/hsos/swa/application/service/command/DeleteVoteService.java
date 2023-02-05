package de.hsos.swa.application.service.command;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.DeleteTopicUseCase;
import de.hsos.swa.application.input.DeleteVoteUseCase;
import de.hsos.swa.application.input.dto.in.DeleteTopicCommand;
import de.hsos.swa.application.input.dto.in.DeleteVoteCommand;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.output.auth.AuthorizationGateway;
import de.hsos.swa.application.output.auth.dto.in.AuthorizationResult;
import de.hsos.swa.application.output.repository.TopicRepository;
import de.hsos.swa.application.output.repository.dto.in.VoteQueryDto;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.application.output.repository.VoteRepository;
import de.hsos.swa.application.output.repository.dto.out.RepositoryResult;
import de.hsos.swa.application.service.AuthorizationResultMapper;
import de.hsos.swa.domain.entity.*;
import de.hsos.swa.domain.service.VoteService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

/**
 * Die UseCase Klasse DeleteVoteService implementiert das Interface
 * DeleteVoteUseCase der Boundary des Application Hexagons.
 * Es realisiert die Applikationslogik für das Löschen eines Themas durch einen Admin.
 *
 * @author Oliver Schlüter
 * @author Lorenzo Battiston
 * @version 1.0
 * @see DeleteVoteUseCase               Korrespondierende Input-Port für diesen Use Case
 * @see DeleteVoteCommand               Korrespondierende Request DTO für diesen Use Case
 * @see VoteRepository                  Output-Port zum Finden des Votes
 * @see PostRepository                  Output-Port zum Updaten des Posts nach Löschen des Votes
 * @see VoteService                     Domain Service für das Löschen von Votes
 * @see AuthorizationGateway            Output-Port zur Zugriffskontrolle für Löschvorgang
 */
@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class DeleteVoteService implements DeleteVoteUseCase {

    @Inject
    VoteRepository voteRepository;
    @Inject
    PostRepository postRepository;
    @Inject
    VoteService voteService;
    @Inject
    AuthorizationGateway authorizationGateway;

    /**
     *
     * @param command            enthält ID des zu löschenden Votes
     * @param requestingUser     enthält den Nutzernamen der Lösch-Anfrage
     * @return ApplicationResult<Optional<Vote>> enthält gelöschten Vote bzw. Fehlermeldung bei Misserfolg
     */
    @Override
    public ApplicationResult<Optional<Vote>> deleteVote(DeleteVoteCommand command, String requestingUser) {
        RepositoryResult<VoteQueryDto> voteResult = this.voteRepository.getVoteById(UUID.fromString(command.voteId()));
        if (voteResult.error())
            return ApplicationResult.noContent(Optional.empty());
        VoteQueryDto vote = voteResult.get();
        User user = vote.vote().getUser();

        AuthorizationResult<Boolean> permission = authorizationGateway.canDeleteVote(requestingUser, UUID.fromString(command.voteId()));
        if(permission.denied())
            return AuthorizationResultMapper.handleRejection(permission.status());


        RepositoryResult<Post> postResult = new RepositoryResult<>();
        Optional<Vote> optionalVote = Optional.empty();
        switch (vote.votedEntityType()) {
            case COMMENT -> {
                postResult = this.postRepository.getPostByCommentId(vote.votedEntityId());
                if (postResult.ok()) {
                    Optional<Comment> optionalComment = postResult.get().findCommentById(vote.votedEntityId());
                    if (optionalComment.isPresent()) {
                        optionalVote = this.voteService.deleteVote(optionalComment.get(), user);
                    }
                }
            }
            case POST -> {
                postResult = this.postRepository.getPostById(vote.votedEntityId(),true);
                if (postResult.ok()) {
                    optionalVote = this.voteService.deleteVote(postResult.get(), user);
                }
            }
        }

        if (optionalVote.isEmpty())
            return ApplicationResult.noContent(Optional.empty());

        RepositoryResult<Post> updatePostResult = this.postRepository.updatePost(postResult.get());
        if (updatePostResult.error())
            return ApplicationResult.exception("Cannot update post ");

        return ApplicationResult.ok(optionalVote);
    }
}
