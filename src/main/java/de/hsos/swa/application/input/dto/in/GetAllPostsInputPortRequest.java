package de.hsos.swa.application.input.dto.in;

import de.hsos.swa.application.PostFilterParams;

import java.util.Map;


public class GetAllPostsInputPortRequest {

    private final Map<PostFilterParams, Object> filterParams;

    private final Boolean includeComments;

    public GetAllPostsInputPortRequest(Map<PostFilterParams, Object> filterParams, Boolean includeComments) {
        this.filterParams = filterParams;
        this.includeComments = includeComments;
        if(!this.validateMapTest()) {
            // TODO: Custom Validator schreiben
            throw new RuntimeException("InValidFilterParams");
        }
    }

    public Map<PostFilterParams, Object> getFilterParams() {
        return filterParams;
    }

    public Boolean getIncludeComments() {
        return includeComments;
    }

    // TODO: Custom Validator schreiben
    public boolean validateMapTest(){
        for (Map.Entry<PostFilterParams, Object> filterParam : this.filterParams.entrySet()) {
            if (!filterParam.getValue().getClass().equals(filterParam.getKey().getParamType())) {
                return false;
            }
        }
        return true;
    }
}
