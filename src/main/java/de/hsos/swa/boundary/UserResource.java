package de.hsos.swa.boundary;


import de.hsos.swa.boundary.dto.UserCreationDto;
import de.hsos.swa.control.UserManagement;
import de.hsos.swa.entity.User;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/users")
@Transactional(value = Transactional.TxType.REQUIRES_NEW)
// @RolesAllowed("registeredUser")
public class UserResource {
    @Inject
    UserManagement userManagement;

    @POST
    public Response createUser(UserCreationDto userDto) {
        // TODO: Validierung (Nutzername existiert bereits)
        if(!userManagement.usernameExists(userDto.username)) {
            Optional<User> user = userManagement.createUser(UserCreationDto.Converter.toRegistrationDto(userDto));
            if(user.isPresent()) {
                return Response.status(Response.Status.CREATED).entity(user.get()).build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
