package de.hsos.swa.application.input.query;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.input.dto.in.GetCommentByIdQuery;
import de.hsos.swa.domain.entity.Comment;

import javax.validation.Valid;

@InputPort
public interface GetCommentByIdUseCase {
    ApplicationResult<Comment> getCommentById(@Valid GetCommentByIdQuery query);
}
