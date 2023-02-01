package de.hsos.swa.application.input;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.in.GetCommentsByUserQuery;
import de.hsos.swa.application.input.dto.out.Result;
import de.hsos.swa.domain.entity.Comment;

import javax.enterprise.context.RequestScoped;
import javax.validation.Valid;
import java.util.List;

@InputPort
public interface GetCommentsByUserUseCase {
    Result<List<Comment>> getCommentsByUser(@Valid GetCommentsByUserQuery request);
}