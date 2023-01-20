package de.hsos.swa.application.input.request;

import de.hsos.swa.application.PostFilterParams;

import java.util.Map;


public class GetAllPostsInputPortRequest {

    private final Map<PostFilterParams, Object> filterParams;

    public GetAllPostsInputPortRequest(Map<PostFilterParams, Object> filterParams) {
        this.filterParams = filterParams;
        if(!this.validateMapTest()) {
            // TODO: Custom Validator schreiben
            throw new RuntimeException("InValidFilterParams");
        }
    }

    public Map<PostFilterParams, Object> getFilterParams() {
        return filterParams;
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
