package de.hsos.swa.actors.rest;

import de.hsos.swa.actors.rest.dto.in.VoteEntityRequestBody;
import de.hsos.swa.actors.rest.dto.in.validation.ValidationService;
import de.hsos.swa.actors.rest.dto.out.VoteDto;
import de.hsos.swa.actors.rest.dto.in.validation.ErrorResponse;
import de.hsos.swa.application.annotations.Adapter;
import de.hsos.swa.application.input.DeleteVoteUseCase;
import de.hsos.swa.application.input.GetAllVotesUseCase;
import de.hsos.swa.application.input.GetAllVotesByUsernameUseCase;
import de.hsos.swa.application.input.VoteEntityUseCase;
import de.hsos.swa.application.input.dto.in.DeleteVoteCommand;
import de.hsos.swa.application.input.dto.in.GetAllVotesByUsernameQuery;
import de.hsos.swa.application.input.dto.in.VoteEntityCommand;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.input.dto.out.VoteWithVotedEntityReference;
import de.hsos.swa.domain.entity.Vote;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

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
import java.util.Optional;

@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("api/v1/votes")
@Transactional(value = Transactional.TxType.REQUIRES_NEW)
@Adapter
public class VotesRessource {

    // QUERIES
    @Inject
    GetAllVotesByUsernameUseCase getAllVotesByUsernameUseCase;

    @Inject
    GetAllVotesUseCase getAllVotesUseCase;


    // COMMANDS
    @Inject
    VoteEntityUseCase voteEntityUseCase;

    @Inject
    DeleteVoteUseCase deleteVoteUseCase;

    // BEAN VALIDATION
    @Inject
    ValidationService voteValidationService;

    //------------------------------------------------------------------------------------------------------------------
    // GET
    @GET
    @RolesAllowed({"admin", "member"})
    @Operation(summary = "getAllVotes", description = "Holt alle Votes")
    @Tag(name = "Votes", description = "Zugriff auf die Votes der Kommentare und Posts")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(type = SchemaType.ARRAY, name = "VoteDto", implementation = VoteDto.class)))
    })
    public Response getAllVotes(@QueryParam("username") String username, @Context SecurityContext securityContext) {
        try {
            ApplicationResult<List<VoteWithVotedEntityReference>> result;
            if (username != null)
                result = this.getAllVotesByUsernameUseCase.getAllVotesByUsername(new GetAllVotesByUsernameQuery(username), securityContext.getUserPrincipal().getName());
            else result = this.getAllVotesUseCase.getAllVotes(securityContext.getUserPrincipal().getName());

            if (result.ok()) {
                List<VoteDto> votesResponse = result.data().stream().map(VoteDto.Converter::fromInputPortDto).toList();
                return Response.status(Response.Status.OK).entity(votesResponse).build();
            }

            return ErrorResponse.asResponseFromApplicationResult(result.status(), result.message());
        } catch (ConstraintViolationException e) {
            return ErrorResponse.asResponseFromConstraintViolation(e.getConstraintViolations());
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // POST
    @POST
    @RolesAllowed({"admin", "member"})
    @Operation(summary = "vote", description = "Erstellt einen neuen Vote")
    @Tag(name = "Votes", description = "Zugriff auf die Votes der Kommentare und Posts")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(type = SchemaType.ARRAY, name = "VoteDto", implementation = VoteDto.class)))
    })
    public Response vote(@NotNull @RequestBody(
            description = "Vote to create",
            required = true,
            content = @Content(schema = @Schema(implementation = VoteEntityRequestBody.class))
    ) VoteEntityRequestBody request, @Context SecurityContext securityContext) {
        try {
            voteValidationService.validate(request);
            String username = securityContext.getUserPrincipal().getName();
            VoteEntityCommand command = VoteEntityRequestBody.Converter.toInputPortCommand(request);
            ApplicationResult<Vote> result = this.voteEntityUseCase.vote(command, username);

            if (result.ok()) {
                VoteDto voteDto = VoteDto.Converter.fromDomainEntity(result.data());
                return Response.status(Response.Status.OK).entity(voteDto).build();
            }
            return ErrorResponse.asResponseFromApplicationResult(result.status(), result.message());
        } catch (ConstraintViolationException e) {
            return ErrorResponse.asResponseFromConstraintViolation(e.getConstraintViolations());
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // DELETE
    @DELETE
    @Path("/{id}")
    @RolesAllowed({"admin", "member"})
    @Operation(summary = "deleteVote", description = "Löscht den Vote mit der übergebenen ID")
    @Tag(name = "Votes", description = "Zugriff auf die Votes der Kommentare und Posts")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(type = SchemaType.ARRAY, name = "VoteDto", implementation = VoteDto.class)))
    })
    public Response deleteVote(@PathParam("id") String id, @Context SecurityContext securityContext) {
        try {
            String username = securityContext.getUserPrincipal().getName();
            DeleteVoteCommand command = new DeleteVoteCommand(id);
            ApplicationResult<Optional<Vote>> result = this.deleteVoteUseCase.deleteVote(command, username);

            if (result.ok()) {
                if (result.data().isPresent()) {
                    VoteDto voteDto = VoteDto.Converter.fromDomainEntity(result.data().get());
                    return Response.status(Response.Status.OK).entity(voteDto).build();
                }
                return Response.status(Response.Status.NO_CONTENT).build();
            }
            return ErrorResponse.asResponseFromApplicationResult(result.status(), result.message());
        } catch (ConstraintViolationException e) {
            return ErrorResponse.asResponseFromConstraintViolation(e.getConstraintViolations());
        }
    }
}
