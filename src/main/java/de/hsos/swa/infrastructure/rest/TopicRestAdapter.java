package de.hsos.swa.infrastructure.rest;

import de.hsos.swa.application.output.Result;
import de.hsos.swa.application.input.*;
import de.hsos.swa.application.input.dto.out.TopicWithPostCountDto;
import de.hsos.swa.application.input.dto.in.CreateTopicInputPortRequest;
import de.hsos.swa.application.input.dto.in.GetTopicByIdInputPortRequest;
import de.hsos.swa.domain.entity.Topic;
import de.hsos.swa.infrastructure.rest.dto.out.TopicDto;
import de.hsos.swa.infrastructure.rest.dto.in.CreateTopicRestAdapterRequest;
import de.hsos.swa.infrastructure.rest.validation.TopicValidationService;
import de.hsos.swa.infrastructure.rest.validation.ValidationResult;
import org.jboss.logging.Logger;

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
@Path("/topics")
@Transactional(value = Transactional.TxType.REQUIRES_NEW)


//TODO AdapterRequests validieren

public class TopicRestAdapter {

    @Inject
    GetAllTopicsInputPort getAllTopicsInputPort;

    @Inject
    GetAllTopicsWithPostCountInputPort getAllTopicsWithPostCountInputPort;

    @Inject
    GetTopicByIdInputPort getTopicByIdInputPort;

    @Inject
    CreateTopicInputPort createTopicInputPort;

    @Inject
    Logger log;

    @Inject
    TopicValidationService validationService;


    @GET
    public Response getAllTopics() {
        try {
                Result<List<TopicWithPostCountDto>> topicsResult = this.getAllTopicsWithPostCountInputPort.getAllTopics();
                if (topicsResult.isSuccessful()) {
                    List<TopicDto> topicsResponse = topicsResult.getData().stream().map(TopicDto.Converter::toDto).toList();
                    return Response.status(Response.Status.OK).entity(topicsResponse).build();
                }
                return Response.status(Response.Status.NOT_FOUND).entity(new ValidationResult(topicsResult.getErrorMessage())).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }

    @GET
    @Path("{id}")
    public Response getTopicById(@PathParam("id") String id) {
        try {
            GetTopicByIdInputPortRequest query = new GetTopicByIdInputPortRequest(id);
            Result<Topic> topicResult = this.getTopicByIdInputPort.getTopicById(query);
            if (topicResult.isSuccessful()) {
                TopicDto response = TopicDto.Converter.toDto(topicResult.getData());
                return Response.status(Response.Status.OK).entity(response).build();
            }
            return Response.status(Response.Status.NOT_FOUND).entity(new ValidationResult(topicResult.getErrorMessage())).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }

    @POST
    @RolesAllowed("member")
    public Response createTopic(CreateTopicRestAdapterRequest request, @Context SecurityContext securityContext) {
        try {
            validationService.validateTopic(request);
            String username = securityContext.getUserPrincipal().getName();
            CreateTopicInputPortRequest command = CreateTopicRestAdapterRequest.Converter.toInputPort(request, username);
            Result<Topic> topicResult = this.createTopicInputPort.createTopic(command);

            if (topicResult.isSuccessful()) {
                TopicDto topicResponse = TopicDto.Converter.toDto(topicResult.getData());
                // TODO: Neben Body auch Uri Builder nutzen um RessourceLink im Header zurückzugeben (Gilt für alle POST/UPDATE)
                return Response.status(Response.Status.CREATED).entity(topicResponse).build();
            }
            return Response.status(Response.Status.BAD_REQUEST).entity(topicResult.getErrorMessage()).build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ValidationResult(e.getConstraintViolations())).build();
        }
    }
}
