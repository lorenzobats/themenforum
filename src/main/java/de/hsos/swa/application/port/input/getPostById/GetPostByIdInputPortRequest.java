package de.hsos.swa.application.port.input.getPostById;

import de.hsos.swa.application.port.input._shared.SelfValidating;
import javax.validation.constraints.Pattern;


public class GetPostByIdInputPortRequest extends SelfValidating<GetPostByIdInputPortRequest> {

    @Pattern(regexp = "[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}", message = "UUID is not valid.")
    private final String id;

    private final boolean includeComments;

    public GetPostByIdInputPortRequest(String id, boolean includeComments) {
        this.id = id;
        this.includeComments = includeComments;
        this.validateSelf();
    }

    public String getId() {
        return id;
    }
    public boolean includeComments() {
        return includeComments;
    }
}
