package de.hsos.swa.infrastructure.rest;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("api/v1/votes")
@Transactional(value = Transactional.TxType.REQUIRES_NEW)
public class VoteRestAdapter {

    @GET
    // TODO: implementieren => nutze "GetVotedPostsByUserInputPort"
    @RolesAllowed({"admin", "member"})
    public Response getAllVotesByUser() {
        return Response.status(Response.Status.NOT_ACCEPTABLE).build();
    }
}
