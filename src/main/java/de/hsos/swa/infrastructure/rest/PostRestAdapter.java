package de.hsos.swa.infrastructure.rest;

import de.hsos.swa.application.input.VotePostInputPort;
import de.hsos.swa.application.input.dto.in.VotePostInputPortRequest;
import de.hsos.swa.infrastructure.rest.dto.in.VotePostRestAdapterRequest;
import de.hsos.swa.infrastructure.rest.validation.ValidationResult;
import de.hsos.swa.infrastructure.rest.dto.out.PostDto;
import de.hsos.swa.infrastructure.rest.dto.in.CreatePostRestAdapterRequest;
import de.hsos.swa.application.input.dto.in.PostFilterParams;
import de.hsos.swa.application.output.Result;
import de.hsos.swa.application.input.CreatePostInputPort;
import de.hsos.swa.application.input.dto.in.CreatePostInputPortRequest;
import de.hsos.swa.application.input.GetAllPostsInputPort;
import de.hsos.swa.application.input.dto.in.GetAllPostsInputPortRequest;
import de.hsos.swa.application.input.GetPostByIdInputPort;
import de.hsos.swa.application.input.dto.in.GetPostByIdInputPortRequest;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.infrastructure.rest.validation.PostValidationService;
import org.jboss.logging.Logger;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.swing.*;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/posts")
@Transactional(value = Transactional.TxType.REQUIRES_NEW)


//TODO AdapterRequests validieren

public class PostRestAdapter {
    @Inject
    CreatePostInputPort createPostInputPort;

    @Inject
    GetPostByIdInputPort getPostByIdInputPort;

    @Inject
    GetAllPostsInputPort getAllPostsInputPort;

    @Inject
    VotePostInputPort votePostInputPort;

    @Inject
    PostValidationService validationService;

    @Inject
    Logger log;


    @GET
    // --> (Query Params)
    // <-- GetAllPostsRestAdapterResponse
    public Response getAllPosts(@DefaultValue("true") @QueryParam("includeComments") Boolean includeComments,
                                @QueryParam("username") String username,
                                @QueryParam("dateFrom") String dateFrom,
                                @QueryParam("dateTo") String dateTo,
                                @QueryParam("sortBy") String sortBy,
                                @QueryParam("sortOrder") SortOrder sortOrder) {
        try {
            Map<PostFilterParams, Object> filterParams = new HashMap<>();
            filterParams.put(PostFilterParams.INCLUDE_COMMENTS, includeComments);
            if (username != null)
                filterParams.put(PostFilterParams.USERNAME, username);
            if (dateFrom != null)
                filterParams.put(PostFilterParams.DATE_FROM, dateFrom);
            if (dateTo != null)
                filterParams.put(PostFilterParams.DATE_TO, dateTo);
            if (sortBy != null)
                filterParams.put(PostFilterParams.SORT_BY, sortBy);
            if (sortOrder != null)
                filterParams.put(PostFilterParams.SORT_ORDER, sortOrder);   // TODO: Evtl. auch hier String nutzen? DESCENDING / ASCENDING

            GetAllPostsInputPortRequest query = new GetAllPostsInputPortRequest(filterParams);
            Result<List<Post>> postsResult = this.getAllPostsInputPort.getAllPosts(query);
            if (postsResult.isSuccessful()) {
                List<PostDto> postsResponse = postsResult.getData().stream().map(PostDto.Converter::fromDomainEntity).toList();
                return Response.status(Response.Status.OK).entity(postsResponse).build();
            }
            return Response.status(Response.Status.NOT_FOUND).entity(new ValidationResult(postsResult.getErrorMessage())).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }


    @GET
    @Path("{id}")
    // --> String id
    // <-- GetPostByIdRestAdapterResponse
    public Response getPostById(@PathParam("id") String id,
                                @DefaultValue("true") @QueryParam("comments") boolean includeComments) {
        try {
            GetPostByIdInputPortRequest query = new GetPostByIdInputPortRequest(id, includeComments);
            Result<Post> postResult = this.getPostByIdInputPort.getPostById(query);
            if (postResult.isSuccessful()) {
                PostDto response = PostDto.Converter.fromDomainEntity(postResult.getData());
                return Response.status(Response.Status.OK).entity(response).build();
            }
            return Response.status(Response.Status.NOT_FOUND).entity(new ValidationResult(postResult.getErrorMessage())).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }

    @POST
    @RolesAllowed("member")
    // --> CreatePostRestAdapterRequest
    // <-- CreatePostRestAdapterResponse
    public Response createPost(@NotNull CreatePostRestAdapterRequest request, @Context SecurityContext securityContext) {
        try {
            validationService.validatePost(request);
            String username = securityContext.getUserPrincipal().getName();
            CreatePostInputPortRequest command = CreatePostRestAdapterRequest.Converter.toInputPort(request, username);
            Result<Post> postResult = this.createPostInputPort.createPost(command);

            if (postResult.isSuccessful()) {
                PostDto postResponse = PostDto.Converter.fromDomainEntity(postResult.getData());
                // TODO: Neben Body auch Uri Builder nutzen um RessourceLink im Header zurückzugeben (Gilt für alle POST/UPDATE)
                return Response.status(Response.Status.CREATED).entity(postResponse).build();
            }
            return Response.status(Response.Status.BAD_REQUEST).entity(postResult.getErrorMessage()).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }


    @PUT
    @Path("/{id}/vote")
    @RolesAllowed("member")
    // https://developer.salesforce.com/docs/atlas.en-us.chatterapi.meta/chatterapi/connect_resources_comments_capability_up_down_vote.htm?q=downVote
    public Response votePost(@NotNull VotePostRestAdapterRequest request, @PathParam("id") String id, @Context SecurityContext securityContext) {
        try {
            validationService.validateVote(request);
            String username = securityContext.getUserPrincipal().getName();
            VotePostInputPortRequest command = VotePostRestAdapterRequest.Converter.toInputPortCommand(request, id, username);
            Result<Boolean> postResult = this.votePostInputPort.votePost(command);

            if (postResult.isSuccessful()) {
                return Response.status(Response.Status.OK).build();
            }
            return Response.status(Response.Status.BAD_REQUEST).entity(postResult.getErrorMessage()).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }
}
