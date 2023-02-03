package de.hsos.swa.actors.rest.dto.in;

import de.hsos.swa.actors.rest.dto.in.validation.ValidatedRequestBody;
import de.hsos.swa.application.input.dto.in.ReplyToCommentCommand;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.NotBlank;

@Schema(name = "CommentReplyDto")
public class ReplyToCommentRequestBody implements ValidatedRequestBody {

    @NotBlank(message = "text is blank")
    public String text;

    public ReplyToCommentRequestBody() {}

    public static class Converter {
        public static ReplyToCommentCommand toInputPortCommand(ReplyToCommentRequestBody adapterRequest, String commentId, String username) {
            return new ReplyToCommentCommand(commentId, username, adapterRequest.text);
        }
    }
}
