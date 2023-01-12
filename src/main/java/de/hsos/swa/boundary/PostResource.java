package de.hsos.swa.boundary;


import de.hsos.swa.boundary.dto.PostDTO;
import de.hsos.swa.boundary.validation.PostValidator;
import de.hsos.swa.control.PostManagement;
import de.hsos.swa.entity.Post;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

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


}
