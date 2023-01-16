package de.hsos.swa.application.port.output.post;

import de.hsos.swa.application.port.input._shared.Result;
import de.hsos.swa.domain.entity.Post;

import java.util.UUID;

public interface UpdatePostOutputPort {
   Result<Void> updatePost(Post post);
}
