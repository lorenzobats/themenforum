package de.hsos.swa.application.input;

import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Comment;
import java.util.List;

public interface GetAllCommentsInputPort {
    Result<List<Comment>> getAllComments(boolean includeComments);
}
