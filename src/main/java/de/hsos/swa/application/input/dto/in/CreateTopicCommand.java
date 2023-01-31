package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.annotations.InputPortRequest;
import de.hsos.swa.domain.validation.constraints.ValidUsername;

@InputPortRequest
public record CreateTopicCommand(
        // TODO: Validation TopicTitle (Length, Trailing Spaces, Uinqueness!!!)
        String title,
        // TODO: Validation TopicDescription (Length, Trailing Spaces)
        String description,
        @ValidUsername String username
) {}