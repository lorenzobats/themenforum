package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.use_case_query.PostFilterParams;

import java.util.Map;

public record GetFilteredPostInputPortRequest(
        Map<PostFilterParams, Object> filterParams,
        Boolean includeComments) {

    public GetFilteredPostInputPortRequest(Map<PostFilterParams, Object> filterParams, Boolean includeComments) {
        this.filterParams = filterParams;
        this.includeComments = includeComments;
        if (!this.validateMapTest()) {
            // TODO: Custom Validator schreiben
            throw new RuntimeException("InValidFilterParams");
        }
    }

    // TODO: Custom Validator schreiben
    public boolean validateMapTest() {
        for (Map.Entry<PostFilterParams, Object> filterParam : this.filterParams.entrySet()) {
            if (!filterParam.getValue().getClass().equals(filterParam.getKey().getParamType())) {
                return false;
            }
        }
        return true;
    }
}
