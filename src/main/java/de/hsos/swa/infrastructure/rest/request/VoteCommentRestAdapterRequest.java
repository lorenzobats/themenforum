package de.hsos.swa.infrastructure.rest.request;

import de.hsos.swa.application.input.request.VoteCommentInputPortRequest;
import de.hsos.swa.application.input.request.VotePostInputPortRequest;
import de.hsos.swa.domain.vo.VoteType;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;


public class VoteCommentRestAdapterRequest {

    @NotEmpty (message = "voteType missing")
    @Pattern(regexp = "up|down|none", message = "voteType must be one of: up, down, none")
    public String voteType;
    // https://developer.salesforce.com/docs/atlas.en-us.chatterapi.meta/chatterapi/connect_resources_comments_capability_up_down_vote.htm?q=downVote

    public VoteCommentRestAdapterRequest() {}

    public static class Converter {
        public static VoteCommentInputPortRequest toInputPort(VoteCommentRestAdapterRequest adapterRequest, String commentId, String username) {
            return new VoteCommentInputPortRequest(commentId, username, VoteType.fromValue(adapterRequest.voteType));
        }
    }
}
