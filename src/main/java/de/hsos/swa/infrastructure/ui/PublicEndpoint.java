package de.hsos.swa.infrastructure.ui;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.hsos.swa.application.input.*;
import de.hsos.swa.application.input.dto.in.*;
import de.hsos.swa.application.input.dto.out.TopicWithPostCountDto;
import de.hsos.swa.application.use_case_query.OrderParams;
import de.hsos.swa.application.use_case_query.PostFilterParams;
import de.hsos.swa.application.use_case_query.SortingParams;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.VoteType;
import de.hsos.swa.infrastructure.ui.dto.in.CommentPostUIRequest;
import de.hsos.swa.infrastructure.ui.dto.in.ReplyToCommentUIRequest;
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
import javax.ws.rs.core.Response;
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
    GetFilteredPostsInputPort getFilteredPostsInputPort;

    @Inject
    GetPostByIdInputPort getPostByIdInputPort;

    @Inject
    GetAllTopicsWithPostCountInputPort getAllTopicsWithPostCountInputPort;

    @Inject
    CommentPostInputPort commentPostInputPort;

    @Inject
    ReplyToCommentInputPort replyToCommentInputPort;

    @Inject
    VotePostInputPort votePostInputPort;

    @Inject
    VoteCommentInputPort voteCommentInputPort;

    @Inject
    DeletePostInputPort deletePostInputPort;


    @Inject
    DeleteCommentInputPort deleteCommentInputPort;


    @Inject
    Logger log;

    @CheckedTemplate
    public static class Templates {

        // AUTH
        public static native TemplateInstance login();

        public static native TemplateInstance register();

        // TOPICS
        public static native TemplateInstance topics(List<TopicWithPostCountDto> allTopics, boolean isLoggedIn, String username);


        // POSTS
        public static native TemplateInstance posts(String topicTitle, List<Post> allPosts, boolean isLoggedIn, String username);

        public static native TemplateInstance post(Post post, boolean isLoggedIn, String username);

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
    public TemplateInstance topics(@Context SecurityContext securityContext) {
        boolean isLoggedIn = false;
        String username = "";
        if (securityContext.getUserPrincipal() != null) {
            username = securityContext.getUserPrincipal().getName();
            isLoggedIn = true;
        }
        Result<List<TopicWithPostCountDto>> allTopics = getAllTopicsWithPostCountInputPort.getAllTopics();
        return Templates.topics(allTopics.getData(), isLoggedIn, username);
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

        GetFilteredPostInputPortRequest request = new GetFilteredPostInputPortRequest(filterParams, true, sortBy, orderBy);
        Result<List<Post>> filteredPosts = getFilteredPostsInputPort.getFilteredPosts(request);

        if(filterParams.containsKey(PostFilterParams.TOPIC)){
            return Templates.posts(String.valueOf(filterParams.get(PostFilterParams.TOPIC)), filteredPosts.getData(), isLoggedIn, principalUsername);
        }

        return Templates.posts(null, filteredPosts.getData(), isLoggedIn, principalUsername);
    }

    // POSTS
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

        GetPostByIdInputPortRequest request = new GetPostByIdInputPortRequest(id, true, sortBy, orderBy);
        Result<Post> postResult = getPostByIdInputPort.getPostById(request);

        if (postResult.isSuccessful()) {
            return Templates.post(postResult.getData(), isLoggedIn, username);
        }
        return Templates.post(null, isLoggedIn, username);  // TODO: Error laden
    }


    // ACTIONS
    @POST
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/comments/{id}")
    @RolesAllowed({"admin", "member"})
    public Response commentPost(@JsonProperty CommentPostUIRequest request, @PathParam("id") String postId, @Context SecurityContext securityContext) {
        String username = securityContext.getUserPrincipal().getName();
        Result<Comment> commentResult = this.commentPostInputPort.commentPost(new CommentPostInputPortRequest(postId, username, request.commentText()));

        if (!commentResult.isSuccessful()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/replyTo/{commentId}")
    @RolesAllowed({"admin", "member"})
    public Response replyToComment(@JsonProperty ReplyToCommentUIRequest request, @PathParam("commentId") String commentId, @Context SecurityContext securityContext) {
        String username = securityContext.getUserPrincipal().getName();
        Result<Comment> replyResult = this.replyToCommentInputPort.replyToComment(new ReplyToCommentInputPortRequest(commentId, username, request.replyText()));

        if (!replyResult.isSuccessful()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/posts/{id}/vote")
    @RolesAllowed({"admin", "member"})
    public Response votePost(@JsonProperty VoteType voteType, @PathParam("id") String postId, @Context SecurityContext securityContext) {
        String username = securityContext.getUserPrincipal().getName();
        Result<Post> postResult = this.votePostInputPort.votePost(new VotePostInputPortRequest(postId, username, voteType));

        if (postResult.isSuccessful()) {
            return Response.status(Response.Status.OK).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @POST
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/comments/{id}/vote")
    @RolesAllowed({"admin", "member"})
    public Response voteComment(@JsonProperty VoteType voteType, @PathParam("id") String postId, @Context SecurityContext securityContext) {
        String username = securityContext.getUserPrincipal().getName();
        Result<Comment> commentResult = this.voteCommentInputPort.voteComment(new VoteCommentInputPortRequest(postId, username, voteType));

        if (commentResult.isSuccessful()) {
            return Response.status(Response.Status.OK).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }


    @DELETE
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/posts/{id}")
    @RolesAllowed({"admin", "member"})
    public Response deletePost(@PathParam("id") String postId, @Context SecurityContext securityContext) {
        String username = securityContext.getUserPrincipal().getName();
        Result<Post> deletePostResult = this.deletePostInputPort.deletePost(new DeletePostInputPortRequest(postId, username));

        if (deletePostResult.isSuccessful()) {
            return Response.status(Response.Status.OK).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @DELETE
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/comments/{id}")
    @RolesAllowed({"admin", "member"})
    public Response deleteComment(@PathParam("id") String commentId, @Context SecurityContext securityContext) {
        String username = securityContext.getUserPrincipal().getName();
        Result<Comment> deleteCommentResult = this.deleteCommentInputPort.deleteComment(new DeleteCommentInputPortRequest(commentId, username));

        if (deleteCommentResult.isSuccessful()) {
            return Response.status(Response.Status.OK).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
