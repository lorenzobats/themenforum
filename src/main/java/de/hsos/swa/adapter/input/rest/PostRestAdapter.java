package de.hsos.swa.adapter.input.rest;

import de.hsos.swa.adapter.input.rest.createPost.CreatePostRestAdapterRequest;
import de.hsos.swa.adapter.input.rest.createPost.CreatePostRestAdapterResponse;
import de.hsos.swa.application.port.input._shared.Result;
import de.hsos.swa.application.port.input.createPost.CreatePostInputPort;
import de.hsos.swa.application.port.input.createPost.CreatePostInputPortRequest;
import de.hsos.swa.application.port.input.createPost.CreatePostInputPortResponse;


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
}
