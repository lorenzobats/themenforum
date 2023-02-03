package de.hsos.swa.actors.rest;

import de.hsos.swa.actors.rest.dto.in.CreatePostRequestBody;
import de.hsos.swa.actors.rest.dto.in.validation.ErrorResponse;
import de.hsos.swa.actors.rest.dto.in.validation.ValidationService;
import de.hsos.swa.actors.rest.dto.out.PostDto;
import de.hsos.swa.application.annotations.Adapter;
import de.hsos.swa.application.input.CreatePostUseCase;
import de.hsos.swa.application.input.DeletePostUseCase;
import de.hsos.swa.application.input.GetFilteredPostsUseCase;
import de.hsos.swa.application.input.GetPostByIdUseCase;
import de.hsos.swa.application.input.dto.in.CreatePostCommand;
import de.hsos.swa.application.input.dto.in.DeletePostCommand;
import de.hsos.swa.application.input.dto.in.GetFilteredPostQuery;
import de.hsos.swa.application.input.dto.in.GetPostByIdQuery;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.service.query.params.OrderParams;
import de.hsos.swa.application.service.query.params.PostFilterParams;
import de.hsos.swa.application.service.query.params.SortingParams;
import de.hsos.swa.domain.entity.Post;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.annotation.security.PermitAll;
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
import java.time.LocalDateTime;
import java.util.*;

// TODO: smallrye Metrics
// TODO: bei Delete NO_CONTENT falls Optional<Empty> siehe Topic
// TODO: Rest Assured für diesen Enpunkt
// TODO: Insomnia Collecion mit Tests für diesen ENpunkt
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("api/v1/posts")
@Transactional(value = Transactional.TxType.REQUIRES_NEW)
@Adapter
public class PostsRessource {
    // QUERIES
    @Inject
    GetPostByIdUseCase getPostByIdUseCase;
    @Inject
    GetFilteredPostsUseCase getFilteredPostsUseCase;

    // COMMANDS
    @Inject
    CreatePostUseCase createPostUseCase;
    @Inject
    DeletePostUseCase deletePostUseCase;
    @Inject
    ValidationService validationService;

    //------------------------------------------------------------------------------------------------------------------
    // GET
    @GET
    @PermitAll
    @Operation(summary = "getPosts", description = "Holt alle Posts, die den Query-Parametern entsprechen")
    @Tag(name = "Posts", description = "Zugriff auf die Posts")
    @Tag(name = "Posts", description = "Zugriff auf die Posts")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = SchemaType.ARRAY, name = "PostDto", implementation = PostDto.class)))
    })
    public Response getPosts(@DefaultValue("true") @QueryParam("includeComments") boolean includeComments,
                             @QueryParam("username") String username,
                             @QueryParam("userId") UUID userId,
                             @QueryParam("dateFrom") LocalDateTime dateFrom,
                             @QueryParam("dateTo") LocalDateTime dateTo,
                             @QueryParam("topic") String topic,
                             @QueryParam("topicId") UUID topicId,
                             @DefaultValue("VOTES") @QueryParam("sortBy") String sortBy,
                             @DefaultValue("DESC") @QueryParam("orderBy") String orderBy) {
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
            ApplicationResult<List<Post>> result = this.getFilteredPostsUseCase.getFilteredPosts(query);

            if (result.ok()) {
                List<PostDto> postsResponse = result.data().stream().map(PostDto.Converter::fromDomainEntity).toList();
                return Response.status(Response.Status.OK).entity(postsResponse).build();
            }

            return ErrorResponse.asResponseFromApplicationResult(result.status(), result.message());
        } catch (ConstraintViolationException e) {
            return ErrorResponse.asResponseFromConstraintViolation(e.getConstraintViolations());
        }
    }


    @GET
    @Path("{id}")
    @PermitAll
    @Operation(summary = "getPostById", description = "Holt den Post mit der übergebenen ID")
    @Tag(name = "Posts", description = "Zugriff auf die Posts")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(name = "PostDto", implementation = PostDto.class)))
    })
    public Response getPostById(@PathParam("id") String id,
                                @DefaultValue("true") @QueryParam("includeComments") boolean includeComments,
                                @DefaultValue("VOTES") @QueryParam("sortBy") String sortBy,
                                @DefaultValue("DESC") @QueryParam("orderBy") String orderBy) {
        try {
            GetPostByIdQuery query = new GetPostByIdQuery(id, includeComments, sortBy, orderBy);
            ApplicationResult<Post> result = this.getPostByIdUseCase.getPostById(query);

            if (result.ok()) {
                PostDto response = PostDto.Converter.fromDomainEntity(result.data());
                return Response.status(Response.Status.OK).entity(response).build();
            }

            return ErrorResponse.asResponseFromApplicationResult(result.status(), result.message());
        } catch (ConstraintViolationException e) {
            return ErrorResponse.asResponseFromConstraintViolation(e.getConstraintViolations());
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // POST
    @POST
    @RolesAllowed({"admin", "member"})
    @Operation(summary = "createPost", description = "Erstellt einen neuen Post")
    @Tag(name = "Posts", description = "Zugriff auf die Posts")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(name = "PostDto", implementation = PostDto.class)))
    })
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
            ApplicationResult<Post> result = this.createPostUseCase.createPost(command, username);
            if (result.ok()) {
                PostDto postResponse = PostDto.Converter.fromDomainEntity(result.data());
                return Response.status(Response.Status.OK).entity(postResponse).build();
            }
            return ErrorResponse.asResponseFromApplicationResult(result.status(), result.message());
        } catch (ConstraintViolationException e) {
            return ErrorResponse.asResponseFromConstraintViolation(e.getConstraintViolations());
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // DELETE
    @DELETE
    @Path("/{id}")
    @RolesAllowed({"member", "admin"})
    @Operation(summary = "deletePost", description = "Löscht den Post mit der übergebenen ID")
    @Tag(name = "Posts", description = "Zugriff auf die Posts")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(name = "PostDto", implementation = PostDto.class)))
    })
    public Response deletePost(@PathParam("id") String id, @Context SecurityContext securityContext) {
        try {
            String username = securityContext.getUserPrincipal().getName();
            DeletePostCommand command = new DeletePostCommand(id, username);
            ApplicationResult<Optional<Post>> result = this.deletePostUseCase.deletePost(command, username);

            if (result.ok()) {
                if (result.data().isPresent()) {
                    PostDto postDto = PostDto.Converter.fromDomainEntity(result.data().get());
                    return Response.status(Response.Status.OK).entity(postDto).build();
                }
                return Response.status(Response.Status.NO_CONTENT).build();
            }
            return ErrorResponse.asResponseFromApplicationResult(result.status(), result.message());
        } catch (ConstraintViolationException e) {
            return ErrorResponse.asResponseFromConstraintViolation(e.getConstraintViolations());
        }
    }
}
