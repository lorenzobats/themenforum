package de.hsos.swa.infrastructure.rest.dto.in;

import de.hsos.swa.application.input.dto.in.VoteCommentInputPortRequest;
import de.hsos.swa.domain.entity.VoteType;

import javax.validation.constraints.NotNull;


public class VoteCommentRestAdapterRequest {

    @NotNull(message = "voteType is empty")
    public VoteType voteType;
    public VoteCommentRestAdapterRequest() {}

    public static class Converter {
        public static VoteCommentInputPortRequest toInputPortCommand(VoteCommentRestAdapterRequest adapterRequest, String commentId, String username) {
            return new VoteCommentInputPortRequest(commentId, username, adapterRequest.voteType);
        }
    }
}
