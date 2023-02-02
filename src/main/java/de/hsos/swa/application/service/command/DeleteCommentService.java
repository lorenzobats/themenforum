package de.hsos.swa.application.service.command;

import de.hsos.swa.application.annotations.ApplicationService;
import de.hsos.swa.application.input.DeleteCommentUseCase;
import de.hsos.swa.application.input.dto.in.DeleteCommentCommand;
import de.hsos.swa.application.input.dto.out.Result;
import de.hsos.swa.application.output.auth.AuthorizationGateway;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.application.output.repository.dto.out.RepositoryResult;
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
     * @param request enthält Kommentar-ID und Nutzernamen der Lösch-Anfrage
     * @return Result<Comment> enthält gelöschtes/deaktiviertes Kommentar bzw. Fehlermeldung bei Misserfolg
     */
    @Override
    public Result<Comment> deleteComment(DeleteCommentCommand request) {
        RepositoryResult<Post> postResult = this.postRepository.getPostByCommentId(UUID.fromString(request.commentId()));
        if (postResult.badResult()) {
            return Result.error("Cannot find post for comment " + request.commentId());
        }
        Post post = postResult.get();

        Optional<Comment> optionalComment = post.findCommentById(UUID.fromString(request.commentId()));
        if(optionalComment.isEmpty()){
            return Result.error("Cannot find comment " + request.commentId());
        }
        Comment comment = optionalComment.get();


        // TODO Auth Service. Kann dann ganz an den Afang und findCommentById muss auch nicht mehr verwendet werden
        RepositoryResult<User> userResult = this.userRepository.getUserByName(request.username());
        if (userResult.badResult()) {
            return Result.error("Cannot find user " + request.username());
        }
        User user = userResult.get();

        AuthorizationResult<String> roleResult = this.authorizationGateway.getUserAuthRole(user.getId());
        if (roleResult.invalid()) {
            return Result.error("Cannot find user role " + request.username());
        }
        String role = roleResult.get();

        if(!comment.getUser().getId().equals(user.getId()) && !role.equals("admin")){
            return Result.error("Not allowed to delete comment");
        }
        // ENDE TODO AUTH SERVICE

        post.delete(comment.getId());


        RepositoryResult<Post> updatePostResult = this.postRepository.updatePost(post);
        if (updatePostResult.badResult()) {
            return Result.error("Cannot update post ");
        }
        return Result.success(comment);
    }
}
