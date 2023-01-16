package de.hsos.swa.application.port.output.post;

import de.hsos.swa.application.port.input._shared.Result;

public interface DeletePostOutputPort {
   Result<Void> deletePost(String postId);
}
