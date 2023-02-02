package de.hsos.swa.application.input.dto.in;
import de.hsos.swa.application.annotations.InputPortRequest;
import de.hsos.swa.application.input.validation.constraints.ValidId;

@InputPortRequest
public record GetCommentByIdQuery(
        @ValidId String commentId
) {}