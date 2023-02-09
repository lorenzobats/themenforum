package de.hsos.swa.actors.rest;

import de.hsos.swa.actors.rest.dto.in.CreateTopicRequestBody;
import de.hsos.swa.actors.rest.dto.in.validation.ValidationService;
import de.hsos.swa.actors.rest.dto.out.TopicDto;
import de.hsos.swa.actors.rest.dto.in.validation.ErrorResponse;
import de.hsos.swa.application.annotations.Adapter;
import de.hsos.swa.application.input.command.CreateTopicUseCase;
import de.hsos.swa.application.input.command.DeleteTopicUseCase;
import de.hsos.swa.application.input.dto.in.DeleteTopicCommand;
import de.hsos.swa.application.input.dto.in.SearchTopicsQuery;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.input.dto.out.TopicWithPostCountDto;
import de.hsos.swa.application.input.dto.in.CreateTopicCommand;
import de.hsos.swa.application.input.dto.in.GetTopicByIdQuery;
import de.hsos.swa.application.input.query.GetAllTopicsUseCase;
import de.hsos.swa.application.input.query.GetTopicByIdUseCase;
import de.hsos.swa.application.input.query.SearchTopicsUseCase;
import de.hsos.swa.domain.entity.Topic;
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
@Path("api/v1/topics")
@Transactional(value = Transactional.TxType.REQUIRES_NEW)
@Adapter
public class TopicsRessource {

    // QUERIES
    @Inject
    GetAllTopicsUseCase getAllTopicsUseCase;
    @Inject
    SearchTopicsUseCase searchTopicsUseCase;
    @Inject
    GetTopicByIdUseCase getTopicByIdUseCase;

    // COMMANDS
    @Inject
    CreateTopicUseCase createTopicUseCase;
    @Inject
    DeleteTopicUseCase deleteTopicUseCase;

    // BEAN VALIDATION
    @Inject
    ValidationService validationService;

    //------------------------------------------------------------------------------------------------------------------
    // GET
    @GET
    @PermitAll
    @Tag(name = "Topics", description = "Zugriff auf die Themen")
    @Operation(summary = "getAllTopics", description = "Holt alle Topics")
    @Tag(name = "Topics", description = "Zugriff auf die Themen")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(type = SchemaType.ARRAY, name = "TopicDto", implementation = TopicDto.class)))
    })
    public Response getAllTopics(@QueryParam("search") String searchString) {
        try {
            ApplicationResult<List<TopicWithPostCountDto>> result;
            if (searchString != null)
                result = this.searchTopicsUseCase.searchTopics(new SearchTopicsQuery(searchString));
            else result = this.getAllTopicsUseCase.getAllTopics();

            if (result.ok()) {
                List<TopicDto> topicsResponse = result.data().stream().map(TopicDto.Converter::fromInputPortDto).toList();
                return Response.status(Response.Status.OK).entity(topicsResponse).build();
            }
            return ErrorResponse.fromApplicationResult(result.status(), result.message());
        } catch (ConstraintViolationException e) {
            return ErrorResponse.fromConstraintViolation(e.getConstraintViolations());
        }
    }

    @GET
    @Path("{id}")
    @PermitAll
    @Tag(name = "Topics", description = "Zugriff auf die Themen")
    @Operation(summary = "getTopicById", description = "Holt das Topic mit der übergebenen ID")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(name = "TopicDto", implementation = TopicDto.class)))
    })
    public Response getTopicById(@PathParam("id") String id) {
        try {
            GetTopicByIdQuery query = new GetTopicByIdQuery(id);
            ApplicationResult<Topic> result = this.getTopicByIdUseCase.getTopicById(query);
            if (result.ok()) {
                TopicDto response = TopicDto.Converter.fromDomainEntity(result.data());
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
    @RolesAllowed({"member","admin"})
    @Tag(name = "Topics", description = "Zugriff auf die Themen")
    @Operation(summary = "createTopic", description = "Erstellt ein neues Topic")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(name = "TopicDto", implementation = TopicDto.class)))
    })
    public Response createTopic(
            @RequestBody(
                    description = "Topic to create",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateTopicRequestBody.class))
            ) CreateTopicRequestBody request, @Context SecurityContext securityContext) {
        try {
            validationService.validate(request);
            String username = securityContext.getUserPrincipal().getName();
            CreateTopicCommand command = CreateTopicRequestBody.Converter.toInputPortCommand(request);
            ApplicationResult<Topic> result = this.createTopicUseCase.createTopic(command, username);
            if (result.ok()) {
                TopicDto topicResponse = TopicDto.Converter.fromDomainEntity(result.data());
                return Response.status(Response.Status.OK).entity(topicResponse).build();
            }
            return ErrorResponse.fromApplicationResult(result.status(), result.message());
        } catch (ConstraintViolationException e) {
            return ErrorResponse.fromConstraintViolation(e.getConstraintViolations());
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // DELETE
    @DELETE
    @Path("/{id}")
    @RolesAllowed({"admin"})
    @Tag(name = "Topics", description = "Zugriff auf die Themen")
    @Operation(summary = "deleteTopic", description = "Löscht das Topic mit der übergebenen ID")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(name = "TopicDto", implementation = TopicDto.class)))
    })
    public Response deleteTopic(@PathParam("id") String id, @Context SecurityContext securityContext) {
        try {
            String username = securityContext.getUserPrincipal().getName();
            DeleteTopicCommand command = new DeleteTopicCommand(id, username);
            ApplicationResult<Optional<Topic>> result = this.deleteTopicUseCase.deleteTopic(command, username);

            if (result.ok()) {
                if(result.data().isPresent()){
                    TopicDto postDto = TopicDto.Converter.fromDomainEntity(result.data().get());
                    return Response.status(Response.Status.OK).entity(postDto).build();
                }
                return Response.status(Response.Status.NO_CONTENT).build();
            }
            return ErrorResponse.fromApplicationResult(result.status(), result.message());
        } catch (ConstraintViolationException e) {
            return ErrorResponse.fromConstraintViolation(e.getConstraintViolations());
        }
    }
}
