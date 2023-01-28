package de.hsos.swa.infrastructure.rest.dto.in;

import de.hsos.swa.application.input.dto.in.VotePostInputPortRequest;
import de.hsos.swa.domain.entity.VoteType;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class VotePostRestAdapterRequest {

    @NotNull(message = "voteType is empty")
    public VoteType voteType;

    public VotePostRestAdapterRequest() {}

    public static class Converter {
        public static VotePostInputPortRequest toInputPortCommand(VotePostRestAdapterRequest adapterRequest, String postId, String username) {
            return new VotePostInputPortRequest(postId, username, adapterRequest.voteType);
        }
    }
}
