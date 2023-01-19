package de.hsos.swa.adapter.input.rest;

import de.hsos.swa.adapter.input.rest.dto.*;
import de.hsos.swa.adapter.input.rest.validation.ValidationResult;
import de.hsos.swa.application.port.input._shared.Result;
import de.hsos.swa.application.port.input.commentPost.CommentPostInputPort;
import de.hsos.swa.application.port.input.commentPost.CommentPostInputPortRequest;
import de.hsos.swa.application.port.input.commentPost.CommentPostInputPortResponse;
import de.hsos.swa.application.port.input.createPost.CreatePostInputPort;
import de.hsos.swa.application.port.input.createPost.CreatePostInputPortRequest;
import de.hsos.swa.application.port.input.createPost.CreatePostInputPortResponse;
import de.hsos.swa.application.port.input.getPostById.GetPostByIdInputPort;
import de.hsos.swa.application.port.input.getPostById.GetPostByIdInputPortRequest;
import de.hsos.swa.application.port.input.getPostById.GetPostByIdInputPortResponse;
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
@Path("/posts")
@Transactional(value = Transactional.TxType.REQUIRES_NEW)
public class PostRestAdapter {
    @Inject
    CreatePostInputPort createPostInputPort;

    @Inject
    GetPostByIdInputPort getPostByIdInputPort;

    @Inject
    CommentPostInputPort commentPostInputPort;

    @Inject
    ReplyToCommentInputPort replyToCommentInputPort;


    @POST
    @RolesAllowed("member")
    public Response createPost(CreatePostRestAdapterRequest request, @Context SecurityContext securityContext) {
        try {
            String username = securityContext.getUserPrincipal().getName();
            CreatePostInputPortRequest inputPortRequest = CreatePostRestAdapterRequest.Converter.toInputPortRequest(request, username);
            Result<CreatePostInputPortResponse> inputPortResult = this.createPostInputPort.createPost(inputPortRequest);
            if (inputPortResult.isSuccessful()) {
                CreatePostRestAdapterResponse response = CreatePostRestAdapterResponse.Converter.fromInputPortResult(inputPortResult.getData());
                // TODO: Uri Builder nutzen um RessourceLink im Header zurückzugeben
                return Response.status(Response.Status.CREATED).entity(response).build();
            }
            return Response.status(Response.Status.BAD_REQUEST).entity(inputPortResult.getErrorMessage()).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }

    @GET
    @Path("{id}")
    public Response getPostById(@PathParam("id") String id, @QueryParam("comments") boolean includeComments) {
        try {
            Result<GetPostByIdInputPortResponse> inputPortResult = this.getPostByIdInputPort.getPostById(new GetPostByIdInputPortRequest(id));
            if (inputPortResult.isSuccessful()) {
                GetPostByIdRestAdapterResponse response = GetPostByIdRestAdapterResponse.Converter.fromUseCaseResult(inputPortResult.getData());
                return Response.status(Response.Status.OK).entity(response).build();
            }
            return Response.status(Response.Status.NOT_FOUND).entity(new ValidationResult(inputPortResult.getErrorMessage())).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }


    @POST
    @Path("{id}/comments")
    @RolesAllowed("member")
    public Response commentPost(@PathParam("id") String postId, @Context SecurityContext securityContext) {
        try {
            String username = securityContext.getUserPrincipal().getName();
            Result<CommentPostInputPortResponse> commentPostResult =
                    this.commentPostInputPort.commentPost(new CommentPostInputPortRequest(postId, username, "Testcomment"));

            if (commentPostResult.isSuccessful()) {
                return Response.status(Response.Status.OK).entity(commentPostResult.getData()).build();
            }
            return Response.status(Response.Status.NOT_FOUND).entity(new ValidationResult(commentPostResult.getErrorMessage())).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }

    @POST
    @Path("{postId}/comments/{commentId}")
    //Es ist eigentlich moeglich die UUID beim Comment wegzulassen und stattdessen das comment ueber "postId + localcommendId" zu identifizieren
    // -> Wie holt man sich dann alle Comments (unabhaenging von Posts)?
    public Response replyToComment(@PathParam("postId") String postId, @PathParam("commentId") String commentId, @Context SecurityContext securityContext) {
        try {
            String username = securityContext.getUserPrincipal().getName();

            Result<ReplyToCommentInputPortResponse> replyToComment =
                    this.replyToCommentInputPort.replyToComment(new ReplyToCommentInputPortRequest(postId, commentId, username, "Test Reply"));
            if(replyToComment.isSuccessful()) {
                return Response.status(Response.Status.CREATED).entity(replyToComment.getData()).build();
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }
}
