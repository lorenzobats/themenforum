package de.hsos.swa.actors.ui;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.hsos.swa.actors.ui.dto.in.CommentPostUIRequest;
import de.hsos.swa.actors.ui.dto.in.CreatePostUIRequest;
import de.hsos.swa.actors.ui.dto.in.ReplyToCommentUIRequest;
import de.hsos.swa.actors.ui.validation.UIValidationResult;
import de.hsos.swa.actors.ui.validation.UIValidationService;
import de.hsos.swa.application.input.*;
import de.hsos.swa.application.input.dto.in.*;
import de.hsos.swa.application.input.dto.out.TopicInputPortDto;
import de.hsos.swa.application.service.query.params.OrderParams;
import de.hsos.swa.application.service.query.params.PostFilterParams;
import de.hsos.swa.application.service.query.params.SortingParams;
import de.hsos.swa.application.input.dto.out.Result;
import de.hsos.swa.domain.entity.*;
import de.hsos.swa.domain.vo.VoteType;
import de.hsos.swa.domain.vo.VotedEntityType;
import de.hsos.swa.actors.ui.dto.in.CreateTopicUIRequest;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO Error Templates erstellen

@Path("/ui/")
@PermitAll
@Transactional(value = Transactional.TxType.REQUIRES_NEW)
public class PublicEndpoint {

    @Inject
    UIValidationService validationService;

    @Inject
    GetFilteredPostsUseCase getFilteredPostsUseCase;

    @Inject
    GetPostByIdUseCase getPostByIdUseCase;

    @Inject
    GetAllTopicsUseCase getAllTopicsUseCase;

    @Inject
    SearchTopicsUseCase searchTopicsUseCase;

    @Inject
    CommentPostUseCase commentPostUseCase;

    @Inject
    ReplyToCommentUseCase replyToCommentUseCase;

    @Inject
    VoteEntityUseCase voteEntityUseCase;

    @Inject
    DeletePostUseCase deletePostUseCase;


    @Inject
    DeleteCommentUseCase deleteCommentUseCase;

    @Inject
    CreatePostUseCase createPostUseCase;

    @Inject
    CreateTopicUseCase createTopicUseCase;


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
        // TODO Registrieren
        return Templates.login();
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



    // TODO: Alle raus und im HTML durch
    /////////////////////
    // ACTIONS
    /////////////////////

