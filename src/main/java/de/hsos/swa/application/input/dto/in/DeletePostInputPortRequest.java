package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.input.validation.constraints.ValidId;
import de.hsos.swa.application.input.validation.constraints.ValidInputPortRequest;
import de.hsos.swa.application.input.validation.constraints.ValidUsername;
@ValidInputPortRequest
public record DeletePostInputPortRequest(
        @ValidId String postId,
        @ValidUsername String username
) {}
