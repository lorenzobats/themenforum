package de.hsos.swa.actors.rest.dto.in;

import de.hsos.swa.application.input.dto.in.CommentPostInputPortRequest;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "CommentCreationDTO")
public class CommentPostRestAdapterRequest {

    public String postId;

    public String text;

    public static class Converter {
        public static CommentPostInputPortRequest toInputPortCommand(CommentPostRestAdapterRequest adapterRequest, String username) {
            return new CommentPostInputPortRequest(adapterRequest.postId, username, adapterRequest.text);
        }
    }
}
