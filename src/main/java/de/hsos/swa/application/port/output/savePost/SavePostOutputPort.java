package de.hsos.swa.application.port.output.savePost;

import de.hsos.swa.application.port.input._shared.Result;

public interface SavePostOutputPort {
   Result<SavePostOutputPortResponse> savePost(SavePostOutputPortRequest inputPortRequest);
}
