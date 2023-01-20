package de.hsos.swa.adapter.input.rest;

import de.hsos.swa.adapter.input.rest.validation.PostValidationService;
import de.hsos.swa.adapter.input.rest.validation.ValidationResult;
import de.hsos.swa.adapter.input.rest.post.request.CreatePostRestAdapterRequest;
import de.hsos.swa.adapter.input.rest.post.response.CreatePostRestAdapterResponse;
import de.hsos.swa.adapter.input.rest.post.response.GetAllPostsRestAdapterResponse;
import de.hsos.swa.adapter.input.rest.post.response.GetPostByIdRestAdapterResponse;
import de.hsos.swa.application.PostFilterParams;
import de.hsos.swa.application.port.input.Result;
import de.hsos.swa.application.port.input.CreatePostInputPort;
import de.hsos.swa.application.port.input.request.CreatePostInputPortRequest;
import de.hsos.swa.application.port.input.GetAllPostsInputPort;
import de.hsos.swa.application.port.input.request.GetAllPostsInputPortRequest;
import de.hsos.swa.application.port.input.GetPostByIdInputPort;
import de.hsos.swa.application.port.input.request.GetPostByIdInputPortRequest;
import de.hsos.swa.domain.entity.Post;
import org.jboss.logging.Logger;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.swing.*;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
            if(username!= null)
                filterParams.put(PostFilterParams.USERNAME, username);
            if(dateFrom!= null)
                filterParams.put(PostFilterParams.DATE_FROM, dateFrom);
            if(dateTo!= null)
                filterParams.put(PostFilterParams.DATE_TO, dateTo);
            if(sortBy!= null)
                filterParams.put(PostFilterParams.SORT_BY, sortBy);
            if(sortOrder!= null)
                filterParams.put(PostFilterParams.SORT_ORDER, sortOrder);   // TODO: Evtl. auch hier String nutzen? DESCENDING / ASCENDING

            GetAllPostsInputPortRequest query = new GetAllPostsInputPortRequest(filterParams);
            Result<List<Post>> postsResult = this.getAllPostsInputPort.getAllPosts(query);
            if (postsResult.isSuccessful()) {
                GetAllPostsRestAdapterResponse response = GetAllPostsRestAdapterResponse.Converter.fromInputPortResult(postsResult.getData());
                return Response.status(Response.Status.OK).entity(response).build();
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
                GetPostByIdRestAdapterResponse response = GetPostByIdRestAdapterResponse.Converter.fromUseCaseResult(postResult.getData());
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
    public Response createPost(CreatePostRestAdapterRequest request, @Context SecurityContext securityContext) {
        try {
            validationService.validatePost(request);
            String username = securityContext.getUserPrincipal().getName();
            CreatePostInputPortRequest command = CreatePostRestAdapterRequest.Converter.toInputPortCommand(request, username);
            Result<UUID> result = this.createPostInputPort.createPost(command);
            if (result.isSuccessful()) {
                CreatePostRestAdapterResponse response = CreatePostRestAdapterResponse.Converter.fromInputPortResult(result.getData());
                // TODO: Neben Body auch Uri Builder nutzen um RessourceLink im Header zurückzugeben (Gilt für alle POST/UPDATE)
                return Response.status(Response.Status.CREATED).entity(response).build();
            }
            return Response.status(Response.Status.BAD_REQUEST).entity(result.getErrorMessage()).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }
}
