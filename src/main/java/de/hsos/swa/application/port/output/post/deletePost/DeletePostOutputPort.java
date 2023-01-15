package de.hsos.swa.application.port.output.post.deletePost;

import de.hsos.swa.application.port.input._shared.Result;
import de.hsos.swa.application.port.output.post.savePost.SavePostOutputPortRequest;
import de.hsos.swa.application.port.output.post.savePost.SavePostOutputPortResponse;

public interface DeletePostOutputPort {
   Result<SavePostOutputPortResponse> savePost(SavePostOutputPortRequest inputPortRequest);
}
