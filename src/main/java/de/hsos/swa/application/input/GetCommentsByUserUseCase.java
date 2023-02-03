package de.hsos.swa.application.input;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.in.GetCommentsByUserQuery;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.domain.entity.Comment;

import javax.validation.Valid;
import java.util.List;

@InputPort
public interface GetCommentsByUserUseCase {
    ApplicationResult<List<Comment>> getCommentsByUser(@Valid GetCommentsByUserQuery query);
}