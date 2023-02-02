package de.hsos.swa.actors.rest;

import de.hsos.swa.actors.rest.dto.in.CommentPostRequestBody;
import de.hsos.swa.actors.rest.dto.in.ReplyToCommentRequestBody;
import de.hsos.swa.actors.rest.dto.in.validation.ValidationService;
import de.hsos.swa.actors.rest.dto.out.CommentDto;
import de.hsos.swa.actors.rest.dto.in.validation.ErrorResponse;
import de.hsos.swa.application.annotations.Adapter;
import de.hsos.swa.application.input.*;
import de.hsos.swa.application.input.dto.in.*;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.domain.entity.Comment;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
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
import java.util.List;

// TODO: getAllComments mit includeComments = false dann werden votes nicht berechnet
// TODO: smallrye Metrics (auskommentiert, da teilweise gecrasht)
// TODO: bei Delete NO_CONTENT falls Optional<Empty> siehe Topic
// TODO: Rest Assured für diesen Enpunkt
// TODO: Insomnia Collecion mit Tests für diesen ENpunkt
// TODO: SecurityContext übergeben bei AuthentifizierungsMethoden
// TODO: Alle Request Bodys sinnvoll mit Annotationen zur Validierung versehen (swagger)
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("api/v1/comments")
@Transactional(value = Transactional.TxType.REQUIRES_NEW)
@Adapter
public class CommentsRessource {

    // QUERIES
    @Inject
    GetAllCommentsUseCase getAllCommentsUseCase;
    @Inject
    GetCommentByIdUseCase getCommentByIdUseCase;

    // COMMANDS
    @Inject
    CommentPostUseCase commentPostUseCase;
    @Inject
    ReplyToCommentUseCase replyToCommentUseCase;
    @Inject
    DeleteCommentUseCase deleteCommentUseCase;

    // BEAN VALIDATION
    @Inject
    ValidationService validationService;


