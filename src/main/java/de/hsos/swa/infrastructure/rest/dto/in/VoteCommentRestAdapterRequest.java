package de.hsos.swa.infrastructure.rest.dto.in;

import de.hsos.swa.application.input.dto.in.VoteCommentInputPortRequest;
import de.hsos.swa.domain.value_object.VoteType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;


public class VoteCommentRestAdapterRequest {


    @NotEmpty (message = "voteType is empty")
    @Pattern(regexp = "up|down|none", message = "voteType must be one of: up, down, none")
    public String voteType;
    public VoteCommentRestAdapterRequest() {}

    public static class Converter {
        public static VoteCommentInputPortRequest toInputPortCommand(VoteCommentRestAdapterRequest adapterRequest, String commentId, String username) {
            return new VoteCommentInputPortRequest(commentId, username, VoteType.fromValue(adapterRequest.voteType));
        }
    }
}
