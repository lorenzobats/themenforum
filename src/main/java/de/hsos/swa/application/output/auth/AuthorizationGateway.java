package de.hsos.swa.application.output.auth;

import de.hsos.swa.application.annotations.OutputPort;
import de.hsos.swa.application.output.auth.dto.out.SaveAuthUserCommand;
import de.hsos.swa.application.output.auth.dto.in.AuthorizationResult;

import java.util.UUID;

@OutputPort
public interface AuthorizationGateway {
    AuthorizationResult<Void> registerUser(SaveAuthUserCommand outputPortRequest);

    AuthorizationResult<Void> addOwnership(String owningUser, UUID ressourceId);

    public AuthorizationResult<Void> removeOwnership(String owningUser, UUID ressourceId);

    AuthorizationResult<Boolean> canAccessUsers(String accessingUser);
    AuthorizationResult<Boolean> canAccessVotes(String accessingUser);
    AuthorizationResult<Boolean> canAccessVotesBy(String accessingUser, String votesOwner);

    AuthorizationResult<Boolean> canDeleteComment(String accessingUser, UUID CommentId);

    AuthorizationResult<Boolean> canDeletePost(String accessingUser, UUID PostId);

    AuthorizationResult<Boolean> canDeleteUser(String accessingUser, UUID UserId);

    AuthorizationResult<Boolean> canDeleteVote(String accessingUser, UUID VoteId);
    AuthorizationResult<Boolean> canDeleteTopic(String accessingUser, UUID TopicId);

    //------------------------------------------------------------------------------------------------------------------
    // UPDATE PERMISSION
    AuthorizationResult<Boolean> canUpdatePost(String accessingUser, UUID commentId);
}
