package de.hsos.swa.actors.rest;
import de.hsos.swa.actors.rest.dto.in.VoteEntityRestAdapterRequest;
import de.hsos.swa.actors.rest.dto.out.VoteDto;
import de.hsos.swa.actors.rest.validation.ValidationResult;
import de.hsos.swa.actors.rest.validation.VoteValidationService;
import de.hsos.swa.application.input.DeleteVoteInputPort;
import de.hsos.swa.application.input.GetAllVotesInputPort;
import de.hsos.swa.application.input.GetAllVotesByUsernameInputPort;
import de.hsos.swa.application.input.VoteEntityInputPort;
import de.hsos.swa.application.input.dto.in.DeleteVoteInputPortRequest;
import de.hsos.swa.application.input.dto.in.GetAllVotesByUsernameInputPortRequest;
import de.hsos.swa.application.input.dto.in.VoteEntityInputPortRequest;
import de.hsos.swa.application.input.dto.out.VoteInputPortDto;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Vote;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("api/v1/votes")
@Transactional(value = Transactional.TxType.REQUIRES_NEW)
public class VoteRestAdapter {

    @Inject
    VoteEntityInputPort voteEntityInputPort;

    @Inject
    DeleteVoteInputPort deleteVoteInputPort;

    @Inject
    GetAllVotesByUsernameInputPort getAllVotesByUsernameInputPort;

    @Inject
    GetAllVotesInputPort getAllVotesInputPort;

    @Inject
    VoteValidationService voteValidationService;


    @GET
    @RolesAllowed({"admin", "member"})
    public Response getAllVotes(@QueryParam("username") String username, @Context SecurityContext securityContext) {
        try {
            Result<List<VoteInputPortDto>> votesResult;
            if(username != null)
                votesResult = this.getAllVotesByUsernameInputPort.getAllVotesByUsername(new GetAllVotesByUsernameInputPortRequest(username), securityContext);
            else votesResult = this.getAllVotesInputPort.getAllVotes(securityContext);

            if (votesResult.isSuccessful()) {
                List<VoteDto> votesResponse = votesResult.getData().stream().map(VoteDto::fromInputPortDto).toList();
                return Response.status(Response.Status.OK).entity(votesResponse).build();
            }

            return Response.status(Response.Status.NOT_FOUND).entity(votesResult.getMessage()).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }

    @POST
    public Response vote(@NotNull VoteEntityRestAdapterRequest request, @Context SecurityContext securityContext) {
        try {
            voteValidationService.validateVote(request);
            String username = securityContext.getUserPrincipal().getName();
            VoteEntityInputPortRequest command = VoteEntityRestAdapterRequest.Converter.toInputPortCommand(request, username);

            Result<Vote> voteResult = this.voteEntityInputPort.vote(command);

            if (voteResult.isSuccessful()) {
                //TODO VoteDTO returnen
                return Response.status(Response.Status.OK).entity(voteResult.getData()).build();
            }
            return Response.status(Response.Status.BAD_REQUEST).entity("postResult.getMessage()").build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("member")
    public Response deleteVote(@PathParam("id") String id, @Context SecurityContext securityContext) {
        try {
            String username = securityContext.getUserPrincipal().getName();

            Result<Vote> voteResult = this.deleteVoteInputPort.deleteVote(new DeleteVoteInputPortRequest(id, username));

            if (voteResult.isSuccessful()) {
                return Response.status(Response.Status.OK).entity(voteResult.getData()).build();
            }

            return Response.status(Response.Status.BAD_REQUEST).entity(voteResult.getMessage()).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }
}
