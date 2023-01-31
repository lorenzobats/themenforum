package de.hsos.swa.actors.ui;

import de.hsos.swa.application.input.*;
import de.hsos.swa.application.input.dto.in.GetFilteredPostQuery;
import de.hsos.swa.application.input.dto.in.GetPostByIdQuery;
import de.hsos.swa.application.input.dto.in.SearchTopicsQuery;
import de.hsos.swa.application.input.dto.out.Result;
import de.hsos.swa.application.input.dto.out.TopicInputPortDto;
import de.hsos.swa.application.service.query.params.OrderParams;
import de.hsos.swa.application.service.query.params.PostFilterParams;
import de.hsos.swa.application.service.query.params.SortingParams;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.Post;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO Error Templates erstellen

@Path("/ui/")
@PermitAll
@Transactional(value = Transactional.TxType.REQUIRES_NEW)
public class PublicEndpoint {

    @Inject
    GetFilteredPostsUseCase getFilteredPostsUseCase;

    @Inject
    GetPostByIdUseCase getPostByIdUseCase;

    @Inject
    GetAllTopicsUseCase getAllTopicsUseCase;

    @Inject
    SearchTopicsUseCase searchTopicsUseCase;

    @Inject
    Logger log;

    @CheckedTemplate
    public static class Templates {

        // AUTH
        public static native TemplateInstance login();

        public static native TemplateInstance register();

        // TOPICS
        public static native TemplateInstance topics(List<TopicInputPortDto> allTopics, boolean isLoggedIn, String username);

        public static native TemplateInstance createTopic(String username);


        // POSTS
        public static native TemplateInstance posts(String topicTitle, List<Post> allPosts, boolean isLoggedIn, String username);

        public static native TemplateInstance post(Post post, boolean isLoggedIn, String username);

        public static native TemplateInstance createPost(List<TopicInputPortDto> allTopics, String username);


        // COMMENT
        public static native TemplateInstance comment(Comment comment, boolean isLoggedIn, String username);
    }


    @GET
    // TODO: Index
    public TemplateInstance index() {
        return Templates.login();
    }

    // AUTH
    @GET
    @Path("/login")
    public TemplateInstance login() {
        return Templates.login();
    }

    @GET
    @Path("/register")
    public TemplateInstance register() {
        return Templates.register();
    }

    // TOPICS
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/topics")
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
            Result<List<TopicInputPortDto>> searchedTopics = searchTopicsUseCase.searchTopics(new SearchTopicsQuery(searchString));
            return Templates.topics(searchedTopics.getData(), isLoggedIn, username);
        }
        Result<List<TopicInputPortDto>> allTopics = getAllTopicsUseCase.getAllTopics();
        return Templates.topics(allTopics.getData(), isLoggedIn, username);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/topics/new")
    @RolesAllowed({"admin", "member"})
    public TemplateInstance createTopic(@Context SecurityContext securityContext) {
        String username = "";
        if (securityContext.getUserPrincipal() != null) {
            username = securityContext.getUserPrincipal().getName();
        }
        Result<List<TopicInputPortDto>> allTopics = getAllTopicsUseCase.getAllTopics();
        return Templates.createTopic(username);
    }

    // POSTS
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/posts")
    @PermitAll
    public TemplateInstance posts(
            @QueryParam("topic") String topic,
            @QueryParam("username") String username,
            @DefaultValue("DATE") @QueryParam("sortBy") SortingParams sortBy,
            @DefaultValue("DESC") @QueryParam("orderBy") OrderParams orderBy,

            @Context SecurityContext securityContext) {
        boolean isLoggedIn = false;
        String principalUsername = "";

        if (securityContext.getUserPrincipal() != null) {
            principalUsername = securityContext.getUserPrincipal().getName();
            isLoggedIn = true;
        }

        Map<PostFilterParams, Object> filterParams = new HashMap<>();
        if (topic != null)
            filterParams.put(PostFilterParams.TOPIC, topic);
        if (username != null)   // Profiltemplate?
            filterParams.put(PostFilterParams.USERNAME, username);

        GetFilteredPostQuery request = new GetFilteredPostQuery(filterParams, true, sortBy, orderBy);
        Result<List<Post>> filteredPosts = getFilteredPostsUseCase.getFilteredPosts(request);

        if (filterParams.containsKey(PostFilterParams.TOPIC)) {
            return Templates.posts(String.valueOf(filterParams.get(PostFilterParams.TOPIC)), filteredPosts.getData(), isLoggedIn, principalUsername);
        }

        return Templates.posts(null, filteredPosts.getData(), isLoggedIn, principalUsername);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/posts/{id}")
    public TemplateInstance post(
            @PathParam("id") String id,
            @DefaultValue("VOTES") @QueryParam("sortBy") SortingParams sortBy,
            @DefaultValue("DESC") @QueryParam("orderBy") OrderParams orderBy,
            @Context SecurityContext securityContext) {
        boolean isLoggedIn = false;
        String username = "";
        if (securityContext.getUserPrincipal() != null) {
            username = securityContext.getUserPrincipal().getName();
            isLoggedIn = true;
        }

        GetPostByIdQuery request = new GetPostByIdQuery(id, true, sortBy, orderBy);
        Result<Post> postResult = getPostByIdUseCase.getPostById(request);

        if (postResult.isSuccessful()) {
            return Templates.post(postResult.getData(), isLoggedIn, username);
        }
        return Templates.post(null, isLoggedIn, username);  // TODO: Error laden
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/posts/new")
    @RolesAllowed({"admin", "member"})
    public TemplateInstance createPost(@Context SecurityContext securityContext) {
        String username = "";
        if (securityContext.getUserPrincipal() != null) {
            username = securityContext.getUserPrincipal().getName();
        }
        Result<List<TopicInputPortDto>> allTopics = getAllTopicsUseCase.getAllTopics();
        return Templates.createPost(allTopics.getData(), username);
    }

}
