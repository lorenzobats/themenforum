package de.hsos.swa.application.input.dto.in;
import de.hsos.swa.application.input.validation.constraints.ValidId;
import de.hsos.swa.application.input.validation.constraints.ValidInputPortRequest;

@ValidInputPortRequest
public record GetCommentByIdInputPortRequest(
        @ValidId
        String id
) {}