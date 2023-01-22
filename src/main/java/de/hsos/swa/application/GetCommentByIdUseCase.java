package de.hsos.swa.application;

import de.hsos.swa.application.output.Result;
import de.hsos.swa.application.input.GetCommentByIdInputPort;
import de.hsos.swa.application.input.dto.in.GetCommentByIdInputPortRequest;
import de.hsos.swa.application.output.repository.CommentRepository;
import de.hsos.swa.domain.entity.Comment;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.UUID;

@RequestScoped
public class GetCommentByIdUseCase implements GetCommentByIdInputPort {

    @Inject
    CommentRepository commentRepository;

    @Override
    public Result<Comment> getCommentById(GetCommentByIdInputPortRequest request) {
        Result<Comment> commentResult = commentRepository.getCommentById(UUID.fromString(request.getId()));
        if (commentResult.isSuccessful()) {
            return Result.success(commentResult.getData());
        }
        return Result.error("Cannot find Comment");
    }
}
