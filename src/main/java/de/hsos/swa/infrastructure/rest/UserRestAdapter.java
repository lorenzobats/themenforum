package de.hsos.swa.infrastructure.rest;


import de.hsos.swa.infrastructure.rest.dto.out.UserDto;
import de.hsos.swa.infrastructure.rest.dto.in.RegisterUserRestAdapterRequest;
import de.hsos.swa.infrastructure.rest.validation.ValidationResult;
import de.hsos.swa.application.output.Result;
import de.hsos.swa.application.input.dto.in.GetUserByNameInputPortRequest;
import de.hsos.swa.application.input.GetUserByNameInputPort;
import de.hsos.swa.application.input.dto.in.RegisterUserInputPortRequest;
import de.hsos.swa.application.input.RegisterUserInputPort;
import de.hsos.swa.domain.entity.User;
import de.hsos.swa.infrastructure.rest.validation.UserValidationService;

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
            Result<User> userResult = this.getUserByNameInputPort.getUserByName(new GetUserByNameInputPortRequest(username));
            if (userResult.isSuccessful()) {
                UserDto responseDto = UserDto.Converter.fromDomainEntity(userResult.getData());
                return Response.status(Response.Status.OK).entity(responseDto).build();
            }
            return Response.status(Response.Status.BAD_REQUEST).entity(userResult.getErrorMessage()).build();
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
            Result<User> userResult = this.registerUserInputPort.registerUser(command);
            if (userResult.isSuccessful()) {
                UserDto responseDto = UserDto.Converter.fromDomainEntity(userResult.getData());
                return Response.status(Response.Status.CREATED).entity(responseDto).build();
            }
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(userResult.getErrorMessage())).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }

    // TODO: UpdateUser

    // TODO: DeleteUser
}
