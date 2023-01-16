package de.hsos.swa.adapter.input.rest;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/comments")
@Transactional(value = Transactional.TxType.REQUIRES_NEW)
public class CommentRestAdapter {

//    @Inject
//    GetCommentByIdInputPort getCommentByIdInputPort;

    // TODO: Post request zum erzeugen von Kommentaren (commentPostUseCase)


    @GET
    @Path("{id}")
    public Response getCommentById(@PathParam("id") String id) {
//        try {
//            Result<GetCommentByIdInputPortResponse> inputPortResult = this.getCommentByIdInputPort.getCommentById(new GetCommentByIdInputPortRequest(id));
//            if(inputPortResult.isSuccessful()) {
//                GetCommentByIdRestAdapterResponse response = GetCommentByIdRestAdapterResponse.Converter.fromUseCaseResult(inputPortResult.getData());
//                return Response.status(Response.Status.OK).entity(response).build();
//            }
//            return Response.status(Response.Status.NOT_FOUND).entity(new ValidationResult(inputPortResult.getErrorMessage())).build();
//        } catch (ConstraintViolationException e) {
//            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
//        }
        return Response.serverError().build();
    }
}
