package de.hsos.swa.infrastructure.rest;

import de.hsos.swa.infrastructure.rest.validation.ValidationResult;
import de.hsos.swa.infrastructure.rest.request.CommentPostRestAdapterRequest;
import de.hsos.swa.infrastructure.rest.request.ReplyToCommentRestAdapterRequest;
import de.hsos.swa.application.input.CommentPostInputPort;
import de.hsos.swa.application.input.GetCommentByIdInputPort;
import de.hsos.swa.application.input.ReplyToCommentInputPort;
import de.hsos.swa.application.Result;
import de.hsos.swa.application.input.request.CommentPostInputPortRequest;
import de.hsos.swa.application.input.request.GetCommentByIdInputPortRequest;
import de.hsos.swa.application.input.request.ReplyToCommentInputPortRequest;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.infrastructure.rest.dto.CommentDto;
import de.hsos.swa.infrastructure.rest.validation.CommentValidationService;

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
}
