package de.hsos.swa.application.service.command;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.DeletePostUseCase;
import de.hsos.swa.application.input.dto.in.DeletePostCommand;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.output.auth.AuthorizationGateway;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.application.output.repository.dto.in.RepositoryResult;
import de.hsos.swa.application.service.AuthorizationResultMapper;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.application.output.auth.dto.in.AuthorizationResult;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

/**
 * Die UseCase Klasse DeletePostService implementiert das Interface
 * DeletePostUseCase der Boundary des Application Hexagons.
 * Es realisiert die Applikationslogik für das Löschen eines Beitrags durch seinen Ersteller bzw. einen Admin.
 *
 * @author Oliver Schlüter
 * @author Lorenzo Battiston
 * @version 1.0
 * @see DeletePostUseCase               Korrespondierende Input-Port für diesen Use Case
 * @see DeletePostCommand               Korrespondierende Request DTO für diesen Use Case
 * @see PostRepository                  Output-Port zum Löschen des Beitrags
 * @see AuthorizationGateway            Output-Port zur Zugriffskontrolle für Löschvorgang
 */
@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class DeletePostService implements DeletePostUseCase {
    @Inject
    PostRepository postRepository;

    @Inject
    AuthorizationGateway authorizationGateway;

    /**
     * Löscht ein Post auf Basis der übergebenen Informationen.
     *
     * @param command           enthält ID des zu löschenden Posts
     * @param requestingUser    enthält den Nutzernamen der Löschen-Anfrage
     * @return ApplicationResult<Post> enthält gelöschten Beitrag bzw. Fehlermeldung bei Misserfolg
     */
    @Override
    public ApplicationResult<Optional<Post>> deletePost(DeletePostCommand command, String requestingUser) {
        RepositoryResult<Post> existingPost = postRepository.getPostById(UUID.fromString(command.postId()), false);
        if(existingPost.error())
            return ApplicationResult.noContent(Optional.empty());

        AuthorizationResult<Boolean> permission = authorizationGateway.canDeletePost(requestingUser, UUID.fromString(command.postId()));
        if(permission.denied())
            return AuthorizationResultMapper.handleRejection(permission.status());

        RepositoryResult<Post> result = this.postRepository.deletePost(UUID.fromString(command.postId()));
        if (result.error()) {
            return ApplicationResult.exception("Cannot delete post");
        }

        return ApplicationResult.ok(Optional.of(result.get()));
    }
}
