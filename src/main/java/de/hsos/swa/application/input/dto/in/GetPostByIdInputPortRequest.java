package de.hsos.swa.application.input.dto.in;

import javax.validation.constraints.Pattern;


public record GetPostByIdInputPortRequest(
        @Pattern(regexp = "[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}", message = "UUID is not valid.")
        String id,
        boolean includeComments
) {}
