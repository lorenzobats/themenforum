package de.hsos.swa.infrastructure.rest.dto.in;

import de.hsos.swa.application.input.dto.in.VotePostInputPortRequest;
import de.hsos.swa.domain.value_object.VoteType;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class VotePostRestAdapterRequest {

    // TODO: Converter schreiben
    @NotEmpty (message = "voteType is empty")
    @Pattern(regexp = "up|down", message = "voteType must be one of: up, down")
    public String voteType;
    public VotePostRestAdapterRequest() {}

    public static class Converter {
        public static VotePostInputPortRequest toInputPortCommand(VotePostRestAdapterRequest adapterRequest, String postId, String username) {
            return new VotePostInputPortRequest(postId, username, VoteType.fromValue(adapterRequest.voteType));
        }
    }
}
