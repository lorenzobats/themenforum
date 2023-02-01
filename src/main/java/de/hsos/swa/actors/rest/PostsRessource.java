package de.hsos.swa.actors.rest;

import de.hsos.swa.actors.rest.dto.in.CreatePostRequestBody;
import de.hsos.swa.actors.rest.dto.in.validation.ValidationService;
import de.hsos.swa.actors.rest.dto.out.PostDto;
import de.hsos.swa.actors.rest.dto.in.validation.ValidationResult;
import de.hsos.swa.application.annotations.Adapter;
import de.hsos.swa.application.input.*;
import de.hsos.swa.application.input.dto.in.*;
import de.hsos.swa.application.input.dto.out.Result;
import de.hsos.swa.application.output.repository.VoteRepository;
import de.hsos.swa.application.service.query.params.OrderParams;
import de.hsos.swa.application.service.query.params.PostFilterParams;
import de.hsos.swa.application.service.query.params.SortingParams;
import de.hsos.swa.domain.entity.Post;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.jboss.logging.Logger;

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
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("api/v1/posts")
@Transactional(value = Transactional.TxType.REQUIRES_NEW)
@Adapter
public class PostsRessource {
    @Inject
    CreatePostUseCase createPostUseCase;

    @Inject
    GetPostByIdUseCase getPostByIdUseCase;

    @Inject
    GetFilteredPostsUseCase getFilteredPostsUseCase;

    @Inject
    DeletePostUseCase deletePostUseCase;

    @Inject
    ValidationService validationService;


    @Inject
    VoteRepository voteRepository;

    @Inject
    Logger log;


    @GET
    public Response getPosts(@DefaultValue("true") @QueryParam("includeComments") Boolean includeComments,
                             @QueryParam("username") String username,
                             @QueryParam("userId") UUID userId,
                             @QueryParam("dateFrom") LocalDateTime dateFrom,
                             @QueryParam("dateTo") LocalDateTime dateTo,
                             @QueryParam("topic") String topic,
                             @QueryParam("topicId") UUID topicId,
                             @DefaultValue("VOTES") @QueryParam("sortBy") SortingParams sortBy,
                             @DefaultValue("DESC") @QueryParam("orderBy") OrderParams orderBy) {
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
            if (topic != null)
                filterParams.put(PostFilterParams.TOPIC, topic);
            if (topicId != null)
                filterParams.put(PostFilterParams.TOPICID, topicId);

            GetFilteredPostQuery query = new GetFilteredPostQuery(filterParams, includeComments, sortBy, orderBy);
            Result<List<Post>> postsResult = this.getFilteredPostsUseCase.getFilteredPosts(query);

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
            @DefaultValue("true") @QueryParam("includeComments") boolean includeComments,
            @DefaultValue("VOTES") @QueryParam("sortBy") SortingParams sortBy,
            @DefaultValue("DESC") @QueryParam("orderBy") OrderParams orderBy) {
        try {
            GetPostByIdQuery query = new GetPostByIdQuery(id, includeComments, sortBy, orderBy);
            Result<Post> postResult = this.getPostByIdUseCase.getPostById(query);

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
    public Response createPost(
            @NotNull @RequestBody(
                    description = "Post to create",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreatePostRequestBody.class))
            ) CreatePostRequestBody request,
            @Context SecurityContext securityContext) {
        try {
            validationService.validate(request);
            String username = securityContext.getUserPrincipal().getName();
            CreatePostCommand command = CreatePostRequestBody.Converter.toInputPortCommand(request, username);
            Result<Post> postResult = this.createPostUseCase.createPost(command);

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

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"member", "admin"})
    public Response deletePost(@PathParam("id") String id, @Context SecurityContext securityContext) {
        try {
            String username = securityContext.getUserPrincipal().getName();
            DeletePostCommand command = new DeletePostCommand(id, username);
            Result<Post> postResult = this.deletePostUseCase.deletePost(command);

            if (postResult.isSuccessful()) {
                PostDto postDto = PostDto.Converter.fromDomainEntity(postResult.getData());
                return Response.status(Response.Status.OK).entity(postDto).build();
            }
            return Response.status(Response.Status.BAD_REQUEST).entity(postResult.getMessage()).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }
}