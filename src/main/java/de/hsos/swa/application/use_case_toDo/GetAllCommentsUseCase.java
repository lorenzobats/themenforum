package de.hsos.swa.application.use_case_toDo;

import de.hsos.swa.application.input.GetAllCommentsInputPort;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Comment;

import java.util.List;

public class GetAllCommentsUseCase implements GetAllCommentsInputPort {
    @Override
    public Result<List<Comment>> getAllComments(boolean includeComments) {
        return null;
    }
}
