package de.hsos.swa.actors.rest;

import de.hsos.swa.actors.rest.dto.in.RegisterUserRequestBody;
import de.hsos.swa.actors.rest.dto.in.validation.ValidationService;
import de.hsos.swa.actors.rest.dto.out.UserDto;
import de.hsos.swa.actors.rest.dto.in.validation.ErrorResponse;
import de.hsos.swa.application.annotations.Adapter;
import de.hsos.swa.application.input.GetUserByNameUseCase;
import de.hsos.swa.application.input.RegisterUserUseCase;
import de.hsos.swa.application.input.dto.in.GetUserByNameQuery;
import de.hsos.swa.application.input.dto.in.RegisterUserCommand;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.domain.entity.User;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.hibernate.annotations.Filter;

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
// TODO: smallrye Metrics
// TODO: bei Delete NO_CONTENT falls Optional<Empty> siehe Topic
// TODO: Rest Assured für diesen Enpunkt
// TODO: Insomnia Collecion mit Tests für diesen ENpunkt
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("api/v1/users")
@Transactional(value = Transactional.TxType.REQUIRES_NEW)
@Adapter
public class UsersRessource {


    // COMMANDS
    @Inject
    RegisterUserUseCase registerUserUseCase;

    // QUERIES
    @Inject
    GetUserByNameUseCase getUserByNameUseCase;

    // BEAN VALIDATION
    @Inject
    ValidationService validationService;

    //------------------------------------------------------------------------------------------------------------------
    // GET
    @GET
    @RolesAllowed({"admin"})
    @Tag(name = "Users", description = "Zugriff auf die Nutzer")
    @Filter(name = "testfilter")
    @Operation(summary = "getAllUsers", description = "Holt alle User")
    public Response getAllUsers(@Context SecurityContext securityContext) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @GET
    @Path("{username}")
    @RolesAllowed({"admin"})
    @Tag(name = "Users", description = "Zugriff auf die Nutzer")
    @Operation(summary = "getUserByName", description = "Holt den User mit dem übergebenen Username")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(name = "UserDto", implementation = UserDto.class)))
    })
    public Response getUserByName(@PathParam("username") String username, @Context SecurityContext securityContext) {
        try {
            GetUserByNameQuery query = new GetUserByNameQuery(username);
            ApplicationResult<User> result = this.getUserByNameUseCase.getUserByName(query, securityContext.getUserPrincipal().getName());
            if (result.ok()) {
                UserDto responseDto = UserDto.Converter.fromDomainEntity(result.data());
                return Response.status(Response.Status.OK).entity(responseDto).build();
            }
            return ErrorResponse.asResponseFromApplicationResult(result.status(), result.message());
        } catch (ConstraintViolationException e) {
            return ErrorResponse.asResponseFromConstraintViolation(e.getConstraintViolations());
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // POST
    @POST
    @PermitAll
    @Operation(summary = "registerUser", description = "Registriert einen neuen User")
    @Tag(name = "Registration", description = "Registration")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(name = "UserDto", implementation = UserDto.class)))
    })
    public Response registerUser(
            @RequestBody(
                    description = "User to register",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RegisterUserRequestBody.class))
            ) RegisterUserRequestBody request) {
        try {
            validationService.validate(request);
            RegisterUserCommand command = RegisterUserRequestBody.Converter.toInputPortCommand(request);
            ApplicationResult<User> result = this.registerUserUseCase.registerUser(command);
            if (result.ok()) {
                UserDto responseDto = UserDto.Converter.fromDomainEntity(result.data());
                return Response.status(Response.Status.OK).entity(responseDto).build();
            }
            return ErrorResponse.asResponseFromApplicationResult(result.status(), result.message());
        } catch (ConstraintViolationException e) {
            return ErrorResponse.asResponseFromConstraintViolation(e.getConstraintViolations());
        }
    }
}
