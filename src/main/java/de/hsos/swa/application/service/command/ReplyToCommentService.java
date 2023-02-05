package de.hsos.swa.application.service.command;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.ReplyToCommentUseCase;
import de.hsos.swa.application.input.dto.in.ReplyToCommentCommand;
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
 * Die UseCase Klasse ReplyToCommentService implementiert das Interface
 * ReplyToCommentUseCase der Boundary des Application Hexagons.
 * Es realisiert die Applikationslogik für das Hinzufügen einer Antwort zu einem Kommentar durch den Nutzer.
 *
 * @author Oliver Schlüter
 * @author Lorenzo Battiston
 * @version 1.0
 * @see ReplyToCommentUseCase               Korrespondierende Input-Port für diesen Use Case
 * @see ReplyToCommentCommand               Korrespondierende Request DTO für diesen Use Case
 * @see UserRepository                      Output-Port zum Laden des anfragenden Nutzers
 * @see PostRepository                      Output-Port zum Speichern des Posts nach Hinzufügen des Kommentars
 * @see AuthorizationGateway                Output-Port zum Speichern des Kommentar-Inhabers für spätere Zugriffskontrolle
 */
@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class ReplyToCommentService implements ReplyToCommentUseCase {
    @Inject
    PostRepository postRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    AuthorizationGateway authorizationGateway;

    /**
     * Fügt ein Kommentar zu einem bestehenden Kommentar hinzu auf Basis der übergebenen Informationen.
     *
     * @param command           enthält Kommentartext, Eltern-Kommentar-Id für das zu erstellende Kommentar
     * @param requestingUser    enthält den Nutzernamen des Erstellers
     * @return ApplicationResult<Comment> enthält erzeugtes Kommentar bzw. Fehlermeldung bei Misserfolg
     */
    @Override
    public ApplicationResult<Comment> replyToComment(ReplyToCommentCommand command, String requestingUser) {
        RepositoryResult<User> userResult = this.userRepository.getUserByName(requestingUser);
        if (userResult.error())
            return ApplicationResult.notValid("Cannot find user: " + requestingUser);
        User user = userResult.get();

        RepositoryResult<Post> postResult = this.postRepository.getPostByCommentId(UUID.fromString(command.commentId()));
        if (postResult.error())
            return ApplicationResult.notValid("Cannot find post " + command.commentId());
        Post post = postResult.get();

        Comment reply = CommentFactory.createComment(command.commentText(), user);
        if(!post.addReplyToComment(UUID.fromString(command.commentId()), reply)){
            return ApplicationResult.notValid("Comment you want to reply to is a disabled comment");
        }

        RepositoryResult<Post> updatedPostResult = this.postRepository.updatePost(post);
        if (updatedPostResult.error()) {
            return ApplicationResult.exception("Cannot reply to comment");
        }
        if(authorizationGateway.addOwnership(requestingUser, reply.getId()).denied())
            return ApplicationResult.exception("Cannot reply to comment");
        return ApplicationResult.ok(reply);
    }
}
