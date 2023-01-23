package de.hsos.swa.application.input;

import de.hsos.swa.application.input.dto.in.DeletePostInputPortRequest;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Post;

import javax.validation.Valid;

public interface DeletePostInputPort {
    Result<Post> deletePost(@Valid DeletePostInputPortRequest request);
}
