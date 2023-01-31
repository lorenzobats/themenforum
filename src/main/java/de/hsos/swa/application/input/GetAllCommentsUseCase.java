package de.hsos.swa.application.input;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.out.Result;
import de.hsos.swa.domain.entity.Comment;
import java.util.List;
@InputPort
public interface GetAllCommentsUseCase {
    Result<List<Comment>> getAllComments(boolean includeComments);
}
