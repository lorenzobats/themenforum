package de.hsos.swa.application.port.input.getAllPosts;

import de.hsos.swa.application.PostFilterParams;
import de.hsos.swa.application.port.input._shared.SelfValidating;

import java.util.Map;


public class GetAllPostsInputPortRequest extends SelfValidating<GetAllPostsInputPortRequest> {

    private final Map<PostFilterParams, Object> filterParams;

    public GetAllPostsInputPortRequest(Map<PostFilterParams, Object> filterParams) {
        this.filterParams = filterParams;
        if(!this.validateMapTest()) {
            // TODO: Custom Validator schreiben
            throw new RuntimeException("InValidFilterParams");
        }
        this.validateSelf();
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
