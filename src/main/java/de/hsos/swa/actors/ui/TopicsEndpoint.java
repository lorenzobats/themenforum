package de.hsos.swa.actors.ui;

import de.hsos.swa.actors.rest.dto.out.TopicDto;
import de.hsos.swa.application.input.query.GetAllTopicsUseCase;
import de.hsos.swa.application.input.query.SearchTopicsUseCase;
import de.hsos.swa.application.input.dto.in.SearchTopicsQuery;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.input.dto.out.TopicWithPostCountDto;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import org.eclipse.microprofile.openapi.annotations.Operation;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.ArrayList;
import java.util.List;

@Path("/ui/topics")
@PermitAll
public class TopicsEndpoint {

    @Inject
    SearchTopicsUseCase searchTopicsUseCase;

    @Inject
    GetAllTopicsUseCase getAllTopicsUseCase;

    @CheckedTemplate
    public static class Templates {

        public static native TemplateInstance topics(List<TopicDto> allTopics, boolean isLoggedIn, String username);

        public static native TemplateInstance createTopic(String username);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Operation(hidden = true)
    public TemplateInstance topics(
            @Context SecurityContext securityContext,
            @QueryParam("search") String searchString
    ) {
        boolean isLoggedIn = false;
        String username = "";
        if (securityContext.getUserPrincipal() != null) {
            username = securityContext.getUserPrincipal().getName();
            isLoggedIn = true;
        }
        if (searchString != null) {
            ApplicationResult<List<TopicWithPostCountDto>> searchedTopics = searchTopicsUseCase.searchTopics(new SearchTopicsQuery(searchString));
            return Templates.topics(searchedTopics.data().stream().map(TopicDto.Converter::fromInputPortDto).toList(), isLoggedIn, username);
        }
        ApplicationResult<List<TopicWithPostCountDto>> allTopics = getAllTopicsUseCase.getAllTopics();
        List<TopicDto> topicDtos = new ArrayList<>();
        if (allTopics.ok()) {
            topicDtos = allTopics.data().stream().map(TopicDto.Converter::fromInputPortDto).toList();
        }
        return Templates.topics(topicDtos, isLoggedIn, username);
    }

    @GET
    @Path("/new")
    @Produces(MediaType.TEXT_HTML)
    @RolesAllowed({"admin", "member"})
    @Operation(hidden = true)
    public TemplateInstance createTopic(@Context SecurityContext securityContext) {
        String username = "";
        if (securityContext.getUserPrincipal() != null) {
            username = securityContext.getUserPrincipal().getName();
        }
        ApplicationResult<List<TopicWithPostCountDto>> allTopics = getAllTopicsUseCase.getAllTopics();
        return Templates.createTopic(username);
    }
}
