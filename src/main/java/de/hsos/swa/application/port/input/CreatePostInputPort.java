package de.hsos.swa.application.port.input;

import de.hsos.swa.application.port.input.request.CreatePostInputPortRequest;

import javax.validation.Valid;
import java.util.UUID;

public interface CreatePostInputPort {
   Result<UUID> createPost(@Valid CreatePostInputPortRequest request);
}
