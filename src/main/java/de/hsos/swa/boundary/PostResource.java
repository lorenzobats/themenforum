package de.hsos.swa.boundary;


import de.hsos.swa.boundary.dto.PostCreationDto;
import de.hsos.swa.boundary.dto.PostDto;
import de.hsos.swa.boundary.validation.PostValidator;
import de.hsos.swa.boundary.validation.Result;
import de.hsos.swa.control.PostManagement;
import de.hsos.swa.entity.Post;

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
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/posts")
@Transactional(value = Transactional.TxType.REQUIRES_NEW)
public class PostResource {
    @Inject
    PostManagement management;

    @Inject
    PostValidator validator;

    @GET
    public Response getAllPosts() {
        Collection<Post> posts = management.getAllPosts();
        if (posts.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        Collection<PostDto> postDtos = posts.stream()
                .map(PostDto.Converter::toDto)
                .toList();
        return Response.status(Response.Status.OK).entity(postDtos).build();
    }

    @GET
    @Path("/{id}")
    public Response getPostById(@PathParam("id") String id) {
        Optional<Post> post = management.getPostById(UUID.fromString(id));
        if (post.isPresent()) {
            PostDto postDTO = PostDto.Converter.toDto(post.get());
            return Response.status(Response.Status.OK).entity(postDTO).build();
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @POST
    @RolesAllowed("member")
    public Response createPost(PostCreationDto postCreationDTO, @Context SecurityContext securityContext) {
        try {
            validator.validatePostCreationDTO(postCreationDTO);
            Optional<Post> post = management.createPost(PostCreationDto.Converter.toEntity(postCreationDTO), securityContext.getUserPrincipal().getName());
            if (post.isPresent()) {
                return Response.status(Response.Status.CREATED).entity(PostDto.Converter.toDto(post.get())).build();
            }
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new Result(e.getConstraintViolations())).build();
        }
    }

    @PUT
    @RolesAllowed("member")
    public Response updatePost(PostDto postDTO) {
        // TODO: Nur eigenen Post updaten
        try {
            validator.validatePostDTO(postDTO);
            Optional<Post> post = management.updatePost(PostDto.Converter.toEntity(postDTO));
            if (post.isPresent()) {
                return Response.status(Response.Status.CREATED).entity(PostDto.Converter.toDto(post.get())).build();
            }
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new Result(e.getConstraintViolations())).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("member")
    public Response deletePost(@PathParam("id") String id) {
        // TODO: Nur eigenen Post deleten
        if (management.deletePost(UUID.fromString(id))) {
            return Response.status(Response.Status.OK).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
