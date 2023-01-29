package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.input.validation.constraints.ValidId;
import de.hsos.swa.application.input.validation.constraints.ValidInputPortRequest;


@ValidInputPortRequest
public record GetTopicByIdInputPortRequest(
        @ValidId(message = "Invalid 'topicId'")
        String topicId
) {}
