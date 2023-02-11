package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.annotations.InputPortRequest;
import de.hsos.swa.application.input.validation.constraints.ValidEnumValue;
import de.hsos.swa.application.input.validation.constraints.ValidPostFilterParams;
import de.hsos.swa.application.service.query.params.OrderParams;
import de.hsos.swa.application.service.query.params.PostFilterParams;
import de.hsos.swa.application.service.query.params.SortingParams;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * Der Record GetFilteredPostsQuery definiert das validierte Request-DTO für den Input Port GetFilteredPostsUseCase.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see de.hsos.swa.application.input.query.GetFilteredPostsUseCase         Nutzt dieses Request-DTO
 */
@InputPortRequest
public record GetFilteredPostsQuery(
        @ValidPostFilterParams
        Map<PostFilterParams, Object> filterParams,
        @NotNull
        Boolean includeComments,
        @ValidEnumValue(enumClass = SortingParams.class)
        String sortingParams,
        @ValidEnumValue(enumClass = OrderParams.class)
        String orderParams
) {}
