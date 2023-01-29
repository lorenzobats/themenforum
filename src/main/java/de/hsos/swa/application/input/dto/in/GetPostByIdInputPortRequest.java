package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.input.validation.constraints.ValidId;
import de.hsos.swa.application.input.validation.constraints.ValidInputPortRequest;
import de.hsos.swa.application.use_case_query.OrderParams;
import de.hsos.swa.application.use_case_query.SortingParams;

@ValidInputPortRequest
public record GetPostByIdInputPortRequest(
        @ValidId
        String id,
        boolean includeComments,
        SortingParams sortingParams,
        OrderParams orderParams
) {
    public GetPostByIdInputPortRequest(String id, boolean includeComments) {
        this(id, includeComments, SortingParams.VOTES, OrderParams.DESC);
    }

    public GetPostByIdInputPortRequest(String id, boolean includeComments, SortingParams sortingParams, OrderParams orderParams) {
        this.id = id;
        this.includeComments = includeComments;
        this.sortingParams = sortingParams;
        this.orderParams = orderParams;
    }
}
