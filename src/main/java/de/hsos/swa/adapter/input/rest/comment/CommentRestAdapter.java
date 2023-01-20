package de.hsos.swa.adapter.input.rest.comment;

import de.hsos.swa.adapter.input.rest.comment.request.CommentPostRestAdapterRequest;
import de.hsos.swa.adapter.input.rest.comment.request.ReplyToCommentRestAdapterRequest;
import de.hsos.swa.adapter.input.rest.comment.response.CommentPostRestAdapterResponse;
import de.hsos.swa.adapter.input.rest.comment.response.GetCommentByIdRestAdapterResponse;
import de.hsos.swa.adapter.input.rest._validation.ValidationResult;
import de.hsos.swa.adapter.input.rest.comment.response.ReplyToCommentRestAdapterResponse;
import de.hsos.swa.application.port.input._shared.Result;
import de.hsos.swa.application.port.input.commentPost.CommentPostInputPort;
import de.hsos.swa.application.port.input.commentPost.CommentPostInputPortRequest;
import de.hsos.swa.application.port.input.commentPost.CommentPostInputPortResponse;
import de.hsos.swa.application.port.input.getCommentById.GetCommentByIdInputPort;
import de.hsos.swa.application.port.input.getCommentById.GetCommentByIdInputPortRequest;
import de.hsos.swa.application.port.input.getCommentById.GetCommentByIdInputPortResponse;
import de.hsos.swa.application.port.input.replyToComment.ReplyToCommentInputPort;
import de.hsos.swa.application.port.input.replyToComment.ReplyToCommentInputPortRequest;
import de.hsos.swa.application.port.input.replyToComment.ReplyToCommentInputPortResponse;

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


    @GET
    @Path("{id}")
    // --> String id
    // <-- GetCommentByIdRestAdapterResponse
    public Response getCommentById(@PathParam("id") String id) {
        try {
            GetCommentByIdInputPortRequest query = new GetCommentByIdInputPortRequest(id);
            Result<GetCommentByIdInputPortResponse> result = this.getCommentByIdInputPort.getCommentById(query);
            if (result.isSuccessful()) {
                GetCommentByIdRestAdapterResponse response = GetCommentByIdRestAdapterResponse.Converter.fromInputPortResult(result.getData());
                return Response.status(Response.Status.OK).entity(response).build();
            }
            return Response.status(Response.Status.NOT_FOUND).entity(new ValidationResult(result.getErrorMessage())).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }

    @POST
    @RolesAllowed("member")
    // --> CommentPostRestAdapterRequest
    // <-- CommentPostRestAdapterResponse
    public Response commentPost(CommentPostRestAdapterRequest request, @Context SecurityContext securityContext) {
        // TODO: Diese Message sinnvoll abfangen
        if(request == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult("Empy Request Body")).build();

        try {
            String username = securityContext.getUserPrincipal().getName();
            CommentPostInputPortRequest command = new CommentPostInputPortRequest(request.postId, username, request.text);
            Result<CommentPostInputPortResponse> result = this.commentPostInputPort.commentPost(command);
            if (result.isSuccessful()) {
                CommentPostRestAdapterResponse response = CommentPostRestAdapterResponse.Converter.fromInputPortResult(result.getData());
                return Response.status(Response.Status.OK).entity(response).build();
            }
            return Response.status(Response.Status.NOT_FOUND).entity(new ValidationResult(result.getErrorMessage())).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }

    @POST
    @Path("/{id}")
    // --> String id + ReplyToCommentRestAdapterRequest
    // <-- ReplyToCommentRestAdapterResponse
    public Response replyToComment(@PathParam("id") String id, ReplyToCommentRestAdapterRequest request, @Context SecurityContext securityContext) {
        // TODO: Diese Message sinnvoll abfangen
        if(request == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult("Empy Request Body2")).build();

        try {
            String username = securityContext.getUserPrincipal().getName();
            // TODO: Hier vllt nicht vom Nutzer die PostId fordern, da diese nicht zur Identifizierung benötigt wird.
            // TODO: Stattdessen für Use Case OutputPort definieren, der einem den zugehörigen Post zu einer CommentId holt!
            ReplyToCommentInputPortRequest command = new ReplyToCommentInputPortRequest(request.postId,  request.commentId, username,  request.text);
            Result<ReplyToCommentInputPortResponse> result = this.replyToCommentInputPort.replyToComment(command);
            if (result.isSuccessful()) {
                ReplyToCommentRestAdapterResponse response = ReplyToCommentRestAdapterResponse.Converter.fromInputPortResult(result.getData());
                return Response.status(Response.Status.OK).entity(response).build();
            }
            return Response.status(Response.Status.NOT_FOUND).entity(new ValidationResult(result.getErrorMessage())).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }
}
