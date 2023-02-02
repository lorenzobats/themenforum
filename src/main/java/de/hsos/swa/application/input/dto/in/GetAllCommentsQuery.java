package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.annotations.InputPortRequest;
import javax.validation.constraints.NotNull;

@InputPortRequest
public record GetAllCommentsQuery(
        @NotNull boolean includeReplies
) {}
