package de.hsos.swa.infrastructure.rest;
import de.hsos.swa.application.input.DeleteVoteInputPort;
import de.hsos.swa.application.input.VoteCommentInputPort;
import de.hsos.swa.application.input.VotePostInputPort;
import de.hsos.swa.application.input.dto.in.DeleteVoteInputPortRequest;
import de.hsos.swa.application.input.dto.in.VoteCommentInputPortRequest;
import de.hsos.swa.application.input.dto.in.VotePostInputPortRequest;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.Vote;
import de.hsos.swa.infrastructure.rest.dto.in.VoteCommentRestAdapterRequest;
import de.hsos.swa.infrastructure.rest.dto.in.VotePostRestAdapterRequest;
import de.hsos.swa.infrastructure.rest.dto.out.CommentDto;
import de.hsos.swa.infrastructure.rest.dto.out.PostDto;
import de.hsos.swa.infrastructure.rest.validation.CommentValidationService;
import de.hsos.swa.infrastructure.rest.validation.PostValidationService;
import de.hsos.swa.infrastructure.rest.validation.ValidationResult;

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
@Path("api/v1/votes")
@Transactional(value = Transactional.TxType.REQUIRES_NEW)
public class VoteRestAdapter {

    @Inject
    VotePostInputPort votePostInputPort;

    @Inject
    VoteCommentInputPort voteCommentInputPort;

    @Inject
    DeleteVoteInputPort deleteVoteInputPort;

    @Inject
    PostValidationService postValidationService;

    @Inject
    CommentValidationService commentValidationService;

    @GET
    // TODO: implementieren => nutze "GetVotedPostsByUserInputPort"
    @RolesAllowed({"admin", "member"})
    public Response getAllVotesByUser() {
        return Response.status(Response.Status.NOT_ACCEPTABLE).build();
    }

    @POST
    @Path("/post")
    public Response votePost(@NotNull VotePostRestAdapterRequest request, @Context SecurityContext securityContext) {
        try {
            postValidationService.validateVote(request);
            String username = securityContext.getUserPrincipal().getName();
            VotePostInputPortRequest command = VotePostRestAdapterRequest.Converter.toInputPortCommand(request, username);
            Result<Post> postResult = this.votePostInputPort.votePost(command);

            if (postResult.isSuccessful()) {
                PostDto postDto = PostDto.Converter.fromDomainEntity(postResult.getData());
                return Response.status(Response.Status.OK).entity(postDto).build();
            }
            return Response.status(Response.Status.BAD_REQUEST).entity("postResult.getMessage()").build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }

    @POST
    @Path("/comment")
    @RolesAllowed("member")
    public Response voteComment(@NotNull VoteCommentRestAdapterRequest request, @Context SecurityContext securityContext) {
        try {
            commentValidationService.validateVote(request);
            String username = securityContext.getUserPrincipal().getName();
            VoteCommentInputPortRequest command = VoteCommentRestAdapterRequest.Converter.toInputPortCommand(request, username);
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
    @Path("/post/{id}")
    @RolesAllowed("member")
    public Response deleteVote(@PathParam("id") String id, @Context SecurityContext securityContext) {
        try {
            String username = securityContext.getUserPrincipal().getName();

            Result<Vote> voteResult = this.deleteVoteInputPort.deleteVote(new DeleteVoteInputPortRequest(id, username));

            if (voteResult.isSuccessful()) {
                return Response.status(Response.Status.OK).entity(voteResult.getData()).build();
            }

            return Response.status(Response.Status.BAD_REQUEST).entity(voteResult.getMessage()).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }


}
