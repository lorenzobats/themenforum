package de.hsos.swa.application.output.auth;

import de.hsos.swa.application.annotations.OutputPort;
import de.hsos.swa.application.output.auth.dto.out.RegisterAuthUserCommand;
import de.hsos.swa.application.output.auth.dto.in.AuthorizationResult;

import java.util.UUID;

@OutputPort
public interface AuthorizationGateway {
    AuthorizationResult<Void> registerUser(RegisterAuthUserCommand command);

    AuthorizationResult<Void> deleteUser(String name);

    AuthorizationResult<Void> addOwnership(String owningUser, UUID ressourceId);

    AuthorizationResult<Void> removeOwnership(String owningUser, UUID ressourceId);

    //------------------------------------------------------------------------------------------------------------------
    // READ PERMISSION
    AuthorizationResult<Boolean> canAccessUsers(String accessingUser);
    AuthorizationResult<Boolean> canAccessVotes(String accessingUser);
    AuthorizationResult<Boolean> canAccessVotesBy(String accessingUser, String votesOwner);

    //------------------------------------------------------------------------------------------------------------------
    // DELETE PERMISSION
    AuthorizationResult<Boolean> canDeleteUser(String accessingUser, String username);
    AuthorizationResult<Boolean> canDeleteComment(String accessingUser, UUID CommentId);
    AuthorizationResult<Boolean> canDeletePost(String accessingUser, UUID PostId);
    AuthorizationResult<Boolean> canDeleteVote(String accessingUser, UUID VoteId);
    AuthorizationResult<Boolean> canDeleteTopic(String accessingUser, UUID TopicId);

    //------------------------------------------------------------------------------------------------------------------
    // UPDATE PERMISSION
    AuthorizationResult<Boolean> canUpdatePost(String accessingUser, UUID commentId);
}
