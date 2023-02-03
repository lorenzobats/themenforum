package de.hsos.swa.application.output.auth;

import de.hsos.swa.application.annotations.OutputPort;
import de.hsos.swa.application.output.auth.dto.out.SaveAuthUserCommand;
import de.hsos.swa.application.output.auth.dto.in.AuthorizationResult;

import java.util.UUID;

@OutputPort
public interface AuthorizationGateway {
    AuthorizationResult<Void> registerUser(SaveAuthUserCommand outputPortRequest);
    AuthorizationResult<String> getUserAuthRole(UUID userId);



    AuthorizationResult<Boolean> canAccessUsers(String username);
    AuthorizationResult<Boolean> canAccessVotes(String username);
    AuthorizationResult<Boolean> canAccessVotesBy(String username, String voteOwner);
}
