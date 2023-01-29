package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.input.validation.constraints.ValidInputPortRequest;
import de.hsos.swa.application.input.validation.constraints.ValidUsername;

import javax.validation.Valid;
import java.util.UUID;

@ValidInputPortRequest
public record CreatePostInputPortRequest(
        // TODO: Validation PostTitle (Length, Trailing Spaces)
        @Valid
        String title,

        // TODO: Validation Content (Length, Trailing Spaces)
        String content,
        // TODO: String und @ValidId
        @Valid UUID topicId,
        @ValidUsername String username
) {}
