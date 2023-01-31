package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.annotations.InputPortRequest;
import de.hsos.swa.application.service.query.params.OrderParams;
import de.hsos.swa.application.service.query.params.PostFilterParams;
import de.hsos.swa.application.service.query.params.SortingParams;

import java.util.Map;
// Todo Validierung!
@InputPortRequest
public record GetFilteredPostQuery(
        Map<PostFilterParams, Object> filterParams,
        Boolean includeComments,
        SortingParams sortingParams,
        OrderParams orderParams) {



    //Eventuell overloaded Constructor ohne Sorting und Order
    public GetFilteredPostQuery(
            Map<PostFilterParams, Object> filterParams,
            Boolean includeComments,
            SortingParams sortingParams,
            OrderParams orderParams) {
        this.filterParams = filterParams;
        this.includeComments = includeComments;
        this.sortingParams = sortingParams;
        this.orderParams = orderParams;
        if (!this.validateMapTest()) {
            // TODO: Custom Validator
            throw new RuntimeException("InvalidFilterParams");
        }
    }

    // TODO: Custom Validator
    public boolean validateMapTest() {
        for (Map.Entry<PostFilterParams, Object> filterParam : this.filterParams.entrySet()) {
            if (!filterParam.getValue().getClass().equals(filterParam.getKey().getParamType())) {
                return false;
            }
        }
        return true;
    }
}
