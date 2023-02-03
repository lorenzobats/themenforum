package de.hsos.swa.actors.rest.dto.in;

import de.hsos.swa.actors.rest.dto.in.validation.ValidatedRequestBody;
import de.hsos.swa.application.input.dto.in.UpdatePostCommand;
import io.smallrye.common.constraint.Nullable;
import org.eclipse.microprofile.openapi.annotations.media.Schema;


@Schema(name = "UpdatePostDto", required = true)
public class UpdatePostRequestBody implements ValidatedRequestBody {

    @Nullable
    public String title;

    @Nullable
    public String content;

    public static class Converter {
        public static UpdatePostCommand toInputPortCommand(UpdatePostRequestBody adapterRequest, String postId) {
            return new UpdatePostCommand(postId, adapterRequest.title, adapterRequest.content);
        }
    }
}
