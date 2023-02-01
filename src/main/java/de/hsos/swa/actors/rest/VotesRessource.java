package de.hsos.swa.actors.rest;

import de.hsos.swa.actors.rest.dto.in.RegisterUserRequestBody;
import de.hsos.swa.actors.rest.dto.in.VoteEntityRequestBody;
import de.hsos.swa.actors.rest.dto.in.validation.ValidationService;
import de.hsos.swa.actors.rest.dto.out.UserDto;
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
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

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
    @Operation(summary = "Holt alle Votes")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = SchemaType.ARRAY, name = "VoteDto", implementation = VoteDto.class)))
    })
    public Response getAllVotes(@QueryParam("username") String username, @Context SecurityContext securityContext) {
        try {
            Result<List<VoteWithVotedEntityReferenceDto>> votesResult;
            if (username != null)
                votesResult = this.getAllVotesByUsernameUseCase.getAllVotesByUsername(new GetAllVotesByUsernameQuery(username), securityContext);
            else votesResult = this.getAllVotesUseCase.getAllVotes(securityContext);

            if (votesResult.isSuccessful()) {
                List<VoteDto> votesResponse = votesResult.getData().stream().map(VoteDto.Converter::fromInputPortDto).toList();
                return Response.status(Response.Status.OK).entity(votesResponse).build();
            }

            return Response.status(Response.Status.NOT_FOUND).entity(votesResult.getMessage()).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }

    @POST
    @Operation(summary = "Erstellt einen neuen Vote")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = SchemaType.ARRAY, name = "VoteDto", implementation = VoteDto.class)))
    })
    public Response vote(@NotNull @RequestBody(
            description = "Vote to create",
            required = true,
            content = @Content(schema = @Schema(implementation = VoteEntityRequestBody.class))
    ) VoteEntityRequestBody request, @Context SecurityContext securityContext) {
        try {
            voteValidationService.validate(request);
            String username = securityContext.getUserPrincipal().getName();
            VoteEntityCommand command = VoteEntityRequestBody.Converter.toInputPortCommand(request, username);

            Result<Vote> voteResult = this.voteEntityUseCase.vote(command);

            if (voteResult.isSuccessful()) {
                VoteDto voteDto = VoteDto.Converter.fromDomainEntity(voteResult.getData());
                return Response.status(Response.Status.OK).entity(voteDto).build();
            }
            return Response.status(Response.Status.BAD_REQUEST).entity("postResult.getMessage()").build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("member")
    @Operation(summary = "Löscht den Vote mit der übergebenen ID")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = SchemaType.ARRAY, name = "VoteDto", implementation = VoteDto.class)))
    })
    public Response deleteVote(@PathParam("id") String id, @Context SecurityContext securityContext) {
        try {
            String username = securityContext.getUserPrincipal().getName();

            Result<Vote> voteResult = this.deleteVoteUseCase.deleteVote(new DeleteVoteCommand(id, username));

            if (voteResult.isSuccessful()) {
                VoteDto voteDto = VoteDto.Converter.fromDomainEntity(voteResult.getData());
                return Response.status(Response.Status.OK).entity(voteDto).build();
            }
            return Response.status(Response.Status.BAD_REQUEST).entity(voteResult.getMessage()).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }
}
