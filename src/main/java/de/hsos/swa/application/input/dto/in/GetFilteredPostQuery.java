package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.annotations.InputPortRequest;
import de.hsos.swa.application.input.validation.constraints.ValidEnumValue;
import de.hsos.swa.application.input.validation.constraints.ValidPostFilterParams;
import de.hsos.swa.application.service.query.params.OrderParams;
import de.hsos.swa.application.service.query.params.PostFilterParams;
import de.hsos.swa.application.service.query.params.SortingParams;

import javax.validation.constraints.NotNull;
import java.util.Map;

@InputPortRequest
public record GetFilteredPostQuery(
        @ValidPostFilterParams
        Map<PostFilterParams, Object> filterParams,
        @NotNull
        Boolean includeComments,
        @ValidEnumValue(enumClass = SortingParams.class)
        String sortingParams,
        @ValidEnumValue(enumClass = OrderParams.class)
        String orderParams
) {}
