package de.hsos.swa.actors.rest.dto.in;

import de.hsos.swa.actors.rest.dto.in.validation.ValidatedRequestBody;
import de.hsos.swa.application.input.dto.in.CommentPostCommand;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.NotEmpty;

@Schema(name = "CommentCreationDto")
public class CommentPostRequestBody implements ValidatedRequestBody {
    @NotEmpty
    public String postId;
    @NotEmpty
    public String text;

    public static class Converter {
        public static CommentPostCommand toInputPortCommand(CommentPostRequestBody adapterRequest, String username) {
            return new CommentPostCommand(adapterRequest.postId, username, adapterRequest.text);
        }
    }
}
