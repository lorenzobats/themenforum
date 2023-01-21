package de.hsos.swa.application.input.request;

import javax.validation.constraints.Pattern;


public class GetTopicByIdInputPortRequest {

    @Pattern(regexp = "[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}", message = "Topic ID is not valid.")
    private final String id;


    public GetTopicByIdInputPortRequest(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
