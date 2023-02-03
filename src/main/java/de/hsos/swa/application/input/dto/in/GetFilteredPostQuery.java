package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.annotations.InputPortRequest;
import de.hsos.swa.application.input.validation.constraints.ValidOrderParams;
import de.hsos.swa.application.input.validation.constraints.ValidPostFilterParams;
import de.hsos.swa.application.input.validation.constraints.ValidSortingParams;
import de.hsos.swa.application.service.query.params.OrderParams;
import de.hsos.swa.application.service.query.params.PostFilterParams;
import de.hsos.swa.application.service.query.params.SortingParams;

import javax.validation.constraints.NotNull;
import java.util.Map;

@InputPortRequest
public record GetFilteredPostQuery(
        @ValidPostFilterParams Map<PostFilterParams, Object> filterParams,
        @NotNull Boolean includeComments,
        @ValidSortingParams SortingParams sortingParams,
        @ValidOrderParams OrderParams orderParams) {


    public GetFilteredPostQuery(
            Map<PostFilterParams, Object> filterParams,
            Boolean includeComments,
            SortingParams sortingParams,
            OrderParams orderParams) {
        this.filterParams = filterParams;
        this.includeComments = includeComments;
        this.sortingParams = sortingParams;
        this.orderParams = orderParams;
    }
}
