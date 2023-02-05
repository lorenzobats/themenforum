package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.annotations.InputPortRequest;
import javax.validation.constraints.NotBlank;

@InputPortRequest
public record SearchTopicsQuery(
        @NotBlank String searchString
) {
}
