package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.input.validation.constraints.ValidInputPortRequest;
import de.hsos.swa.application.input.validation.constraints.ValidUsername;

@ValidInputPortRequest
public record CreateTopicInputPortRequest(
        // TODO: Validation TopicTitle (Length, Trailing Spaces)
        String title,
        // TODO: Validation TopicDescription (Length, Trailing Spaces)
        String description,
        @ValidUsername String username
) {}