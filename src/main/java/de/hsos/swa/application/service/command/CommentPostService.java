package de.hsos.swa.application.service.command;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.CommentPostUseCase;
import de.hsos.swa.application.input.dto.in.CommentPostCommand;
import de.hsos.swa.application.output.repository.RepositoryResult;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.application.input.dto.out.Result;
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
     * @param request enthält Kommentartext, Beitrags-Id und Nutzernamen für das zu erstellende Kommentar
     * @return Result<Commentar> enthält erzeugtes Kommentar bzw. Fehlermeldung bei Misserfolg
     */
    @Override
    public Result<Comment> commentPost(CommentPostCommand request) {
        RepositoryResult<User> userResult = this.userRepository.getUserByName(request.username());
        if (userResult.badResult()) {
            return Result.error("Cannot find user " + request.username());
        }
        User user = userResult.get();


        Result<Post> postResult = this.postRepository.getPostById(UUID.fromString(request.postId()), true);
        if (!postResult.isSuccessful()) {
            return Result.error("Cannot find post " + request.postId());
        }
        Post post = postResult.getData();

        Comment comment = CommentFactory.createComment(request.commentText(), user);
        post.addComment(comment);

        Result<Post> updatePostResult = this.postRepository.updatePost(post);
        if (!updatePostResult.isSuccessful()) {
            return Result.error("Cannot update post " + request.postId());
        }
        return Result.success(comment);
    }
}
