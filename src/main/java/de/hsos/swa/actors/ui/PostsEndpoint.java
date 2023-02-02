package de.hsos.swa.actors.ui;

import de.hsos.swa.application.input.*;
import de.hsos.swa.application.input.dto.in.GetFilteredPostQuery;
import de.hsos.swa.application.input.dto.in.GetPostByCommentIdQuery;
import de.hsos.swa.application.input.dto.in.GetPostByIdQuery;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.input.dto.out.TopicWithPostCountDto;
import de.hsos.swa.application.service.query.params.OrderParams;
import de.hsos.swa.application.service.query.params.PostFilterParams;
import de.hsos.swa.application.service.query.params.SortingParams;
import de.hsos.swa.domain.entity.Post;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/ui/posts")
@PermitAll
public class PostsEndpoint {

    @Inject
    GetFilteredPostsUseCase getFilteredPostsUseCase;

    @Inject
    GetPostByIdUseCase getPostByIdUseCase;

    @Inject
    GetAllTopicsUseCase getAllTopicsUseCase;

    @Inject
    GetPostByCommentIdUseCase getPostByCommentIdUseCase;

    @CheckedTemplate
    public static class Templates {

        public static native TemplateInstance posts(String topicTitle, List<Post> allPosts, boolean isLoggedIn, String username);

        public static native TemplateInstance post(Post post, boolean isLoggedIn, String username);

        public static native TemplateInstance createPost(List<TopicWithPostCountDto> allTopics, String username);
    }



    @GET
    @Produces(MediaType.TEXT_HTML)
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
        ApplicationResult<List<Post>> filteredPosts = getFilteredPostsUseCase.getFilteredPosts(request);

        if (filterParams.containsKey(PostFilterParams.TOPIC)) {
            return Templates.posts(String.valueOf(filterParams.get(PostFilterParams.TOPIC)), filteredPosts.getData(), isLoggedIn, principalUsername);
        }

        return Templates.posts(null, filteredPosts.getData(), isLoggedIn, principalUsername);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/{id}")
    @PermitAll
    public TemplateInstance post(
            @org.jboss.resteasy.annotations.jaxrs.PathParam("id") String id,
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
        ApplicationResult<Post> postResult = getPostByIdUseCase.getPostById(request);

        if (postResult.isSuccessful()) {
            return Templates.post(postResult.getData(), isLoggedIn, username);
        }
        return Templates.post(null, isLoggedIn, username);  // TODO: Error laden
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/comment/{id}")
    @PermitAll
    public TemplateInstance postByCommentId(@PathParam("id") String id, @Context SecurityContext securityContext) {
        boolean isLoggedIn = false;
        String username = "";
        if (securityContext.getUserPrincipal() != null) {
            username = securityContext.getUserPrincipal().getName();
            isLoggedIn = true;
        }

        ApplicationResult<Post> postResult = getPostByCommentIdUseCase.getPostByCommentId(new GetPostByCommentIdQuery(id));

        if (postResult.isSuccessful()) {
            return Templates.post(postResult.getData(), isLoggedIn, username);
        }
        return Templates.post(null, isLoggedIn, username);  // TODO: Error laden
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/new")
    @RolesAllowed({"admin", "member"})
    public TemplateInstance createPost(@Context SecurityContext securityContext) {
        String username = "";
        if (securityContext.getUserPrincipal() != null) {
            username = securityContext.getUserPrincipal().getName();
        }
        ApplicationResult<List<TopicWithPostCountDto>> allTopics = getAllTopicsUseCase.getAllTopics();
        return Templates.createPost(allTopics.getData(), username);
    }



}
