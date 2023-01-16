package de.hsos.swa.application.port.output.post.saveComment;

import de.hsos.swa.application.port.input._shared.Result;

public interface SaveCommentOutputPort {
   Result<SaveCommentOutputPortResponse> saveComment(SaveCommentOutputPortRequest inputPortRequest);
}
