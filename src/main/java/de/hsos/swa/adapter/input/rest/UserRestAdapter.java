package de.hsos.swa.adapter.input.rest;


import de.hsos.swa.adapter.input.rest.getUserByName.GetUserByNameRestAdapterResponse;
import de.hsos.swa.adapter.input.rest.registerUser.RegisterUserRestAdapterRequest;
import de.hsos.swa.adapter.input.rest.registerUser.RegisterUserRestAdapterResponse;
import de.hsos.swa.application.port.input.getUserByName.GetUserByNameInputPortRequest;
import de.hsos.swa.application.port.input.getUserByName.GetUserByNameInputPortResponse;
import de.hsos.swa.application.port.input.getUserByName.GetUserByNameInputPort;
import de.hsos.swa.application.port.input.registerUser.RegisterUserInputPortRequest;
import de.hsos.swa.application.port.input.registerUser.RegisterUserInputPortResponse;
import de.hsos.swa.application.port.input.registerUser.RegisterUserInputPort;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/users")
@Transactional(value = Transactional.TxType.REQUIRES_NEW)
public class UserRestAdapter {
    @Inject
    RegisterUserInputPort registerUserInputPort;
    @Inject
    GetUserByNameInputPort getUserByNameInputPort;

    @GET
    @Path("{name}")
    public Response getUserByName(@PathParam("name") String username) {
        try {
            GetUserByNameInputPortRequest command = new GetUserByNameInputPortRequest(username);
            GetUserByNameInputPortResponse result = this.getUserByNameInputPort.getUserByName(command);
            GetUserByNameRestAdapterResponse response = GetUserByNameRestAdapterResponse.Converter.fromUseCaseResult(result);
            return Response.status(Response.Status.OK).entity(response).build();
        } catch (ConstraintViolationException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(new Result(e.getConstraintViolations())).build();
        }
    }

    @POST
    public Response registerUser(RegisterUserRestAdapterRequest request) {
        try {
            RegisterUserInputPortRequest command = RegisterUserRestAdapterRequest.Converter.toUseCaseCommand(request);
            RegisterUserInputPortResponse result = this.registerUserInputPort.registerUser(command);
            // TODO: Check SUCCESS / ERROR / EXCEPTION
            RegisterUserRestAdapterResponse response = RegisterUserRestAdapterResponse.Converter.fromUseCaseResult(result);
            return Response.status(Response.Status.CREATED).entity(response).build();
        } catch (ConstraintViolationException e){
            return Response.status(Response.Status.BAD_REQUEST).entity(new Result(e.getConstraintViolations())).build();
        }
    }
}
