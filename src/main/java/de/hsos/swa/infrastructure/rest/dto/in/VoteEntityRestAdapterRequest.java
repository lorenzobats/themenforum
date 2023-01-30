package de.hsos.swa.infrastructure.rest.dto.in;

import de.hsos.swa.application.input.dto.in.VoteEntityInputPortRequest;
import de.hsos.swa.domain.entity.VoteType;

import javax.validation.constraints.NotNull;


public class VoteEntityRestAdapterRequest {

    @NotNull(message = "voteType is empty")
    public VoteType voteType;

    @NotNull(message = "commentId is empty")
    public String id;

    @NotNull(message = "entityType is empty")
    public String entityType;

    public VoteEntityRestAdapterRequest() {}

    public static class Converter {
        public static VoteEntityInputPortRequest toInputPortCommand(VoteEntityRestAdapterRequest request, String username) {
            return new VoteEntityInputPortRequest(request.id, username, request.voteType, request.entityType);
        }
    }
}
