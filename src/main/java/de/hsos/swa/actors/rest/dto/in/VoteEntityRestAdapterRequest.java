package de.hsos.swa.actors.rest.dto.in;

import de.hsos.swa.application.input.dto.in.VoteEntityInputPortRequest;
import de.hsos.swa.domain.vo.VoteType;
import de.hsos.swa.domain.vo.VotedEntityType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.NotNull;


@Schema(name = "VoteCreationDTO")
public class VoteEntityRestAdapterRequest {

    @NotNull(message = "voteType is empty")
    public VoteType voteType;

    @NotNull(message = "entityId is empty")
    public String entityId;

    @NotNull(message = "entityType is empty")
    public VotedEntityType entityType;

    public VoteEntityRestAdapterRequest() {}

    public static class Converter {
        public static VoteEntityInputPortRequest toInputPortCommand(VoteEntityRestAdapterRequest request, String username) {
            return new VoteEntityInputPortRequest(request.entityId, username, request.voteType, request.entityType);
        }
    }
}
