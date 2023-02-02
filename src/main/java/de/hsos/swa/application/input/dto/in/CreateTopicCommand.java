package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.annotations.InputPortRequest;
import de.hsos.swa.application.input.validation.constraints.ValidTopicDescription;
import de.hsos.swa.application.input.validation.constraints.ValidTopicTitle;
import de.hsos.swa.application.input.validation.constraints.ValidUsername;

@InputPortRequest
public record CreateTopicCommand(
        @ValidTopicTitle String title,
        @ValidTopicDescription String description,
        @ValidUsername String username
) {}