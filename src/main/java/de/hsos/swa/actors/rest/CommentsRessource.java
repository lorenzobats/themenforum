package de.hsos.swa.actors.rest;

import de.hsos.swa.actors.rest.dto.in.CommentPostRequestBody;
import de.hsos.swa.actors.rest.dto.in.ReplyToCommentRequestBody;
import de.hsos.swa.actors.rest.dto.in.validation.ValidationService;
import de.hsos.swa.actors.rest.dto.out.CommentDto;
import de.hsos.swa.actors.rest.dto.in.validation.ErrorResponse;
import de.hsos.swa.application.annotations.Adapter;
import de.hsos.swa.application.input.command.CommentPostUseCase;
import de.hsos.swa.application.input.command.DeleteCommentUseCase;
import de.hsos.swa.application.input.command.ReplyToCommentUseCase;
import de.hsos.swa.application.input.dto.in.*;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.input.query.GetAllCommentsUseCase;
import de.hsos.swa.application.input.query.GetCommentByIdUseCase;
import de.hsos.swa.domain.entity.Comment;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

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
import java.util.Optional;

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


    //------------------------------------------------------------------------------------------------------------------
    // GET
    @GET
    @Operation(summary = "getAllComments", description = "Ermöglicht für den Zugriff auf Alle Kommentare")
    @PermitAll
    @Tag(name = "Comments", description = "Zugriff auf alle Kommentare für Admins")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(type = SchemaType.ARRAY, name = "CommentDto", implementation = CommentDto.class)))
    })
    @Counted(name = "getAllComments", description = "Wie oft alle Kommentare abgerufen wurden")
    @Timed(name = "getAllCommentsTimer", description = "Misst, wie lange es dauert alle Kommentar abzurufen")
    public Response getAllComments(@DefaultValue("true") @QueryParam("includeReplies") boolean includeReplies) {
        try {
            GetAllCommentsQuery query = new GetAllCommentsQuery(includeReplies);
            ApplicationResult<List<Comment>> result = this.getAllCommentsUseCase.getAllComments(query);
            if (result.ok()) {
                List<CommentDto> commentsResponse = result.data().stream().map(CommentDto.Converter::fromDomainEntity).toList();
                return Response.status(Response.Status.OK).entity(commentsResponse).build();
            }
            return ErrorResponse.fromApplicationResult(result.status(), result.message());
        } catch (ConstraintViolationException e) {
            return ErrorResponse.fromConstraintViolation(e.getConstraintViolations());
        }
    }

    @GET
    @Path("{id}")
    @PermitAll
    @Operation(summary = "getCommentById", description = "Holt das Kommentar mit der übergebenen ID")
    @Tag(name = "Comments", description = "Zugriff auf die Kommentare")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(name = "CommentDto", implementation = CommentDto.class)))
    })
    @Counted(name = "getCommentById", description = "Wie oft eine Kommentar anahand ID gesucht wurde")
    @Timed(name = "getCommentByIdTimer", description = "Misst, wie lange es dauert ein Kommentar anhand ID zu suchen")
    public Response getCommentById(@PathParam("id") String id) {
        try {
            GetCommentByIdQuery query = new GetCommentByIdQuery(id);
            ApplicationResult<Comment> result = this.getCommentByIdUseCase.getCommentById(query);
            if (result.ok()) {
                CommentDto response = CommentDto.Converter.fromDomainEntity(result.data());
                return Response.status(Response.Status.OK).entity(response).build();
            }
            return ErrorResponse.fromApplicationResult(result.status(), result.message());
        } catch (ConstraintViolationException e) {
            return ErrorResponse.fromConstraintViolation(e.getConstraintViolations());
        }
    }


    //------------------------------------------------------------------------------------------------------------------
    // POST
    @POST
    @RolesAllowed({"admin", "member"})
    @Operation(summary = "commentPost", description = "Erstellt ein neues Kommentar auf den Post mit der übergebenen ID")
    @Tag(name = "Comments", description = "Zugriff auf die Kommentare")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(name = "CommentDto", implementation = CommentDto.class)))
    })
    @Counted(name = "postComment", description = "Wie oft ein Kommentar zu einem Post erstellt wurde")
    @Timed(name = "postCommentTimer", description = "Misst, wie lange es dauert ein Kommentar zu einem Post zu erstellen")
    public Response commentPost(
            @RequestBody(
                    description = "Comment to create",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CommentPostRequestBody.class))
            ) CommentPostRequestBody request, @Context SecurityContext securityContext) {
        try {
            validationService.validate(request);
            String username = securityContext.getUserPrincipal().getName();
            CommentPostCommand command = CommentPostRequestBody.Converter.toInputPortCommand(request);
            ApplicationResult<Comment> result = this.commentPostUseCase.commentPost(command, username);
            if (result.ok()) {
                CommentDto response = CommentDto.Converter.fromDomainEntity(result.data());
                return Response.status(Response.Status.OK).entity(response).build();
            }
            return ErrorResponse.fromApplicationResult(result.status(), result.message());
        } catch (ConstraintViolationException e) {
            return ErrorResponse.fromConstraintViolation(e.getConstraintViolations());
        }
    }

    @POST
    @Path("/{id}")
    @RolesAllowed({"admin", "member"})
    @Operation(summary = "replyToComment", description = "Erstellt eine Antwort auf das Kommentar mit der übergebenen ID")
    @Tag(name = "Comments", description = "Zugriff auf die Kommentare")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(name = "CommentDto", implementation = CommentDto.class)))
    })
    @Counted(name = "replyToComment", description = "Wie oft ein Kommentar geantwortet wurde")
    @Timed(name = "replyToCommentTimer", description = "Misst, wie lange es dauert auf ein Kommentar zu antworten")
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
            ReplyToCommentCommand command = ReplyToCommentRequestBody.Converter.toInputPortCommand(request, id);
            ApplicationResult<Comment> result = this.replyToCommentUseCase.replyToComment(command, username);
            if (result.ok()) {
                CommentDto response = CommentDto.Converter.fromDomainEntity(result.data());
                return Response.status(Response.Status.OK).entity(response).build();
            }
            return ErrorResponse.fromApplicationResult(result.status(), result.message());
        } catch (ConstraintViolationException e) {
            return ErrorResponse.fromConstraintViolation(e.getConstraintViolations());
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // DELETE
    @DELETE
    @Path("/{id}/")
    @RolesAllowed({"member", "admin"})
    @Operation(summary = "deleteComment", description = "Löscht das Kommentar mit der übergebenen ID")
    @Tag(name = "Comments", description = "Zugriff auf die Kommentare")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = SchemaType.ARRAY, name = "CommentDto", implementation = CommentDto.class)))
    })
    @Counted(name = "deleteToComment", description = "Wie oft ein Kommentar geloescht wurde")
    @Timed(name = "deleteToCommentTimer", description = "Misst, wie lange es dauert einen Kommentar zu loeschen")
    public Response deleteComment(@PathParam("id") String id, @Context SecurityContext securityContext) {
        try {
            String username = securityContext.getUserPrincipal().getName();
            DeleteCommentCommand command = new DeleteCommentCommand(id);
            ApplicationResult<Optional<Comment>> result = this.deleteCommentUseCase.deleteComment(command, username);

            if (result.ok()) {
                if (result.data().isPresent()) {
                    CommentDto commentDto = CommentDto.Converter.fromDomainEntity(result.data().get());
                    return Response.status(Response.Status.OK).entity(commentDto).build();
                }
                return Response.status(Response.Status.NO_CONTENT).build();
            }
            return ErrorResponse.fromApplicationResult(result.status(), result.message());
        } catch (ConstraintViolationException e) {
            return ErrorResponse.fromConstraintViolation(e.getConstraintViolations());
        }
    }
}
