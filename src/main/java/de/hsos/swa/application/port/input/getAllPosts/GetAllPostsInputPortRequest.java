package de.hsos.swa.application.port.input.getAllPosts;

import de.hsos.swa.application.port.input._shared.SelfValidating;

import javax.validation.constraints.Pattern;


public class GetAllPostsInputPortRequest extends SelfValidating<GetAllPostsInputPortRequest> {


    private final boolean includeComments;

    public GetAllPostsInputPortRequest(boolean includeComments) {
        this.includeComments = includeComments;
        this.validateSelf();
    }

    public boolean includeComments() {
        return includeComments;
    }
}
