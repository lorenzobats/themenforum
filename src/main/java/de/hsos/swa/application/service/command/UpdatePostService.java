package de.hsos.swa.application.service.command;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.command.UpdatePostUseCase;
import de.hsos.swa.application.input.dto.in.UpdatePostCommand;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.output.auth.AuthorizationGateway;
import de.hsos.swa.application.output.auth.dto.in.AuthorizationResult;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.application.output.repository.dto.in.RepositoryResult;
import de.hsos.swa.application.service.util.AuthorizationResultMapper;
import de.hsos.swa.domain.entity.Post;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.UUID;

/**
 * Die Application Service Klasse UpdatePostService implementiert das Interface
 * UpdatePostUseCase der Boundary des Application Hexagons.
 * Es realisiert die Applikationslogik für das Voten von Kommentaren und Posts
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see UpdatePostUseCase               Korrespondierender Input-Port für diesen Service
 * @see UpdatePostCommand               Korrespondierendes Request DTO für diesen Service
 * @see PostRepository                  Output-Port zum Speichern des angepassten Beitrags
 * @see AuthorizationGateway            Output-Port zur Zugriffskontrolle für UpdateVorgang
 */
@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class UpdatePostService implements UpdatePostUseCase {

    @Inject
    PostRepository postRepository;

    @Inject
    AuthorizationGateway authorizationGateway;

    /**
     * Aktualisiert optional Titel und Inhalt zum Aktualisieren eines bestehenden Beitrags
     * @param command           enthält ID des Posts, sowie optionalen neuen Titel und/oder Content
     * @param requestingUser    enthält den Nutzernamen der Update-Anfrage
     * @return ApplicationResult<Post>  enthält den Post, nachdem dieser optional aktualisiert wurde
     */
    @Override
    public ApplicationResult<Post> updatePost(UpdatePostCommand command, String requestingUser) {
        AuthorizationResult<Boolean> permission = authorizationGateway.canUpdatePost(requestingUser, UUID.fromString(command.postId()));
        if(permission.denied())
            return AuthorizationResultMapper.handleRejection(permission.status());

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

        if (command.title() != null)
            post.setTitle(command.title());

        if (command.content() != null)
            post.setContent(command.content());

        RepositoryResult<Post> updatePostResult = this.postRepository.updatePost(post);
        if (updatePostResult.error())
            return ApplicationResult.exception("Post could not be updated");

        return ApplicationResult.ok(post);
    }
}
