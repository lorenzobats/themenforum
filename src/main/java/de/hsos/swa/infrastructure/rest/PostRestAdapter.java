package de.hsos.swa.infrastructure.rest;

import de.hsos.swa.application.input.*;
import de.hsos.swa.application.input.dto.in.*;
import de.hsos.swa.infrastructure.rest.dto.in.VotePostRestAdapterRequest;
import de.hsos.swa.infrastructure.rest.validation.ValidationResult;
import de.hsos.swa.infrastructure.rest.dto.out.PostDto;
import de.hsos.swa.infrastructure.rest.dto.in.CreatePostRestAdapterRequest;
import de.hsos.swa.application.PostFilterParams;
import de.hsos.swa.application.util.Result;
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
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    GetAllPostsInputPort getAllPostsInputPort;

    @Inject
    GetFilteredPostsInputPort getFilteredPostsInputPort;

    @Inject
    VotePostInputPort votePostInputPort;

    @Inject
    DeletePostInputPort deletePostInputPort;

    @Inject
    PostValidationService validationService;


    @Inject
    Logger log;


    @GET
    public Response getAllPosts(@DefaultValue("true") @QueryParam("includeComments") Boolean includeComments,
                                @QueryParam("username") String username,
                                @QueryParam("userId") UUID userId,
                                @QueryParam("dateFrom") LocalDateTime dateFrom,
                                @QueryParam("dateTo") LocalDateTime dateTo,
                                @QueryParam("topic") String topic,
                                @QueryParam("sortBy") String sortBy,
                                @QueryParam("sortOrder") SortOrder sortOrder) {
        try {
            Map<PostFilterParams, Object> filterParams = new HashMap<>();
            if (username != null)
                filterParams.put(PostFilterParams.USERNAME, username);
            if (userId != null)
                filterParams.put(PostFilterParams.USERID, userId);
            if (dateFrom != null)
                filterParams.put(PostFilterParams.DATE_FROM, dateFrom);
            if (dateTo != null)
                filterParams.put(PostFilterParams.DATE_TO, dateTo);
            if(topic != null)
                filterParams.put(PostFilterParams.TOPIC, topic);
            if (sortBy != null)
                filterParams.put(PostFilterParams.SORT_BY, sortBy); // TODO: Implementieren nach Datum / nach anzahlUpvotes
            if (sortOrder != null)
                filterParams.put(PostFilterParams.SORT_ORDER, sortOrder);   // TODO: Implementieren

            GetFilteredPostInputPortRequest query = new GetFilteredPostInputPortRequest(filterParams, includeComments);
            Result<List<Post>> postsResult = this.getFilteredPostsInputPort.getFilteredPosts(query);
            if (postsResult.isSuccessful()) {
                List<PostDto> postsResponse = postsResult.getData().stream().map(PostDto.Converter::fromDomainEntity).toList();
                return Response.status(Response.Status.OK).entity(postsResponse).build();
            }
            return Response.status(Response.Status.NOT_FOUND).entity(new ValidationResult(postsResult.getMessage())).build();
        } catch (DateTimeParseException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getMessage())).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }


    @GET
    @Path("{id}")
    public Response getPostById(@PathParam("id") String id,
                                @DefaultValue("true") @QueryParam("includeComments") boolean includeComments) {
        try {
            GetPostByIdInputPortRequest query = new GetPostByIdInputPortRequest(id, includeComments);
            Result<Post> postResult = this.getPostByIdInputPort.getPostById(query);
            if (postResult.isSuccessful()) {
                PostDto response = PostDto.Converter.fromDomainEntity(postResult.getData());
                return Response.status(Response.Status.OK).entity(response).build();
            }
            return Response.status(Response.Status.NOT_FOUND).entity(new ValidationResult(postResult.getMessage())).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }


    @POST
    @RolesAllowed({"member"})
    public Response createPost(@NotNull CreatePostRestAdapterRequest request, @Context SecurityContext securityContext) {
        try {
            validationService.validatePost(request);
            String username = securityContext.getUserPrincipal().getName();
            CreatePostInputPortRequest command = CreatePostRestAdapterRequest.Converter.toInputPortCommand(request, username);
            Result<Post> postResult = this.createPostInputPort.createPost(command);

            if (postResult.isSuccessful()) {
                PostDto postResponse = PostDto.Converter.fromDomainEntity(postResult.getData());
                // TODO: URI.Create auslagern und Hilfsklasse schreiben?
                return Response.status(Response.Status.CREATED).location(URI.create("/posts/" + postResponse.id)).entity(postResponse).build();
            }
            return Response.status(Response.Status.BAD_REQUEST).entity(postResult.getMessage()).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }


    @PUT
    // TODO: implementieren => nutze "UpdatePostInputPort"
    @RolesAllowed({"member"})
    public Response updatePost(@Context SecurityContext securityContext) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }


    // TODO: Dont allow "none" -> Domain Service muss Vote löschen
    // TODO: Evtl. in Vote Adapter verschieben?
    @PATCH
    @Path("/{id}/vote")
    @RolesAllowed("member")
    public Response votePost(@NotNull VotePostRestAdapterRequest request, @PathParam("id") String id, @Context SecurityContext securityContext) {
        try {
            validationService.validateVote(request);
            String username = securityContext.getUserPrincipal().getName();
            VotePostInputPortRequest command = VotePostRestAdapterRequest.Converter.toInputPortCommand(request, id, username);
            Result<Post> postResult = this.votePostInputPort.votePost(command);

            if (postResult.isSuccessful()) {
                PostDto postDto = PostDto.Converter.fromDomainEntity(postResult.getData());
                return Response.status(Response.Status.OK).entity(postDto).build();
            }
            return Response.status(Response.Status.BAD_REQUEST).entity(postResult.getMessage()).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"member", "admin"})
    public Response deletePost(@PathParam("id") String id, @Context SecurityContext securityContext) {
        try {
            String username = securityContext.getUserPrincipal().getName();
            DeletePostInputPortRequest command = new DeletePostInputPortRequest(id, username);
            Result<Post> postResult = this.deletePostInputPort.deletePost(command);

            if (postResult.isSuccessful()) {
                PostDto postDto = PostDto.Converter.fromDomainEntity(postResult.getData());
                return Response.status(Response.Status.OK).entity(postDto).build();
            }
            return Response.status(Response.Status.BAD_REQUEST).entity(postResult.getMessage()).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }

    @DELETE
    // TODO: implementieren => nutze "DeletePostVoteInputPort" // Oder in VoteAdapter verschieben?
    @Path("/{id}/vote")
    @RolesAllowed("member")
    public Response deletePostVote(@PathParam("id") String id, @Context SecurityContext securityContext) {
            return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }
}
