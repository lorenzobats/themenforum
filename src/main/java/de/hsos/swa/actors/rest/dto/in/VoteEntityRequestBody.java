package de.hsos.swa.actors.rest.dto.in;

import de.hsos.swa.actors.rest.dto.in.validation.ValidatedRequestBody;
import de.hsos.swa.application.input.dto.in.VoteEntityCommand;
import de.hsos.swa.domain.vo.VoteType;
import de.hsos.swa.domain.vo.VotedEntityType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Schema(name = "VoteCreationDto")
public class VoteEntityRequestBody implements ValidatedRequestBody {

    @NotBlank(message = "voteType is empty")
    public String voteType;

    @NotBlank(message = "entityId is blank")
    public String entityId;

    @NotBlank(message = "entityType is empty")
    public String entityType;

    public VoteEntityRequestBody() {}

    public static class Converter {
        public static VoteEntityCommand toInputPortCommand(VoteEntityRequestBody request) {
            return new VoteEntityCommand(request.entityId, request.voteType, request.entityType);
        }
    }
}
