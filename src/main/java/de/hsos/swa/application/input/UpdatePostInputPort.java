package de.hsos.swa.application.input;

import de.hsos.swa.application.input.dto.in.UpdatePostInputPortRequest;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Post;

import javax.validation.Valid;

public interface UpdatePostInputPort {
   Result<Post> updatePost(@Valid UpdatePostInputPortRequest request);
}
