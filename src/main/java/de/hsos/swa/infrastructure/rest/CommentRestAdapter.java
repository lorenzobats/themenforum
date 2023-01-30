package de.hsos.swa.infrastructure.rest;

import de.hsos.swa.application.input.*;
import de.hsos.swa.application.input.dto.in.*;
import de.hsos.swa.application.input.dto.out.TopicWithPostCountDto;
import de.hsos.swa.application.use_case_query.GetAllCommentsUseCase;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.infrastructure.rest.dto.in.CreatePostRestAdapterRequest;
import de.hsos.swa.infrastructure.rest.dto.in.VoteCommentRestAdapterRequest;
import de.hsos.swa.infrastructure.rest.dto.out.TopicDto;
import de.hsos.swa.infrastructure.rest.validation.ValidationResult;
import de.hsos.swa.infrastructure.rest.dto.in.CommentPostRestAdapterRequest;
import de.hsos.swa.infrastructure.rest.dto.in.ReplyToCommentRestAdapterRequest;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.infrastructure.rest.dto.out.CommentDto;
import de.hsos.swa.infrastructure.rest.validation.CommentValidationService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;
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

    @Inject
    GetCommentByIdInputPort getCommentByIdInputPort;

    @Inject
    CommentPostInputPort commentPostInputPort;

    @Inject
    ReplyToCommentInputPort replyToCommentInputPort;

    @Inject
    VoteCommentInputPort voteCommentInputPort;

    @Inject
    DeleteCommentInputPort deleteCommentInputPort;

    @Inject
    GetAllCommentsInputPort getAllCommentsInputPort;

    @Inject
    CommentValidationService validationService;


    @GET
    @Path("{id}")
    public Response getCommentById(@PathParam("id") String id) {
        try {
            GetCommentByIdInputPortRequest query = new GetCommentByIdInputPortRequest(id);
            Result<Comment> commentResult = this.getCommentByIdInputPort.getCommentById(query);
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
    public Response getAllComments(@DefaultValue("true") @QueryParam("includeReplies") Boolean includeReplies) {
        try {
            // TODO: Query (includeComments)
            Result<List<Comment>> commentsResult = this.getAllCommentsInputPort.getAllComments(includeReplies);
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
    public Response commentPost(
            @RequestBody(
                    description = "Post to create",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CommentPostRestAdapterRequest.class))
            ) CommentPostRestAdapterRequest request, @Context SecurityContext securityContext) {
        try {
            validationService.validateComment(request);
            String username = securityContext.getUserPrincipal().getName();
            CommentPostInputPortRequest command = CommentPostRestAdapterRequest.Converter.toInputPortCommand(request, username);
            Result<Comment> result = this.commentPostInputPort.commentPost(command);
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
    public Response replyToComment(@PathParam("id") String id, ReplyToCommentRestAdapterRequest request, @Context SecurityContext securityContext) {
        try {
            validationService.validateReply(request);
            String username = securityContext.getUserPrincipal().getName();
            ReplyToCommentInputPortRequest command = ReplyToCommentRestAdapterRequest.Converter.toInputPortCommand(request, id, username);
            Result<Comment> result = this.replyToCommentInputPort.replyToComment(command);
            if (result.isSuccessful()) {
                CommentDto response = CommentDto.Converter.fromDomainEntity(result.getData());
                return Response.status(Response.Status.OK).entity(response).build();
            }
            return Response.status(Response.Status.NOT_FOUND).entity(new ValidationResult(result.getMessage())).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }


    @PUT
    // TODO: implementieren => nutze "UpdateCommentInputPort"
    @RolesAllowed({"member"})
    public Response updateComment(@Context SecurityContext securityContext) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @PATCH
    @Path("/{id}/vote")
    @RolesAllowed("member")
    public Response voteComment(@NotNull VoteCommentRestAdapterRequest request, @PathParam("id") String id, @Context SecurityContext securityContext) {
        try {
            validationService.validateVote(request);
            String username = securityContext.getUserPrincipal().getName();
            VoteCommentInputPortRequest command = VoteCommentRestAdapterRequest.Converter.toInputPortCommand(request, id, username);
            Result<Comment> commentResult = this.voteCommentInputPort.voteComment(command);
            if (commentResult.isSuccessful()) {
                CommentDto commentDto = CommentDto.Converter.fromDomainEntity(commentResult.getData());
                return Response.status(Response.Status.OK).entity(commentDto).build();
            }
            return Response.status(Response.Status.BAD_REQUEST).entity(commentResult.getMessage()).build();
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
            DeleteCommentInputPortRequest command = new DeleteCommentInputPortRequest(id, username);
            Result<Comment> commentResult = this.deleteCommentInputPort.deleteComment(command);

            if (commentResult.isSuccessful()) {
                CommentDto commentDto = CommentDto.Converter.fromDomainEntity(commentResult.getData());
                return Response.status(Response.Status.OK).entity(commentDto).build();
            }
            return Response.status(Response.Status.BAD_REQUEST).entity(commentResult.getMessage()).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }

    @DELETE
    // TODO: implementieren => nutze "DeleteCommentVoteInputPort"
    @Path("/{id}/vote")
    @RolesAllowed("member")
    public Response deleteCommentVote(@PathParam("id") String id, @Context SecurityContext securityContext) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }
}
