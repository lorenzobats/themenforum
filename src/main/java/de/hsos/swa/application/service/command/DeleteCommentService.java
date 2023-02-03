package de.hsos.swa.application.service.command;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.DeleteCommentUseCase;
import de.hsos.swa.application.input.dto.in.DeleteCommentCommand;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.output.auth.AuthorizationGateway;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.application.output.repository.dto.out.RepositoryResult;
import de.hsos.swa.application.service.AuthorizationResultMapper;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.User;
import de.hsos.swa.application.output.auth.dto.in.AuthorizationResult;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

/**
 * Die UseCase Klasse DeleteCommentService implementiert das Interface
 * DeleteCommentUseCase der Boundary des Application Hexagons.
 * Es realisiert die Applikationslogik für das Löschen (=Dekativieren) eines Kommentars durch seinen Ersteller bzw. einen Admin.
 *
 * @author Oliver Schlüter
 * @author Lorenzo Battiston
 * @version 1.0
 * @see DeleteCommentUseCase          Korrespondierende Input-Port für diesen Use Case
 * @see DeleteCommentCommand   Korrespondierende Request DTO für diesen Use Case
 * @see UserRepository                  Verwendeter Output-Port zum Laden des anfragenden Nutzers
 * @see PostRepository                  Verwendeter Output-Port zum Speichern des Posts nach Hinzufügen des Kommentars
 * @see AuthorizationGateway       Verwendeter Output-Port zum Laden der Rolle des anfragenden Nutzers
 */
@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class DeleteCommentService implements DeleteCommentUseCase {
    @Inject
    UserRepository userRepository;

    @Inject
    PostRepository postRepository;

    @Inject
    AuthorizationGateway authorizationGateway;


    /**
     * Löscht (=deaktiviert) ein Kommentar auf Basis der übergebenen Informationen.
     *
     * @param command         enthält Kommentar-ID und Nutzernamen der Lösch-Anfrage
     * @param requestingUser
     * @return ApplicationResult<Comment> enthält gelöschtes/deaktiviertes Kommentar bzw. Fehlermeldung bei Misserfolg
     */
    @Override
    public ApplicationResult<Optional<Comment>> deleteComment(DeleteCommentCommand command, String requestingUser) {

        RepositoryResult<Post> postResult = this.postRepository.getPostByCommentId(UUID.fromString(command.commentId()));
        if (postResult.error()) {
            return ApplicationResult.exception("Cannot find post for comment " + command.commentId());
        }
        Post post = postResult.get();

        Optional<Comment> optionalComment = post.findCommentById(UUID.fromString(command.commentId()));

        if (optionalComment.isEmpty()) {
            return ApplicationResult.noContent(Optional.empty());
        }
        Comment comment = optionalComment.get();

        AuthorizationResult<Boolean> permission = authorizationGateway.canDeleteComment(requestingUser, comment.getId());
        if(permission.denied())
            return AuthorizationResultMapper.handleRejection(permission.status());

        post.delete(comment.getId());

        RepositoryResult<Post> updatePostResult = this.postRepository.updatePost(post);
        if (updatePostResult.error()) {
            return ApplicationResult.exception("Cannot update post ");
        }

        return ApplicationResult.ok(Optional.of(comment));
    }
}
