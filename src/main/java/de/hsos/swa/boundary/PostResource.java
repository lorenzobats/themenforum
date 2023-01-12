package de.hsos.swa.boundary;


import de.hsos.swa.boundary.dto.PostCreationDTO;
import de.hsos.swa.boundary.dto.PostDTO;
import de.hsos.swa.boundary.validation.PostValidator;
import de.hsos.swa.boundary.validation.Result;
import de.hsos.swa.control.PostManagement;
import de.hsos.swa.entity.Post;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/posts")
@Transactional(value = Transactional.TxType.REQUIRES_NEW)
// @RolesAllowed("registeredUser")
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
        Collection<PostDTO> postDTOS = posts.stream()
                .map(PostDTO.Converter::toDto)
                .toList();
        return Response.status(Response.Status.OK).entity(postDTOS).build();
    }

    @GET
    @Path("/{id}")
    // @RolesAllowed("admin")
    public Response getPostById(@PathParam("id") String id) {
        Optional<Post> post = management.getPostById(UUID.fromString(id));
        if (post.isPresent()) {
            PostDTO postDTO = PostDTO.Converter.toDto(post.get());
            return Response.status(Response.Status.OK).entity(postDTO).build();
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @POST
    // @PermitAll
    public Response postPost(PostCreationDTO postCreationDTO) {
        try {
            validator.validatePostCreationDTO(postCreationDTO);
            Optional<Post> post = management.createPost(PostCreationDTO.Converter.toEntity(postCreationDTO));
            if (post.isPresent()) {
                return Response.status(Response.Status.CREATED).entity(PostDTO.Converter.toDto(post.get())).build();
            }
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new Result(e.getConstraintViolations())).build();
        }
    }

    @PUT
    //@RolesAllowed({"post", "admin"})
    public Response putPost(PostDTO postDTO) {
        try {
            validator.validatePostDTO(postDTO);
            Optional<Post> post = management.updatePost(PostDTO.Converter.toEntity(postDTO));
            if (post.isPresent()) {
                return Response.status(Response.Status.CREATED).entity(PostDTO.Converter.toDto(post.get())).build();
            }
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new Result(e.getConstraintViolations())).build();
        }
    }

    @DELETE
    @Path("/{id}")
    //@RolesAllowed("admin")
    public Response deletePost(@PathParam("id") String id) {
        if (management.deletePost(UUID.fromString(id))) {
            return Response.status(Response.Status.OK).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
