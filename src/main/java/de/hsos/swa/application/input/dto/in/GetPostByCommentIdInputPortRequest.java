package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.input.validation.constraints.ValidId;
import de.hsos.swa.application.input.validation.constraints.ValidInputPortRequest;

import javax.validation.constraints.Pattern;

@ValidInputPortRequest
public record GetPostByCommentIdInputPortRequest(
        @ValidId
        String id
) {}
