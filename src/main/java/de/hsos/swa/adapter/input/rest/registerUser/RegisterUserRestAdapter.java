package de.hsos.swa.adapter.input.rest.registerUser;


import de.hsos.swa.application.port.input.registerUser.RegisterUserCommand;
import de.hsos.swa.application.port.input.registerUser.RegisterUserResult;
import de.hsos.swa.application.port.input.registerUser.RegisterUserUseCase;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/users")
@Transactional(value = Transactional.TxType.REQUIRES_NEW)
public class RegisterUserRestAdapter {
    @Inject
    RegisterUserUseCase registerUserUseCase;

    @POST
    public Response registerUser(RegisterUserRestAdapterRequest request) {
        RegisterUserCommand command = RegisterUserRestAdapterRequest.Converter.toUseCaseCommand(request);
        RegisterUserResult result = this.registerUserUseCase.registerUser(command);
        RegisterUserRestAdapterResponse response = RegisterUserRestAdapterResponse.Converter.fromUseCaseResult(result);
        return Response.status(Response.Status.CREATED).entity(response).build();
    }
}
