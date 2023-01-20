package de.hsos.swa.adapter.input.rest;


import de.hsos.swa.adapter.input.rest.validation.UserValidationService;
import de.hsos.swa.adapter.input.rest.user.response.GetUserByNameRestAdapterResponse;
import de.hsos.swa.adapter.input.rest.user.request.RegisterUserRestAdapterRequest;
import de.hsos.swa.adapter.input.rest.user.response.RegisterUserRestAdapterResponse;
import de.hsos.swa.adapter.input.rest.validation.ValidationResult;
import de.hsos.swa.application.port.input.Result;
import de.hsos.swa.application.port.input.request.GetUserByNameInputPortRequest;
import de.hsos.swa.application.port.input.GetUserByNameInputPort;
import de.hsos.swa.application.port.input.request.RegisterUserInputPortRequest;
import de.hsos.swa.application.port.input.RegisterUserInputPort;
import de.hsos.swa.domain.entity.User;

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

    @Inject
    UserValidationService validationService;

    @GET
    @Path("{username}")
    // --> String name
    // <-- GetUserByNameRestAdapterResponse
    public Response getUserByName(@PathParam("username") String username) {
        try {
            GetUserByNameInputPortRequest query = new GetUserByNameInputPortRequest(username);
            Result<User> result = this.getUserByNameInputPort.getUserByName(query);
            if (result.isSuccessful()) {
                GetUserByNameRestAdapterResponse response = GetUserByNameRestAdapterResponse.Converter.fromUseCaseResult(result.getData());
                return Response.status(Response.Status.OK).entity(response).build();
            }
            // TODO: Error in Datenstruktur wrappen (gilt für alle)
            return Response.status(Response.Status.BAD_REQUEST).entity(result.getErrorMessage()).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }

    @POST
    // --> RegisterUserRestAdapterRequest
    // <-- RegisterUserRestAdapterResponse
    public Response registerUser(RegisterUserRestAdapterRequest request) {
        try {
            validationService.validateUser(request);
            RegisterUserInputPortRequest command = RegisterUserRestAdapterRequest.Converter.toInputPortCommand(request);
            Result<User> result = this.registerUserInputPort.registerUser(command);
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
