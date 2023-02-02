package de.hsos.swa.actors.rest;

import de.hsos.swa.actors.rest.dto.in.CreateTopicRequestBody;
import de.hsos.swa.actors.rest.dto.in.validation.ValidationService;
import de.hsos.swa.actors.rest.dto.out.TopicDto;
import de.hsos.swa.actors.rest.dto.in.validation.ValidationResult;
import de.hsos.swa.application.annotations.Adapter;
import de.hsos.swa.application.input.dto.in.DeleteTopicCommand;
import de.hsos.swa.application.input.dto.in.SearchTopicsQuery;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.input.dto.out.TopicWithPostCountDto;
import de.hsos.swa.application.input.*;
import de.hsos.swa.application.input.dto.in.CreateTopicCommand;
import de.hsos.swa.application.input.dto.in.GetTopicByIdQuery;
import de.hsos.swa.domain.entity.Topic;
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
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

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

    @Inject
    ValidationService validationService;

    @GET
    @Operation(summary = "Holt alle Topics")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = SchemaType.ARRAY, name = "TopicDto", implementation = TopicDto.class)))
    })
    public Response getAllTopics(@QueryParam("search") String searchString) {
        try {

            ApplicationResult<List<TopicWithPostCountDto>> topicsResult;
            if (searchString != null)
                topicsResult = this.searchTopicsUseCase.searchTopics(new SearchTopicsQuery(searchString));
            else topicsResult = this.getAllTopicsUseCase.getAllTopics();

            if (topicsResult.isSuccessful()) {
                List<TopicDto> topicsResponse = topicsResult.getData().stream().map(TopicDto.Converter::fromInputPortDto).toList();
                return Response.status(Response.Status.OK).entity(topicsResponse).build();
            }
            return Response.status(Response.Status.NOT_FOUND).entity(topicsResult.getMessage()).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }

    @GET
    @Path("{id}")
    @Operation(summary = "Holt das Topic mit der übergebenen ID")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(name = "TopicDto", implementation = TopicDto.class)))
    })
    public Response getTopicById(@PathParam("id") String id) {
        try {
            GetTopicByIdQuery query = new GetTopicByIdQuery(id);
            ApplicationResult<Topic> topicResult = this.getTopicByIdUseCase.getTopicById(query);
            if (topicResult.isSuccessful()) {
                TopicDto response = TopicDto.Converter.fromDomainEntity(topicResult.getData());
                return Response.status(Response.Status.OK).entity(response).build();
            }
            return Response.status(Response.Status.NOT_FOUND).entity(new ValidationResult(topicResult.getMessage())).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }



    @POST
    @RolesAllowed({"member","admin"})
    @Operation(summary = "Erstellt ein neues Topic")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(name = "TopicDto", implementation = TopicDto.class)))
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
            CreateTopicCommand command = CreateTopicRequestBody.Converter.toInputPortCommand(request, username);
            ApplicationResult<Topic> topicResult = this.createTopicUseCase.createTopic(command);

            if (topicResult.isSuccessful()) {
                TopicDto topicResponse = TopicDto.Converter.fromDomainEntity(topicResult.getData());
                return Response.status(Response.Status.OK).entity(topicResponse).build();
            }
            return Response.status(Response.Status.BAD_REQUEST).entity(topicResult.getMessage()).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"admin"})
    @Operation(summary = "Löscht das Topic mit der übergebenen ID")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(name = "TopicDto", implementation = TopicDto.class)))
    })
    public Response deleteTopic(@PathParam("id") String id, @Context SecurityContext securityContext) {
        try {
            String username = securityContext.getUserPrincipal().getName();
            DeleteTopicCommand command = new DeleteTopicCommand(id, username);
            ApplicationResult<Topic> postResult = this.deleteTopicUseCase.deleteTopic(command);

            if (postResult.isSuccessful()) {
                TopicDto postDto = TopicDto.Converter.fromDomainEntity(postResult.getData());
                return Response.status(Response.Status.OK).entity(postDto).build();
            }
            return Response.status(Response.Status.BAD_REQUEST).entity(postResult.getMessage()).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }
}
