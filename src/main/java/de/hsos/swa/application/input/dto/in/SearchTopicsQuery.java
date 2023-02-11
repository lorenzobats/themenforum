package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.annotations.InputPortRequest;
import javax.validation.constraints.NotBlank;

/**
 * Der Record SearchTopicsQuery definiert das validierte Request-DTO für den Input Port SearchTopicsUseCase.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see de.hsos.swa.application.input.query.SearchTopicsUseCase         Nutzt dieses Request-DTO
 */
@InputPortRequest
public record SearchTopicsQuery(
        @NotBlank String searchString
) {
}
