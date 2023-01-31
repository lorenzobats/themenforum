package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.annotations.InputPortRequest;
import de.hsos.swa.domain.validation.constraints.ValidId;


@InputPortRequest
public record GetTopicByIdQuery(
        @ValidId(message = "Invalid 'topicId'")
        String topicId
) {}
