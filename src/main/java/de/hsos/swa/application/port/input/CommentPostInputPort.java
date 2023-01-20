package de.hsos.swa.application.port.input;

import de.hsos.swa.application.port.input.request.CommentPostInputPortRequest;

import javax.validation.Valid;
import java.util.UUID;

public interface CommentPostInputPort {
   Result<UUID> commentPost(@Valid CommentPostInputPortRequest request);
}