    // COMMENT
//    @POST
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Path("/comments/{id}")
//    @RolesAllowed({"admin", "member"})
//    public Response commentPost(@JsonProperty CommentPostUIRequest request, @PathParam("id") String postId, @Context SecurityContext securityContext) {
//        String username = securityContext.getUserPrincipal().getName();
//        Result<Comment> commentResult = this.commentPostUseCase.commentPost(new CommentPostCommand(postId, username, request.commentText));
//
//        if (!commentResult.isSuccessful()) {
//            return Response.status(Response.Status.BAD_REQUEST).build();
//        }
//
//        return Response.status(Response.Status.OK).build();
//    }
//
//    @POST
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Path("/replyTo/{commentId}")
//    @RolesAllowed({"admin", "member"})
//    public Response replyToComment(@JsonProperty ReplyToCommentUIRequest request, @PathParam("entityId") String commentId, @Context SecurityContext securityContext) {
//        String username = securityContext.getUserPrincipal().getName();
//        Result<Comment> replyResult = this.replyToCommentUseCase.replyToComment(new ReplyToCommentCommand(commentId, username, request.replyText));
//
//        if (!replyResult.isSuccessful()) {
//            return Response.status(Response.Status.BAD_REQUEST).build();
//        }
//
//        return Response.status(Response.Status.OK).build();
//    }
//
//    // VOTE
//    @POST
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Path("/posts/{id}/vote")
//    @RolesAllowed({"admin", "member"})
//    public Response votePost(@JsonProperty VoteType voteType, @PathParam("id") String postId, @Context SecurityContext securityContext) {
//        String username = securityContext.getUserPrincipal().getName();
//        Result<Vote> voteResult = this.voteEntityUseCase.vote(new VoteEntityCommand(postId, username, voteType, VotedEntityType.POST));
//
//        if (voteResult.isSuccessful()) {
//            return Response.status(Response.Status.OK).build();
//        }
//        return Response.status(Response.Status.BAD_REQUEST).build();
//    }
//
//    @POST
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Path("/comments/{id}/vote")
//    @RolesAllowed({"admin", "member"})
//    public Response voteComment(@JsonProperty VoteType voteType, @PathParam("id") String postId, @Context SecurityContext securityContext) {
//        String username = securityContext.getUserPrincipal().getName();
//        Result<Vote> voteResult = this.voteEntityUseCase.vote(new VoteEntityCommand(postId, username, voteType, VotedEntityType.COMMENT));
//
//        if (voteResult.isSuccessful()) {
//            return Response.status(Response.Status.OK).build();
//        }
//        return Response.status(Response.Status.BAD_REQUEST).build();
//    }
//
//    @POST
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Path("/posts/")
//    @RolesAllowed({"admin", "member"})
//    public Response createPost(CreatePostUIRequest request, @Context SecurityContext securityContext) {
//        try {
//            validationService.validateRequest(request);
//            String username = securityContext.getUserPrincipal().getName();
//            CreatePostCommand command = CreatePostUIRequest.Converter.toInputPortCommand(request, username);
//            Result<Post> postResult = this.createPostUseCase.createPost(command);
//
//            if (!postResult.isSuccessful()) {
//                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error").build();
//            }
//
//            Post post = postResult.getData();
//            return Response.status(Response.Status.CREATED).location(URI.create("/ui/posts/" + post.getId())).build();
//        } catch (ConstraintViolationException e) {
//            return Response.status(Response.Status.BAD_REQUEST).entity(new UIValidationResult(e.getConstraintViolations(), Response.Status.BAD_REQUEST, "/ui/posts/")).build();
//        }
//    }
//
//    @POST
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Path("/topics/")
//    @RolesAllowed({"admin", "member"})
//    public Response createTopic(CreateTopicUIRequest request, @Context SecurityContext securityContext) {
//        try {
//            validationService.validateRequest(request);
//            String username = securityContext.getUserPrincipal().getName();
//            CreateTopicCommand command = CreateTopicUIRequest.Converter.toInputPortCommand(request, username);
//            Result<Topic> topicResult = this.createTopicUseCase.createTopic(command);
//
//            if (!topicResult.isSuccessful()) {
//                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error").build();
//            }
//
//            Topic topic = topicResult.getData();
//            return Response.status(Response.Status.CREATED).location(URI.create("/ui/posts?topic=" + URLEncoder.encode(topic.getTitle(), StandardCharsets.UTF_8)))
//                    .build();
//        } catch (ConstraintViolationException e) {
//            return Response.status(Response.Status.BAD_REQUEST).entity(new UIValidationResult(e.getConstraintViolations(), Response.Status.BAD_REQUEST, "/ui/posts/")).build();
//        }
//    }
//
//    @DELETE
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Path("/posts/{id}")
//    @RolesAllowed({"admin", "member"})
//    public Response deletePost(@PathParam("id") String postId, @Context SecurityContext securityContext) {
//        String username = securityContext.getUserPrincipal().getName();
//        Result<Post> deletePostResult = this.deletePostUseCase.deletePost(new DeletePostCommand(postId, username));
//
//        if (deletePostResult.isSuccessful()) {
//            return Response.status(Response.Status.OK).build();
//        }
//        return Response.status(Response.Status.BAD_REQUEST).build();
//    }
//
//
//    @DELETE
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Path("/comments/{id}")
//    @RolesAllowed({"admin", "member"})
//    public Response deleteComment(@PathParam("id") String commentId, @Context SecurityContext securityContext) {
//        String username = securityContext.getUserPrincipal().getName();
//        Result<Comment> deleteCommentResult = this.deleteCommentUseCase.deleteComment(new DeleteCommentCommand(commentId, username));
//
//        if (deleteCommentResult.isSuccessful()) {
//            return Response.status(Response.Status.OK).build();
//        }
//        return Response.status(Response.Status.BAD_REQUEST).build();
//    }
}
