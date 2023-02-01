package de.hsos.swa.actors.ui;

import de.hsos.swa.application.input.*;
import de.hsos.swa.application.input.dto.in.*;
import de.hsos.swa.application.input.dto.out.Result;
import de.hsos.swa.application.input.dto.out.TopicInputPortDto;
import de.hsos.swa.application.input.dto.out.VoteInputPortDto;
import de.hsos.swa.application.service.query.params.OrderParams;
import de.hsos.swa.application.service.query.params.PostFilterParams;
import de.hsos.swa.application.service.query.params.SortingParams;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.User;
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
import java.util.ArrayList;
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
    GetAllCommentsUseCase getAllCommentsUseCase;

    @Inject
    SearchTopicsUseCase searchTopicsUseCase;

    @Inject
    GetAllVotesByUsernameUseCase getAllVotesByUsernameUseCase;

    @Inject
    GetAllVotesUseCase getAllVotesUseCase;

    @Inject
    GetAllUsersUseCase getAllUsersUseCase;

    @Inject
    GetUserByNameUseCase getUserByNameUseCase;

    @Inject
    GetCommentsByUserUseCase getCommentsByUserUseCase;

    @Inject
    Logger log;

    @CheckedTemplate
    public static class Templates {

        // INDEX
        public static native TemplateInstance index(boolean isLoggedIn, String username, boolean isAdmin);

        // AUTH
        public static native TemplateInstance login();

        public static native TemplateInstance register();

        // USER-PROFILE
        public static native TemplateInstance profile(
                List<TopicInputPortDto> topics,
                List<Post> posts,
                List<Comment> comments,
                List<VoteInputPortDto> votes,
                String username,
                String selection);


        // TOPICS
        public static native TemplateInstance topics(List<TopicInputPortDto> allTopics, boolean isLoggedIn, String username);

        public static native TemplateInstance createTopic(String username);


        // POSTS
        public static native TemplateInstance posts(String topicTitle, List<Post> allPosts, boolean isLoggedIn, String username);

        public static native TemplateInstance post(Post post, boolean isLoggedIn, String username);

        public static native TemplateInstance createPost(List<TopicInputPortDto> allTopics, String username);

        // USERS
        public static native TemplateInstance users(List<User> users, String username);

        // VOTES
        public static native TemplateInstance votes(List<VoteInputPortDto> votes, String username);

        // ERROR
        public static native TemplateInstance error();
    }


    // INDEX
    @GET
    @PermitAll
    public TemplateInstance index(@Context SecurityContext securityContext) {
        String username = "";
        boolean isLoggedIn = false;
        boolean isAdmin = false;
        if (securityContext.getUserPrincipal() != null) {
            isLoggedIn = true;
            username = securityContext.getUserPrincipal().getName();
            if(securityContext.isUserInRole("admin"))
                isAdmin = true;
        }
        return Templates.index(isLoggedIn, username, isAdmin);
    }

    // AUTH
    @GET
    @Path("/login")
    @Produces(MediaType.TEXT_HTML)
    @PermitAll
    public TemplateInstance login() {
        return Templates.login();
    }

    @GET
    @Path("/register")
    @Produces(MediaType.TEXT_HTML)
    @PermitAll
    public TemplateInstance register() {
        return Templates.register();
    }


    // USER-PROFILE
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/me")
    @RolesAllowed("member")
    public TemplateInstance profile(
            @Context SecurityContext securityContext,
            @DefaultValue("topics") @QueryParam("active") String selection) {
        String username = "";
        if (securityContext.getUserPrincipal() != null) {
            username = securityContext.getUserPrincipal().getName();
        }

        Map<PostFilterParams, Object> filterParams = new HashMap<>();
        if (username != null)
            filterParams.put(PostFilterParams.USERNAME, username);

        //Get topics by username
        Result<List<TopicInputPortDto>> topics = searchTopicsUseCase.searchTopics(new SearchTopicsQuery(username));

        //Get posts by username
        Result<List<Post>> posts = getFilteredPostsUseCase.getFilteredPosts(new GetFilteredPostQuery(filterParams, true, SortingParams.DATE, OrderParams.DESC));

        //Get Comments by username
        Result<List<Comment>> comments = getCommentsByUserUseCase.getCommentsByUser(new GetCommentsByUserQuery(username));

        //
        Result<List<VoteInputPortDto>> votes = getAllVotesByUsernameUseCase.getAllVotesByUsername(new GetAllVotesByUsernameQuery(username), securityContext);

        //
        return Templates.profile(topics.getData(), posts.getData(), comments.getData(), votes.getData(), username, selection);
    }

    // USERS
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/users")
    @RolesAllowed({"admin", "member"})
    public TemplateInstance users(@Context SecurityContext securityContext) {
        String username = "";
        if (securityContext.getUserPrincipal() != null) {
            username = securityContext.getUserPrincipal().getName();
        }
        Result<List<User>> allUsers = getAllUsersUseCase.getAllUsers(securityContext);
        return Templates.users(allUsers.getData(), username);
    }

    // VOTES
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/votes")
    @RolesAllowed({"admin"})
    public TemplateInstance votes(@Context SecurityContext securityContext) {
        String username = "";
        if (securityContext.getUserPrincipal() != null) {
            username = securityContext.getUserPrincipal().getName();
        }
        Result<List<VoteInputPortDto>> allVotes = getAllVotesUseCase.getAllVotes(securityContext);
        return Templates.votes(allVotes.getData(), username);
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
