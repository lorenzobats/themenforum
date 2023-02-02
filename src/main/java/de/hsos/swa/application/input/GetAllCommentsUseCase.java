package de.hsos.swa.application.input;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.domain.entity.Comment;
import java.util.List;
@InputPort
public interface GetAllCommentsUseCase {
    ApplicationResult<List<Comment>> getAllComments(boolean includeComments);
}
