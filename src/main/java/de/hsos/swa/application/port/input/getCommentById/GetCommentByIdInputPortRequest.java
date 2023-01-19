package de.hsos.swa.application.port.input.getCommentById;

import de.hsos.swa.application.port.input._shared.SelfValidating;

import javax.validation.constraints.Pattern;


public class GetCommentByIdInputPortRequest extends SelfValidating<GetCommentByIdInputPortRequest> {

    @Pattern(regexp = "[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}", message = "UUID is not valid.")
    private final String id;

    public GetCommentByIdInputPortRequest(String id) {
        this.id = id;
        this.validateSelf();
    }

    public String getId() {
        return id;
    }
}