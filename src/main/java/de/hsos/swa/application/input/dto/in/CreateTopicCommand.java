package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.annotations.InputPortRequest;
import de.hsos.swa.application.input.validation.constraints.ValidTopicDescription;
import de.hsos.swa.application.input.validation.constraints.ValidTopicTitle;

@InputPortRequest
public record CreateTopicCommand(
        @ValidTopicTitle String title,
        @ValidTopicDescription String description
) {}