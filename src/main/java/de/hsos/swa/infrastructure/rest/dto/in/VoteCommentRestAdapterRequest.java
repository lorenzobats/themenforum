package de.hsos.swa.infrastructure.rest.dto.in;

import de.hsos.swa.application.input.dto.in.VoteCommentInputPortRequest;
import de.hsos.swa.domain.vo.VoteType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;


public class VoteCommentRestAdapterRequest {

    @NotBlank(message = "postId missing")
    public String postId;
    @NotEmpty (message = "voteType missing")
    @Pattern(regexp = "up|down|none", message = "voteType must be one of: up, down, none")
    public String voteType;
    // https://developer.salesforce.com/docs/atlas.en-us.chatterapi.meta/chatterapi/connect_resources_comments_capability_up_down_vote.htm?q=downVote
    public VoteCommentRestAdapterRequest() {}

    public static class Converter {
        public static VoteCommentInputPortRequest toInputPortCommand(VoteCommentRestAdapterRequest adapterRequest, String commentId, String username) {
            return new VoteCommentInputPortRequest(commentId, adapterRequest.postId, username, VoteType.fromValue(adapterRequest.voteType));
        }
    }
}
