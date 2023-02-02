package de.hsos.swa.application.service.command;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.CommentPostUseCase;
import de.hsos.swa.application.input.dto.in.CommentPostCommand;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
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
 * @see CommentPostUseCase            Korrespondierende Input-Port für diesen Use Case
 * @see CommentPostCommand     Korrespondierende Request DTO für diesen Use Case
 * @see UserRepository                  Verwendeter Output-Port zum Laden des anfragenden Nutzers
 * @see PostRepository                  Verwendeter Output-Port zum Speichern des Posts nach Hinzufügen des Kommentars
 */
@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
@ApplicationService
public class CommentPostService implements CommentPostUseCase {

    @Inject
    UserRepository userRepository;
    @Inject
    PostRepository postRepository;

    /**
     * Fügt ein Kommentar zu einem bestehenden Beitrag hinzu auf Basis der übergebenen Informationen.
     *
     * @param request         enthält Kommentartext, Beitrags-Id und Nutzernamen für das zu erstellende Kommentar
     * @param securityContext
     * @return ApplicationResult<Commentar> enthält erzeugtes Kommentar bzw. Fehlermeldung bei Misserfolg
     */
    @Override
    public ApplicationResult<Comment> commentPost(CommentPostCommand request, String securityContext) {
        de.hsos.swa.application.output.repository.dto.out.RepositoryResult<User> userResult = this.userRepository.getUserByName(request.username());
        if (userResult.error()) {
            return ApplicationResult.exception("Cannot find user " + request.username());
        }
        User user = userResult.get();

        RepositoryResult<Post> postResult = this.postRepository.getPostById(UUID.fromString(request.postId()), true);
        if (postResult.error()) {
            return ApplicationResult.exception("Cannot find post " + request.postId());
        }
        Post post = postResult.get();

        Comment comment = CommentFactory.createComment(request.commentText(), user);
        post.add(comment);

        RepositoryResult<Post> updatePostResult = this.postRepository.updatePost(post);
        if (updatePostResult.error()) {
            return ApplicationResult.exception("Cannot update post " + request.postId());
        }
        return ApplicationResult.ok(comment);
    }
}
