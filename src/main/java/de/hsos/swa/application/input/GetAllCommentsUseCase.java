package de.hsos.swa.application.input;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.in.GetAllCommentsQuery;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.output.auth.dto.in.AuthorizationResult;
import de.hsos.swa.domain.entity.Comment;

import javax.validation.Valid;
import java.util.List;
@InputPort
public interface GetAllCommentsUseCase {
    ApplicationResult<List<Comment>> getAllComments(@Valid GetAllCommentsQuery query);
}
