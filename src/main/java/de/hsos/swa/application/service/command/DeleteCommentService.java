package de.hsos.swa.application.service.command;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.command.DeleteCommentUseCase;
import de.hsos.swa.application.input.dto.in.DeleteCommentCommand;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.output.auth.AuthorizationGateway;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.application.output.repository.dto.in.RepositoryResult;
import de.hsos.swa.application.service.util.AuthorizationResultMapper;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.application.output.auth.dto.in.AuthorizationResult;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

/**
 * Die Application Service Klasse DeleteCommentService implementiert das Interface
 * DeleteCommentUseCase der Boundary des Application Hexagons.
 * Es realisiert die Applikationslogik für das Löschen (=Dekativieren) eines Kommentars durch seinen Ersteller bzw. einen Admin.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see DeleteCommentUseCase            Korrespondierender Input-Port für diesen Service
 * @see DeleteCommentCommand            Korrespondierendes Request DTO für diesen Service
 * @see PostRepository                  Verwendeter Output-Port zum Speichern des Posts nach Löschen des Kommentars
 * @see AuthorizationGateway            Output-Port zur Zugriffskontrolle für Löschvorgang
 */
@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class DeleteCommentService implements DeleteCommentUseCase {
    @Inject
    PostRepository postRepository;
    @Inject
    AuthorizationGateway authorizationGateway;

    /**
     * Löscht (=deaktiviert) ein Kommentar auf Basis der übergebenen Informationen.
     *
     * @param command           enthält ID des zu löschenden Kommentars
     * @param requestingUser    enthält den Nutzernamen der Lösch-Anfrage
     * @return ApplicationResult<Comment> enthält gelöschtes/deaktiviertes Kommentar bzw. Fehlermeldung bei Misserfolg
     */
    @Override
    public ApplicationResult<Optional<Comment>> deleteComment(DeleteCommentCommand command, String requestingUser) {
        RepositoryResult<Post> postResult = this.postRepository.getPostByCommentId(UUID.fromString(command.commentId()));
        if (postResult.error())
            return ApplicationResult.notValid("Cannot find post for comment " + command.commentId());
        Post post = postResult.get();

        Optional<Comment> optionalComment = post.findCommentById(UUID.fromString(command.commentId()));
        if (optionalComment.isEmpty() || !optionalComment.get().isActive())
            return ApplicationResult.noContent(Optional.empty());

        AuthorizationResult<Boolean> permission = authorizationGateway.canDeleteComment(requestingUser, UUID.fromString(command.commentId()));
        if(permission.denied())
            return AuthorizationResultMapper.handleRejection(permission.status());

        Comment comment = optionalComment.get();
        post.delete(comment.getId());

        RepositoryResult<Post> updatePostResult = this.postRepository.updatePost(post);
        if (updatePostResult.error())
            return ApplicationResult.exception("Cannot update post");

        return ApplicationResult.ok(Optional.of(comment));
    }
}
