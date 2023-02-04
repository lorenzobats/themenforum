package de.hsos.swa.actors.rest.dto.in;

import de.hsos.swa.actors.rest.dto.in.validation.ValidatedRequestBody;
import de.hsos.swa.application.input.dto.in.CommentPostCommand;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.NotBlank;

@Schema(name = "CommentCreationDto", required = true)
public class CommentPostRequestBody implements ValidatedRequestBody {
    @NotBlank(message = "postId is blank")
    public String postId;

    @NotBlank(message = "text is blank")
    public String text;

    public static class Converter {
        public static CommentPostCommand toInputPortCommand(CommentPostRequestBody adapterRequest) {
            return new CommentPostCommand(adapterRequest.postId, adapterRequest.text);
        }
    }
}
