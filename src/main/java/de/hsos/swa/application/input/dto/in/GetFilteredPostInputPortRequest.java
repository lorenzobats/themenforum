package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.use_case_query.PostFilterParams;
import de.hsos.swa.application.use_case_query.SortingParams;

import java.util.Map;
// Todo Validierung!
public record GetFilteredPostInputPortRequest(
        Map<PostFilterParams, Object> filterParams,
        Boolean includeComments,
        SortingParams sortingParams) {

    public GetFilteredPostInputPortRequest(Map<PostFilterParams, Object> filterParams, Boolean includeComments, SortingParams sortingParams) {
        this.filterParams = filterParams;
        this.includeComments = includeComments;
        this.sortingParams = sortingParams;
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
