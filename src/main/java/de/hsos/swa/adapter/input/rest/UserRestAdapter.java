package de.hsos.swa.adapter.input.rest;


import de.hsos.swa.adapter.input.rest.response.GetUserByNameRestAdapterResponse;
import de.hsos.swa.adapter.input.rest.request.RegisterUserRestAdapterRequest;
import de.hsos.swa.adapter.input.rest.response.RegisterUserRestAdapterResponse;
import de.hsos.swa.adapter.input.rest.validation.ValidationResult;
import de.hsos.swa.application.port.input._shared.Result;
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


    // TODO: GetUsers – Roles Admin

    @GET
    @Path("{name}")
    public Response getUserByName(@PathParam("name") String username) {
        try {
            GetUserByNameInputPortRequest command = new GetUserByNameInputPortRequest(username);
            Result<GetUserByNameInputPortResponse> result = this.getUserByNameInputPort.getUserByName(command);
            if (result.isSuccessful()) {
                GetUserByNameRestAdapterResponse response = GetUserByNameRestAdapterResponse.Converter.fromUseCaseResult(result.getData());
                return Response.status(Response.Status.OK).entity(response).build();
            }
            //Error in Datenstruktur wrappen
            return Response.status(Response.Status.BAD_REQUEST).entity(result.getErrorMessage()).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }

    @POST
    public Response registerUser(RegisterUserRestAdapterRequest request) {
        try {
            RegisterUserInputPortRequest command = RegisterUserRestAdapterRequest.Converter.toUseCaseCommand(request);
            Result<RegisterUserInputPortResponse> result = this.registerUserInputPort.registerUser(command);
            if (result.isSuccessful()) {
                RegisterUserRestAdapterResponse response = RegisterUserRestAdapterResponse.Converter.fromUseCaseResult(result.getData());
                return Response.status(Response.Status.CREATED).entity(response).build();
            }
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(result.getErrorMessage())).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }

    // TODO: UpdateUser
    // TODO: DeleteUser
}
