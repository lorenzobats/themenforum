package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.annotations.InputPortRequest;
import de.hsos.swa.domain.validation.constraints.ValidId;
import de.hsos.swa.application.service.query.params.OrderParams;
import de.hsos.swa.application.service.query.params.SortingParams;

@InputPortRequest
public record GetPostByIdQuery(
        @ValidId
        String id,
        boolean includeComments,
        SortingParams sortingParams,
        OrderParams orderParams
) {
    public GetPostByIdQuery(String id, boolean includeComments, SortingParams sortingParams, OrderParams orderParams) {
        this.id = id;
        this.includeComments = includeComments;
        this.sortingParams = sortingParams;
        this.orderParams = orderParams;
    }
}
