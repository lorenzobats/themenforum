package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.annotations.InputPortRequest;
import de.hsos.swa.application.input.validation.constraints.ValidEnumValue;
import de.hsos.swa.application.input.validation.constraints.ValidId;
import de.hsos.swa.application.service.query.params.OrderParams;
import de.hsos.swa.application.service.query.params.SortingParams;

import javax.validation.constraints.NotNull;

/**
 * Der Record GetPostByIdQuery definiert das validierte Request-DTO für den Input Port GetPostByIdUseCase.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see de.hsos.swa.application.input.query.GetPostByIdUseCase         Nutzt dieses Request-DTO
 */
@InputPortRequest
public record GetPostByIdQuery(
        @ValidId String id,
        @NotNull boolean includeComments,
        @ValidEnumValue(enumClass = SortingParams.class) String sortingParams,
        @ValidEnumValue(enumClass = OrderParams.class) String orderParams) {
}
