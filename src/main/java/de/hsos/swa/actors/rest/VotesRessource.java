package de.hsos.swa.actors.rest;
import de.hsos.swa.actors.rest.dto.in.VoteEntityRequestBody;
import de.hsos.swa.actors.rest.dto.in.validation.ValidationService;
import de.hsos.swa.actors.rest.dto.out.VoteDto;
import de.hsos.swa.actors.rest.dto.in.validation.ValidationResult;
import de.hsos.swa.application.annotations.Adapter;
import de.hsos.swa.application.input.DeleteVoteUseCase;
import de.hsos.swa.application.input.GetAllVotesUseCase;
import de.hsos.swa.application.input.GetAllVotesByUsernameUseCase;
import de.hsos.swa.application.input.VoteEntityUseCase;
import de.hsos.swa.application.input.dto.in.DeleteVoteCommand;
import de.hsos.swa.application.input.dto.in.GetAllVotesByUsernameQuery;
import de.hsos.swa.application.input.dto.in.VoteEntityCommand;
import de.hsos.swa.application.input.dto.out.VoteWithVotedEntityReferenceDto;
import de.hsos.swa.application.input.dto.out.Result;
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
@Adapter
public class VotesRessource {

    @Inject
    VoteEntityUseCase voteEntityUseCase;

    @Inject
    DeleteVoteUseCase deleteVoteUseCase;

    @Inject
    GetAllVotesByUsernameUseCase getAllVotesByUsernameUseCase;

    @Inject
    GetAllVotesUseCase getAllVotesUseCase;

    @Inject
    ValidationService voteValidationService;


    @GET
    @RolesAllowed({"admin", "member"})
    public Response getAllVotes(@QueryParam("username") String username, @Context SecurityContext securityContext) {
        try {
            Result<List<VoteWithVotedEntityReferenceDto>> votesResult;
            if(username != null)
                votesResult = this.getAllVotesByUsernameUseCase.getAllVotesByUsername(new GetAllVotesByUsernameQuery(username), securityContext);
            else votesResult = this.getAllVotesUseCase.getAllVotes(securityContext);

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
    public Response vote(@NotNull VoteEntityRequestBody request, @Context SecurityContext securityContext) {
        try {
            voteValidationService.validate(request);
            String username = securityContext.getUserPrincipal().getName();
            VoteEntityCommand command = VoteEntityRequestBody.Converter.toInputPortCommand(request, username);

            Result<Vote> voteResult = this.voteEntityUseCase.vote(command);

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

            Result<Vote> voteResult = this.deleteVoteUseCase.deleteVote(new DeleteVoteCommand(id, username));

            if (voteResult.isSuccessful()) {
                return Response.status(Response.Status.OK).entity(voteResult.getData()).build();
            }

            return Response.status(Response.Status.BAD_REQUEST).entity(voteResult.getMessage()).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }
}
