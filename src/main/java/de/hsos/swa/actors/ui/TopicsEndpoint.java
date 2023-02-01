package de.hsos.swa.actors.ui;

import de.hsos.swa.application.input.GetAllTopicsUseCase;
import de.hsos.swa.application.input.SearchTopicsUseCase;
import de.hsos.swa.application.input.dto.in.SearchTopicsQuery;
import de.hsos.swa.application.input.dto.out.Result;
import de.hsos.swa.application.input.dto.out.TopicWithPostCountDto;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;

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

        public static native TemplateInstance topics(List<TopicWithPostCountDto> allTopics, boolean isLoggedIn, String username);
        public static native TemplateInstance createTopic(String username);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
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
        if(searchString != null){
            Result<List<TopicWithPostCountDto>> searchedTopics = searchTopicsUseCase.searchTopics(new SearchTopicsQuery(searchString));
            return Templates.topics(searchedTopics.getData(), isLoggedIn, username);
        }
        Result<List<TopicWithPostCountDto>> allTopics = getAllTopicsUseCase.getAllTopics();
        return Templates.topics(allTopics.getData(), isLoggedIn, username);
    }

    @GET
    @Path("/new")
    @Produces(MediaType.TEXT_HTML)
    @RolesAllowed({"admin", "member"})
    public TemplateInstance createTopic(@Context SecurityContext securityContext) {
        String username = "";
        if (securityContext.getUserPrincipal() != null) {
            username = securityContext.getUserPrincipal().getName();
        }
        Result<List<TopicWithPostCountDto>> allTopics = getAllTopicsUseCase.getAllTopics();
        return Templates.createTopic(username);
    }
}
