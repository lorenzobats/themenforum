package de.hsos.swa.application.output.auth;

import de.hsos.swa.application.annotations.OutputPort;
import de.hsos.swa.application.output.auth.dto.out.SaveAuthUserCommand;
import de.hsos.swa.application.output.auth.dto.in.AuthorizationResult;

import java.util.UUID;

@OutputPort
public interface AuthorizationGateway {
    AuthorizationResult<Void> registerUser(SaveAuthUserCommand outputPortRequest);

    AuthorizationResult<Boolean> canAccessUsers(String username);
    AuthorizationResult<Boolean> canAccessVotes(String username);
    AuthorizationResult<Boolean> canAccessVotesBy(String username, String voteOwner);

    AuthorizationResult<Boolean> canDeleteComment(String requestingUser, UUID CommentId);

    AuthorizationResult<Boolean> canDeletePost(String requestingUser, UUID PostId);

    AuthorizationResult<Boolean> canDeleteUser(String requestingUser, UUID UserId);

    AuthorizationResult<Boolean> canDeleteVote(String requestingUser, UUID VoteId);
    AuthorizationResult<Boolean> canDeleteTopic(String requestingUser, UUID TopicId);
}
