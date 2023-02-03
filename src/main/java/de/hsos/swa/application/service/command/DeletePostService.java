package de.hsos.swa.application.service.command;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.DeletePostUseCase;
import de.hsos.swa.application.input.dto.in.DeletePostCommand;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.output.auth.AuthorizationGateway;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.application.output.repository.dto.out.RepositoryResult;
import de.hsos.swa.application.service.AuthorizationResultMapper;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.User;
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
 * @see DeletePostUseCase             Korrespondierende Input-Port für diesen Use Case
 * @see DeletePostCommand      Korrespondierende Request DTO für diesen Use Case
 * @see UserRepository                  Verwendeter Output-Port zum Laden des anfragenden Nutzers
 * @see PostRepository                  Verwendeter Output-Port zum Löschen des Beitrags
 * @see AuthorizationGateway       Verwendeter Output-Port zum Laden der Rolle des anfragenden Nutzers
 */
@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class DeletePostService implements DeletePostUseCase {

    @Inject
    UserRepository userRepository;

    @Inject
    PostRepository postRepository;

    @Inject
    AuthorizationGateway authorizationGateway;


    /**
     * Löscht ein Post auf Basis der übergebenen Informationen.
     *
     * @param command         enthält Post-ID und Nutzernamen der Lösch-Anfrage
     * @param requestingUser
     * @return ApplicationResult<Post> enthält gelöschten Beitrag bzw. Fehlermeldung bei Misserfolg
     */
    @Override
    public ApplicationResult<Optional<Post>> deletePost(DeletePostCommand command, String requestingUser) {
        RepositoryResult<Post> postResult = this.postRepository.getPostById(UUID.fromString(command.postId()), false);
        if (postResult.error()) {
            return ApplicationResult.exception("Cannot find post" + command.postId());
        }
        Post post = postResult.get();


        AuthorizationResult<Boolean> permission = authorizationGateway.canDeletePost(requestingUser, post.getId());
        if(permission.denied())
            return AuthorizationResultMapper.handleRejection(permission.status());


        RepositoryResult<Post> deletePostResult = this.postRepository.deletePost(post.getId());
        if (deletePostResult.error()) {
            switch (deletePostResult.status()) {
                case ENTITY_NOT_FOUND -> {
                    return ApplicationResult.noContent(Optional.empty());
                }
                case EXCEPTION -> {
                    return ApplicationResult.exception("Cannot delete post");
                }
            }
            return ApplicationResult.exception("Cannot delete post ");
        }

        return ApplicationResult.ok(Optional.of(deletePostResult.get()));
    }
}
