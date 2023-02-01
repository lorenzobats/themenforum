package de.hsos.swa.actors.rest;


import de.hsos.swa.actors.rest.dto.in.RegisterUserRequestBody;
import de.hsos.swa.actors.rest.dto.in.validation.ValidationService;
import de.hsos.swa.actors.rest.dto.out.TopicDto;
import de.hsos.swa.actors.rest.dto.out.UserDto;
import de.hsos.swa.actors.rest.dto.in.validation.ValidationResult;
import de.hsos.swa.application.annotations.Adapter;
import de.hsos.swa.application.input.GetAllUsersUseCase;
import de.hsos.swa.application.input.GetUserByNameUseCase;
import de.hsos.swa.application.input.RegisterUserUseCase;
import de.hsos.swa.application.input.dto.in.GetUserByNameQuery;
import de.hsos.swa.application.input.dto.in.RegisterUserCommand;
import de.hsos.swa.application.input.dto.out.Result;
import de.hsos.swa.domain.entity.User;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import javax.annotation.security.PermitAll;
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
@Path("api/v1/users")
@Transactional(value = Transactional.TxType.REQUIRES_NEW)
@Adapter
public class UsersRessource {

    @Inject
    RegisterUserUseCase registerUserUseCase;

    @Inject
    GetUserByNameUseCase getUserByNameUseCase;

    @Inject
    GetAllUsersUseCase getAllUsersUseCase;

    @Inject
    ValidationService validationService;


    @GET
    @RolesAllowed({"admin"})
    @Operation(summary = "Holt alle User")
    public Response getAllUsers(@Context SecurityContext securityContext) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @GET
    @Path("{username}")
    @RolesAllowed({"admin"})
    @Operation(summary = "Holt den User mit dem übergebenen Username")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(name = "UserDto", implementation = UserDto.class)))
    })
    public Response getUserByName(@PathParam("username") String username, @Context SecurityContext securityContext) {
        try {
            Result<User> userResult = this.getUserByNameUseCase.getUserByName(new GetUserByNameQuery(username), securityContext);
            if (userResult.isSuccessful()) {
                UserDto responseDto = UserDto.Converter.fromDomainEntity(userResult.getData());
                return Response.status(Response.Status.OK).entity(responseDto).build();
            }
            return Response.status(Response.Status.BAD_REQUEST).entity(userResult.getMessage()).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }

    @POST
    @PermitAll
    @Operation(summary = "Erstellt einen neuen User")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(name = "UserDto", implementation = UserDto.class)))
    })
    public Response registerUser(
            @RequestBody(
                    description = "Post to create",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RegisterUserRequestBody.class))
            ) RegisterUserRequestBody request) {
        try {
            validationService.validate(request);
            RegisterUserCommand command = RegisterUserRequestBody.Converter.toInputPortCommand(request);
            Result<User> userResult = this.registerUserUseCase.registerUser(command);
            if (userResult.isSuccessful()) {
                UserDto responseDto = UserDto.Converter.fromDomainEntity(userResult.getData());
                return Response.status(Response.Status.OK).entity(responseDto).build();
            }
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(userResult.getMessage())).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }
}
