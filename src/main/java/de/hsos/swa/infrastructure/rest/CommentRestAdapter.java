package de.hsos.swa.infrastructure.rest;

import de.hsos.swa.application.input.VoteCommentInputPort;
import de.hsos.swa.application.input.dto.in.VoteCommentInputPortRequest;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.infrastructure.rest.dto.in.VoteCommentRestAdapterRequest;
import de.hsos.swa.infrastructure.rest.validation.ValidationResult;
import de.hsos.swa.infrastructure.rest.dto.in.CommentPostRestAdapterRequest;
import de.hsos.swa.infrastructure.rest.dto.in.ReplyToCommentRestAdapterRequest;
import de.hsos.swa.application.input.CommentPostInputPort;
import de.hsos.swa.application.input.GetCommentByIdInputPort;
import de.hsos.swa.application.input.ReplyToCommentInputPort;
import de.hsos.swa.application.output.Result;
import de.hsos.swa.application.input.dto.in.CommentPostInputPortRequest;
import de.hsos.swa.application.input.dto.in.GetCommentByIdInputPortRequest;
import de.hsos.swa.application.input.dto.in.ReplyToCommentInputPortRequest;
import de.hsos.swa.infrastructure.rest.dto.out.CommentDto;
import de.hsos.swa.infrastructure.rest.validation.CommentValidationService;

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

@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/comments")
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
    CommentValidationService validationService;


    @GET
    @Path("{id}")
    public Response getCommentById(@PathParam("id") String id) {
        try {
            GetCommentByIdInputPortRequest query = new GetCommentByIdInputPortRequest(id);
            Result<Comment> commentResult = this.getCommentByIdInputPort.getCommentById(query);
            if (commentResult.isSuccessful()) {
                CommentDto response = CommentDto.Converter.toDto(commentResult.getData());
                return Response.status(Response.Status.OK).entity(response).build();
            }
            return Response.status(Response.Status.NOT_FOUND).entity(new ValidationResult(commentResult.getErrorMessage())).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }

    @POST
    @RolesAllowed("member")
    public Response commentPost(CommentPostRestAdapterRequest request, @Context SecurityContext securityContext) {
        try {
            validationService.validateComment(request);
            String username = securityContext.getUserPrincipal().getName();
            CommentPostInputPortRequest command = CommentPostRestAdapterRequest.Converter.toInputPort(request, username);
            Result<Comment> result = this.commentPostInputPort.commentPost(command);
            if (result.isSuccessful()) {
                CommentDto response = CommentDto.Converter.toDto(result.getData());
                return Response.status(Response.Status.OK).entity(response).build();
            }
            return Response.status(Response.Status.NOT_FOUND).entity(new ValidationResult(result.getErrorMessage())).build();
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
            ReplyToCommentInputPortRequest command = ReplyToCommentRestAdapterRequest.Converter.toInputPort(request, id, username);
            Result<Comment> result = this.replyToCommentInputPort.replyToComment(command);
            if (result.isSuccessful()) {
                CommentDto response = CommentDto.Converter.toDto(result.getData());
                return Response.status(Response.Status.OK).entity(response).build();
            }
            return Response.status(Response.Status.NOT_FOUND).entity(new ValidationResult(result.getErrorMessage())).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }

    @PUT
    @Path("/{id}/vote")
    @RolesAllowed("member")
    public Response voteComment(@NotNull VoteCommentRestAdapterRequest request, @PathParam("id") String id, @Context SecurityContext securityContext) {
        try {
            validationService.validateVote(request);
            String username = securityContext.getUserPrincipal().getName();
            VoteCommentInputPortRequest command = VoteCommentRestAdapterRequest.Converter.toInputPort(request, id, username);
            Result<Comment> postResult = this.voteCommentInputPort.voteComment(command);

            if (postResult.isSuccessful()) {
                CommentDto postResponse = CommentDto.Converter.toDto(postResult.getData());
                return Response.status(Response.Status.CREATED).entity(postResponse).build();
            }
            return Response.status(Response.Status.BAD_REQUEST).entity(postResult.getErrorMessage()).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }
}
