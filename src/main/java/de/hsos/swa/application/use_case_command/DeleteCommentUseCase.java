package de.hsos.swa.application.use_case_command;

import de.hsos.swa.application.input.CommentPostInputPort;
import de.hsos.swa.application.input.DeleteCommentInputPort;
import de.hsos.swa.application.input.dto.in.CommentPostInputPortRequest;
import de.hsos.swa.application.input.dto.in.DeleteCommentInputPortRequest;
import de.hsos.swa.application.output.auth.getUserAuthRole.GetUserAuthRoleOutputPort;
import de.hsos.swa.application.output.repository.PostRepository;
import de.hsos.swa.application.output.repository.UserRepository;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.User;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

/**
 * Die UseCase Klasse DeleteCommentUseCase implementiert das Interface
 * DeleteCommentInputPort der Boundary des Application Hexagons.
 * Es realisiert die Applikationslogik für das Löschen (=Dekativieren) eines Kommentars durch seinen Ersteller bzw. einen Admin.
 *
 * @author Oliver Schlüter
 * @author Lorenzo Battiston
 * @version 1.0
 * @see DeleteCommentInputPort          Korrespondierende Input-Port für diesen Use Case
 * @see DeleteCommentInputPortRequest   Korrespondierende Request DTO für diesen Use Case
 * @see UserRepository                  Verwendeter Output-Port zum Laden des anfragenden Nutzers
 * @see PostRepository                  Verwendeter Output-Port zum Speichern des Posts nach Hinzufügen des Kommentars
 * @see GetUserAuthRoleOutputPort       Verwendeter Output-Port zum Laden der Rolle des anfragenden Nutzers
 */
@RequestScoped
@Transactional(Transactional.TxType.REQUIRES_NEW)
public class DeleteCommentUseCase implements DeleteCommentInputPort {
    @Inject
    UserRepository userRepository;

    @Inject
    PostRepository postRepository;

    @Inject
    GetUserAuthRoleOutputPort userAuthRoleOutputPort;


    /**
     * Löscht (=deaktiviert) ein Kommentar auf Basis der übergebenen Informationen.
     * @param request enthält Kommentar-ID und Nutzernamen der Lösch-Anfrage
     * @return Result<Comment> enthält gelöschtes/deaktiviertes Kommentar bzw. Fehlermeldung bei Misserfolg
     */
    @Override
    public Result<Comment> deleteComment(DeleteCommentInputPortRequest request) {
        Result<Post> postResult = this.postRepository.getPostByCommentId(UUID.fromString(request.commentId()));
        if (!postResult.isSuccessful()) {
            return Result.error("Cannot find post for comment " + request.commentId());
        }
        Post post = postResult.getData();

        Optional<Comment> optionalComment = post.findCommentById(request.commentId());
        if(optionalComment.isEmpty()){
            return Result.error("Cannot find comment " + request.commentId());
        }
        Comment comment = optionalComment.get();

        Result<User> userResult = this.userRepository.getUserByName(request.username());
        if (!userResult.isSuccessful()) {
            return Result.error("Cannot find user " + request.username());
        }
        User user = userResult.getData();

        Result<String> roleResult = this.userAuthRoleOutputPort.getUserAuthRole(user.getId());
        if (!roleResult.isSuccessful()) {
            return Result.error("Cannot find user role " + request.username());
        }
        String role = roleResult.getData();

        if(!comment.getUser().getId().equals(user.getId()) && !role.equals("admin")){
            return Result.error("Not allowed to delete comment");
        }

        comment.disable();
        Result<Post> updatePostResult = this.postRepository.updatePost(post);
        if (!updatePostResult.isSuccessful()) {
            return Result.error("Cannot update post ");
        }
        return Result.success(comment);
    }
}
