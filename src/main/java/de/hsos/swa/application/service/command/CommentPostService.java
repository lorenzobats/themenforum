package de.hsos.swa.application.service.command;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.CommentPostUseCase;
import de.hsos.swa.application.input.dto.in.CommentPostCommand;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.output.auth.AuthorizationGateway;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.application.output.repository.dto.out.RepositoryResult;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.User;
import de.hsos.swa.domain.factory.CommentFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.UUID;

/**
 * Die UseCase Klasse CommentPostService implementiert das Interface
 * CommentPostUseCase der Boundary des Application Hexagons.
 * Es realisiert die Applikationslogik für das Hinzufügen eines Kommentars zu einem Beitrag durch den Nutzer.
 *
 * @author Oliver Schlüter
 * @author Lorenzo Battiston
 * @version 1.0
 * @see CommentPostUseCase              Korrespondierende Input-Port für diesen Use Case
 * @see CommentPostCommand              Korrespondierende Request DTO für diesen Use Case
 * @see UserRepository                  Output-Port zum Laden des anfragenden Nutzers
 * @see PostRepository                  Output-Port zum Speichern des Posts nach Hinzufügen des Kommentars
 * @see AuthorizationGateway            Output-Port zum Speichern des Kommentar-Inhabers für spätere Zugriffskontrolle
 */
@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class CommentPostService implements CommentPostUseCase {

    @Inject
    UserRepository userRepository;
    @Inject
    PostRepository postRepository;

    @Inject
    AuthorizationGateway authorizationGateway;

    /**
     * Fügt ein Kommentar zu einem bestehenden Beitrag hinzu auf Basis der übergebenen Informationen.
     *
     * @param command           enthält Kommentartext, Beitrags-Id für das zu erstellende Kommentar
     * @param requestingUser    enthält den Nutzernamen des Erstellers
     * @return ApplicationResult<Commentar> enthält erzeugtes Kommentar bzw. Fehlermeldung bei Misserfolg
     */
    @Override
    public ApplicationResult<Comment> commentPost(CommentPostCommand command, String requestingUser) {
        RepositoryResult<User> userResult = this.userRepository.getUserByName(requestingUser);
        if (userResult.error())
            return ApplicationResult.notValid("Cannot find user: " + requestingUser);
        User user = userResult.get();

        RepositoryResult<Post> postResult = this.postRepository.getPostById(UUID.fromString(command.postId()), true);
        if (postResult.error())
            return ApplicationResult.notValid("Cannot find post " + command.postId());
        Post post = postResult.get();

        Comment comment = CommentFactory.createComment(command.commentText(), user);
        post.add(comment);

        RepositoryResult<Post> updatedPostResult = this.postRepository.updatePost(post);
        if (updatedPostResult.error()) {
            return ApplicationResult.exception("Cannot update post " + command.postId());
        }
        if(authorizationGateway.addOwnership(requestingUser, comment.getId()).denied())
            return ApplicationResult.exception("Cannot create comment");
        return ApplicationResult.ok(comment);
    }
}
