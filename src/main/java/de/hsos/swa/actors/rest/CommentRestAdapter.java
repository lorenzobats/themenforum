package de.hsos.swa.actors.rest;

import de.hsos.swa.actors.rest.dto.in.CommentPostRequestBody;
import de.hsos.swa.actors.rest.dto.in.ReplyToCommentRequestBody;
import de.hsos.swa.actors.rest.dto.in.validation.ValidationService;
import de.hsos.swa.actors.rest.dto.out.CommentDto;
import de.hsos.swa.actors.rest.dto.in.validation.ValidationResult;
import de.hsos.swa.application.input.*;
import de.hsos.swa.application.input.dto.in.*;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.application.input.dto.out.Result;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("api/v1/comments")
@Transactional(value = Transactional.TxType.REQUIRES_NEW)
public class CommentRestAdapter {

    // QUERIES
    @Inject
    GetAllCommentsUseCase getAllCommentsUseCase;
    @Inject
    GetCommentByIdUseCase getCommentByIdUseCase;

    // COMMANDS
    @Inject
    CommentPostUseCase commentPostUseCase;

    @Inject
    ReplyToCommentUseCase replyToCommentUseCase;

    @Inject
    DeleteCommentUseCase deleteCommentUseCase;



    @Inject
    ValidationService validationService;


    @GET
    @Path("{id}")
    @Counted(name = "getCommentById", description = "Wie oft eine Kommentar anahand ID gesucht wurde")
    @Timed(name = "getCommentByIdTimer", description = "Misst, wie lange es dauert ein Kommentar anhand ID zu suchen")
    public Response getCommentById(@PathParam("id") String id) {
        try {
            GetCommentByIdQuery query = new GetCommentByIdQuery(id);
            Result<Comment> commentResult = this.getCommentByIdUseCase.getCommentById(query);
            if (commentResult.isSuccessful()) {
                CommentDto response = CommentDto.Converter.fromDomainEntity(commentResult.getData());
                return Response.status(Response.Status.OK).entity(response).build();
            }
            return Response.status(Response.Status.NOT_FOUND).entity(new ValidationResult(commentResult.getMessage())).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }

    @GET
    @RolesAllowed({"admin", "member"})
    @Counted(name = "getAllComments", description = "Wie oft alle Kommentare abgerufen wurden")
    @Timed(name = "getAllCommentsTimer", description = "Misst, wie lange es dauert alle Kommentar abzurufen")
    public Response getAllComments(@DefaultValue("true") @QueryParam("includeReplies") Boolean includeReplies) {
        try {
            // TODO: Query (includeComments)
            Result<List<Comment>> commentsResult = this.getAllCommentsUseCase.getAllComments(includeReplies);
            if (commentsResult.isSuccessful()) {
                List<CommentDto> commentsResponse = commentsResult.getData().stream().map(CommentDto.Converter::fromDomainEntity).toList();
                return Response.status(Response.Status.OK).entity(commentsResponse).build();
            }
            return Response.status(Response.Status.NOT_FOUND).entity(commentsResult.getMessage()).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }

    @POST
    @RolesAllowed("member")
    @Counted(name = "postComment", description = "Wie oft ein Kommentar zu einem Post erstellt wurde")
    @Timed(name = "postCommentTimer", description = "Misst, wie lange es dauert ein Kommentar zu einem Post zu erstellen")
    public Response commentPost(
            @RequestBody(
                    description = "Comment to create",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CommentPostRequestBody.class))
            ) CommentPostRequestBody request, @Context SecurityContext securityContext) {
        try {
            validationService.validate(request);
            String username = securityContext.getUserPrincipal().getName();
            CommentPostCommand command = CommentPostRequestBody.Converter.toInputPortCommand(request, username);
            Result<Comment> result = this.commentPostUseCase.commentPost(command);
            if (result.isSuccessful()) {
                CommentDto response = CommentDto.Converter.fromDomainEntity(result.getData());
                return Response.status(Response.Status.OK).entity(response).build();
            }
            return Response.status(Response.Status.NOT_FOUND).entity(new ValidationResult(result.getMessage())).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }

    @POST
    @Path("/{id}")
    @Counted(name = "replyToComment", description = "Wie oft ein Kommentar geantwortet wurde")
    @Timed(name = "replyToCommentTimer", description = "Misst, wie lange es dauert auf ein Kommentar zu antworten")
    public Response replyToComment(@PathParam("id") String id, ReplyToCommentRequestBody request, @Context SecurityContext securityContext) {
        try {
            validationService.validate(request);
            String username = securityContext.getUserPrincipal().getName();
            ReplyToCommentCommand command = ReplyToCommentRequestBody.Converter.toInputPortCommand(request, id, username);
            Result<Comment> result = this.replyToCommentUseCase.replyToComment(command);
            if (result.isSuccessful()) {
                CommentDto response = CommentDto.Converter.fromDomainEntity(result.getData());
                return Response.status(Response.Status.OK).entity(response).build();
            }
            return Response.status(Response.Status.NOT_FOUND).entity(new ValidationResult(result.getMessage())).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }

    @DELETE
    @Path("/{id}/")
    @RolesAllowed({"member", "admin"})
    public Response deleteComment(@PathParam("id") String id, @Context SecurityContext securityContext) {
        try {
            String username = securityContext.getUserPrincipal().getName();
            DeleteCommentCommand command = new DeleteCommentCommand(id, username);
            Result<Comment> commentResult = this.deleteCommentUseCase.deleteComment(command);

            if (commentResult.isSuccessful()) {
                CommentDto commentDto = CommentDto.Converter.fromDomainEntity(commentResult.getData());
                return Response.status(Response.Status.OK).entity(commentDto).build();
            }
            return Response.status(Response.Status.BAD_REQUEST).entity(commentResult.getMessage()).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }
}
