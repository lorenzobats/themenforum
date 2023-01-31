package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.annotations.InputPortRequest;
import de.hsos.swa.domain.validation.constraints.ValidUsername;

import javax.validation.Valid;
import java.util.UUID;

@InputPortRequest
public record CreatePostCommand(
        // TODO: Validation PostTitle (Length, Trailing Spaces)
        @Valid
        String title,

        // TODO: Validation Content (Length, Trailing Spaces)
        String content,
        // TODO: String und @ValidId
        @Valid UUID topicId,
        @ValidUsername String username
) {}