    @GET
    @Path("{id}")
    @Operation(summary = "Holt das Kommentar mit der übergebenen ID")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(name = "CommentDto", implementation = CommentDto.class)))
    })
    //@Counted(name = "getCommentById", description = "Wie oft eine Kommentar anahand ID gesucht wurde")
    //@Timed(name = "getCommentByIdTimer", description = "Misst, wie lange es dauert ein Kommentar anhand ID zu suchen")
    @PermitAll
    public Response getCommentById(@PathParam("id") String id) {
        try {
            GetCommentByIdQuery query = new GetCommentByIdQuery(id);
            ApplicationResult<Comment> result = this.getCommentByIdUseCase.getCommentById(query);
            if (result.ok()) {
                CommentDto response = CommentDto.Converter.fromDomainEntity(result.data());
                return Response.status(Response.Status.OK).entity(response).build();
            }
            return ErrorResponse.asResponseFromAppplicationResult(result.status(), result.message());
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse(e.getConstraintViolations())).build();
        }
    }

    @GET
    @RolesAllowed({"admin", "member"})
    @Operation(summary = "Holt alle Kommentare")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = SchemaType.ARRAY, name = "CommentDto", implementation = CommentDto.class)))
    })
    //@Counted(name = "getAllComments", description = "Wie oft alle Kommentare abgerufen wurden")
    //@Timed(name = "getAllCommentsTimer", description = "Misst, wie lange es dauert alle Kommentar abzurufen")
    public Response getAllComments(@DefaultValue("true") @QueryParam("includeReplies") Boolean includeReplies) {
        try {
            ApplicationResult<List<Comment>> result = this.getAllCommentsUseCase.getAllComments(includeReplies);        // TODO: RequestDTO SCHREIBEN? // TODO: Security Context
            if (result.ok()) {
                List<CommentDto> commentsResponse = result.data().stream().map(CommentDto.Converter::fromDomainEntity).toList();
                return Response.status(Response.Status.OK).entity(commentsResponse).build();
            }
            return ErrorResponse.asResponseFromAppplicationResult(result.status(), result.message());
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse(e.getConstraintViolations())).build();
        }
    }

    @POST
    @RolesAllowed({"admin", "member"})
    @Operation(summary = "Erstellt ein neues Kommentar")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(name = "CommentDto", implementation = CommentDto.class)))
    })
    //@Counted(name = "postComment", description = "Wie oft ein Kommentar zu einem Post erstellt wurde")
    //@Timed(name = "postCommentTimer", description = "Misst, wie lange es dauert ein Kommentar zu einem Post zu erstellen")
    public Response commentPost(
            @RequestBody(
                    description = "Comment to create",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CommentPostRequestBody.class))
            ) CommentPostRequestBody request, @Context SecurityContext securityContext) {
        try {
            validationService.validate(request);
            String username = securityContext.getUserPrincipal().getName();
            CommentPostCommand command = CommentPostRequestBody.Converter.toInputPortCommand(request, username);
            ApplicationResult<Comment> result = this.commentPostUseCase.commentPost(command);   // TODO: + Security Context
            if (result.ok()) {
                CommentDto response = CommentDto.Converter.fromDomainEntity(result.data());
                return Response.status(Response.Status.OK).entity(response).build();
            }
            return ErrorResponse.asResponseFromAppplicationResult(result.status(), result.message());
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse(e.getConstraintViolations())).build();
        }
    }

    @POST
    @RolesAllowed({"admin", "member"})
    @Path("/{id}")
    @Operation(summary = "Erstellt eine Antwort auf das Kommentar mit übergebener ID")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(name = "CommentDto", implementation = CommentDto.class)))
    })
    //@Counted(name = "replyToComment", description = "Wie oft ein Kommentar geantwortet wurde")
    //@Timed(name = "replyToCommentTimer", description = "Misst, wie lange es dauert auf ein Kommentar zu antworten")
    public Response replyToComment(
            @PathParam("id") String id,
            @RequestBody(
                    description = "Reply to create",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ReplyToCommentRequestBody.class))
            ) ReplyToCommentRequestBody request, @Context SecurityContext securityContext) {
        try {
            validationService.validate(request);
            String username = securityContext.getUserPrincipal().getName();
            ReplyToCommentCommand command = ReplyToCommentRequestBody.Converter.toInputPortCommand(request, id, username);
            ApplicationResult<Comment> result = this.replyToCommentUseCase.replyToComment(command);         // TODO + Security Context
            if (result.ok()) {
                CommentDto response = CommentDto.Converter.fromDomainEntity(result.data());
                return Response.status(Response.Status.OK).entity(response).build();
            }
            return ErrorResponse.asResponseFromAppplicationResult(result.status(), result.message());
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse(e.getConstraintViolations())).build();
        }
    }

    @DELETE
    @Path("/{id}/")
    @RolesAllowed({"member", "admin"})
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = SchemaType.ARRAY, name = "CommentDto", implementation = CommentDto.class)))
    })
    @Operation(summary = "Löscht das Kommentar mit der übergebenen ID")
    //@Counted(name = "deleteToComment", description = "Wie oft ein Kommentar geloescht wurde")
    //@Timed(name = "deleteToCommentTimer", description = "Misst, wie lange es dauert einen Kommentar zu loeschen")
    public Response deleteComment(@PathParam("id") String id, @Context SecurityContext securityContext) {
        try {
            String username = securityContext.getUserPrincipal().getName();
            DeleteCommentCommand command = new DeleteCommentCommand(id, username);
            ApplicationResult<Comment> result = this.deleteCommentUseCase.deleteComment(command);   // TODO: + Security Context

            if (result.ok()) {
                CommentDto commentDto = CommentDto.Converter.fromDomainEntity(result.data());
                return Response.status(Response.Status.OK).entity(commentDto).build();
            }
            return ErrorResponse.asResponseFromAppplicationResult(result.status(), result.message());
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse(e.getConstraintViolations())).build();
        }
    }
}
