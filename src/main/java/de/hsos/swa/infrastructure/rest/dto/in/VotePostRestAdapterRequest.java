package de.hsos.swa.infrastructure.rest.dto.in;

import de.hsos.swa.application.input.dto.in.VotePostInputPortRequest;
import de.hsos.swa.domain.vo.VoteType;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;


public class VotePostRestAdapterRequest {

    @NotEmpty (message = "voteType missing")
    @Pattern(regexp = "up|down|none", message = "voteType must be one of: up, down, none")
    public String voteType;
    // https://developer.salesforce.com/docs/atlas.en-us.chatterapi.meta/chatterapi/connect_resources_comments_capability_up_down_vote.htm?q=downVote

    public VotePostRestAdapterRequest() {}

    public static class Converter {
        public static VotePostInputPortRequest toInputPortCommand(VotePostRestAdapterRequest adapterRequest, String postId, String username) {
            return new VotePostInputPortRequest(postId, username, VoteType.fromValue(adapterRequest.voteType));
        }
    }
}
