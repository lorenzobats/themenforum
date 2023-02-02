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
import de.hsos.swa.application.input.dto.out.VoteWithVotedEntityReferenceDto;
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
// TODO: smallrye Metrics
// TODO: bei Delete NO_CONTENT falls Optional<Empty> siehe Topic
// TODO: Rest Assured für diesen Enpunkt
// TODO: Insomnia Collecion mit Tests für diesen ENpunkt
// TODO: SecurityContext übergeben bei AuthentifizierungsMethoden
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


    @GET
    @RolesAllowed({"admin", "member"})
    @Operation(summary = "Holt alle Votes")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = SchemaType.ARRAY, name = "VoteDto", implementation = VoteDto.class)))
    })
    public Response getAllVotes(@QueryParam("username") String username, @Context SecurityContext securityContext) {
        try {
            ApplicationResult<List<VoteWithVotedEntityReferenceDto>> result;
            if (username != null)
                result = this.getAllVotesByUsernameUseCase.getAllVotesByUsername(new GetAllVotesByUsernameQuery(username), securityContext);
            else result = this.getAllVotesUseCase.getAllVotes(securityContext);

            if (result.ok()) {
                List<VoteDto> votesResponse = result.data().stream().map(VoteDto.Converter::fromInputPortDto).toList();
                return Response.status(Response.Status.OK).entity(votesResponse).build();
            }

            return ErrorResponse.asResponseFromAppplicationResult(result.status(), result.message());
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse(e.getConstraintViolations())).build();
        }
    }

    @POST
    @RolesAllowed({"admin", "member"})
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
            ApplicationResult<Vote> result = this.voteEntityUseCase.vote(command);      // TODO: + security Context

            if (result.ok()) {
                VoteDto voteDto = VoteDto.Converter.fromDomainEntity(result.data());
                return Response.status(Response.Status.OK).entity(voteDto).build();
            }
            return ErrorResponse.asResponseFromAppplicationResult(result.status(), result.message());
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse(e.getConstraintViolations())).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"admin", "member"})
    @Operation(summary = "Löscht den Vote mit der übergebenen ID")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = SchemaType.ARRAY, name = "VoteDto", implementation = VoteDto.class)))
    })
    public Response deleteVote(@PathParam("id") String id, @Context SecurityContext securityContext) {
        try {
            String username = securityContext.getUserPrincipal().getName();
            DeleteVoteCommand command = new DeleteVoteCommand(id, username);
            ApplicationResult<Vote> result = this.deleteVoteUseCase.deleteVote(command);    // TODO:  Security Context

            if (result.ok()) {
                VoteDto voteDto = VoteDto.Converter.fromDomainEntity(result.data());
                return Response.status(Response.Status.OK).entity(voteDto).build();
            }
            return ErrorResponse.asResponseFromAppplicationResult(result.status(), result.message());
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse(e.getConstraintViolations())).build();
        }
    }
}
