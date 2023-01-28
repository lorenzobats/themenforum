package de.hsos.swa.infrastructure.ui;

import de.hsos.swa.application.input.*;
import de.hsos.swa.application.input.dto.in.*;
import de.hsos.swa.application.use_case_query.PostFilterParams;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.Topic;
import de.hsos.swa.domain.entity.VoteType;
import de.hsos.swa.infrastructure.rest.VoteRestAdapter;
import de.hsos.swa.infrastructure.rest.dto.in.ReplyToCommentRestAdapterRequest;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.security.identity.SecurityIdentity;
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
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO Error Templates erstellen

@Path("/ui/public")
@PermitAll
@Transactional(value = Transactional.TxType.REQUIRES_NEW)
public class PublicEndpoint {

    @Inject
    GetAllPostsInputPort getAllPostsInputPort;

    @Inject
    GetFilteredPostsInputPort getFilteredPostsInputPort;

    @Inject
    GetPostByIdInputPort getPostByIdInputPort;

    @Inject
    GetAllTopicsInputPort getAllTopicsInputPort;

    @Inject
    GetTopicByIdInputPort getTopicByIdInputPort;

    @Inject
    CommentPostInputPort commentPostInputPort;

    @Inject
    ReplyToCommentInputPort replyToCommentInputPort;

    @Inject
    GetPostByCommentIdInputPort getPostByCommentIdInputPort;

    @Inject
    VotePostInputPort votePostInputPort;

    @Inject
    VoteCommentInputPort voteCommentInputPort;

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance posts(List<Post> allPosts, boolean isLoggedIn, String username);

        public static native TemplateInstance post(Post post, boolean isLoggedIn, String username);

        public static native TemplateInstance topics(List<Topic> allTopics, boolean isLoggedIn, String username);

        public static native TemplateInstance topic(Topic topic, List<Post> posts, boolean isLoggedIn, String username);

        public static native TemplateInstance comment(Comment comment, boolean isLoggedIn, String username);

    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/posts")
    @PermitAll
    public TemplateInstance posts(@QueryParam("topic") String topic, @Context SecurityContext securityContext) {
        boolean isLoggedIn = false;
        String username = "";
        if (securityContext.getUserPrincipal() != null) {
            username = securityContext.getUserPrincipal().getName();
            isLoggedIn = true;
        }

        if (topic != null) {
            Map<PostFilterParams, Object> filterParams = new HashMap<>();
            filterParams.put(PostFilterParams.TOPIC, topic);
            Result<List<Post>> filteredPosts = getFilteredPostsInputPort.getFilteredPosts(new GetFilteredPostInputPortRequest(filterParams, true));
            return Templates.posts(filteredPosts.getData(), isLoggedIn, username);
        }
        Result<List<Post>> allPosts = getAllPostsInputPort.getAllPosts(true);
        return Templates.posts(allPosts.getData(), isLoggedIn, username);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/posts/{id}")
    public TemplateInstance post(@PathParam("id") String id, @DefaultValue("true") @QueryParam("includeComments") boolean includeComments, @Context SecurityContext securityContext) {
        boolean isLoggedIn = false;
        String username = "";
        if (securityContext.getUserPrincipal() != null) {
            username = securityContext.getUserPrincipal().getName();
            isLoggedIn = true;
        }
        Result<Post> postResult = getPostByIdInputPort.getPostById(new GetPostByIdInputPortRequest(id, includeComments));
        if (postResult.isSuccessful()) {
            return Templates.post(postResult.getData(), isLoggedIn, username);
        }
        return Templates.post(null, isLoggedIn, username);
    }

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
        Result<List<Topic>> allTopics = getAllTopicsInputPort.getAllTopics();
        return Templates.topics(allTopics.getData(), isLoggedIn, username);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/topics/{id}")
    public TemplateInstance topic(@PathParam("id") String id, @Context SecurityContext securityContext) {
        boolean isLoggedIn = false;
        String username = "";
        if (securityContext.getUserPrincipal() != null) {
            username = securityContext.getUserPrincipal().getName();
            isLoggedIn = true;
        }
        Result<Topic> topicResult = getTopicByIdInputPort.getTopicById(new GetTopicByIdInputPortRequest(id));
        if (topicResult.isSuccessful()) {
            Map<PostFilterParams, Object> filterParams = new HashMap<>();
            filterParams.put(PostFilterParams.TOPIC, topicResult.getData().getTitle());
            Result<List<Post>> postsResult = getFilteredPostsInputPort.getFilteredPosts(new GetFilteredPostInputPortRequest(filterParams, true));

            if (postsResult.isSuccessful()) {
                return Templates.topic(topicResult.getData(), postsResult.getData(), isLoggedIn, username);
            }
        }
        return Templates.topic(null, null, isLoggedIn, username);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/posts/{id}/upvote")
    @RolesAllowed({"admin", "member"})
    public Response upvotePost(@PathParam("id") String postId, @Context SecurityContext securityContext) {
        String username = securityContext.getUserPrincipal().getName();
        Result<Post> postResult = this.votePostInputPort.votePost(new VotePostInputPortRequest(postId, username, VoteType.UP));

        if(postResult.isSuccessful()) {
            return Response.status(301).location(URI.create("/ui/public/posts/")).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/posts/{id}/downvote")
    @RolesAllowed({"admin", "member"})
    public Response downvotePost(@PathParam("id") String postId, @Context SecurityContext securityContext) {
        String username = securityContext.getUserPrincipal().getName();
        Result<Post> postResult = this.votePostInputPort.votePost(new VotePostInputPortRequest(postId, username, VoteType.DOWN));

        if(postResult.isSuccessful()) {
            return Response.status(301).location(URI.create("/ui/public/posts/")).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/posts/{id}/resetvote")
    @RolesAllowed({"admin", "member"})
    public Response resetVotePost(@PathParam("id") String postId, @Context SecurityContext securityContext) {
        String username = securityContext.getUserPrincipal().getName();
        Result<Post> postResult = this.votePostInputPort.votePost(new VotePostInputPortRequest(postId, username, VoteType.NONE));

        if(postResult.isSuccessful()) {
            return Response.status(301).location(URI.create("/ui/public/posts/")).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }


//    @GET
//    @Produces(MediaType.TEXT_HTML)
//    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//    @Path("/posts/{id}/upvotes")
//    @RolesAllowed({"admin", "member"})
//    public Response voteComment(@PathParam("id") String commentId) {
//
//    }

    @POST
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/comments/{id}")
    @RolesAllowed({"admin", "member"})
    public Response commentPost(@FormParam("text") String commentText, @PathParam("id") String postId, @Context SecurityContext securityContext) {
        String username = securityContext.getUserPrincipal().getName();

        Result<Comment> commentResult = this.commentPostInputPort.commentPost(new CommentPostInputPortRequest(postId, username, commentText));

        if (commentResult.isSuccessful()) {
            Result<Post> updatedPostResult = this.getPostByIdInputPort.getPostById(new GetPostByIdInputPortRequest(postId, true));

            if (updatedPostResult.isSuccessful()) {
                return Response.status(301).location(URI.create("/ui/public/posts/" + updatedPostResult.getData().getId())).build();
            }
        }
        return Response.status(Response.Status.BAD_REQUEST).location(URI.create("/ui/public/posts")).build();

    }

    @POST
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/replyTo/{commentId}")
    @RolesAllowed({"admin", "member"})
    public Response replyToComment(@FormParam("replytext") String replyText, @PathParam("commentId") String commentId, @Context SecurityContext securityContext) {
        String username = securityContext.getUserPrincipal().getName();
        Result<Comment> replyResult = this.replyToCommentInputPort.replyToComment(new ReplyToCommentInputPortRequest(commentId, username, replyText));

        if(!replyResult.isSuccessful()) {
            return Response.status(Response.Status.BAD_REQUEST).location(URI.create("/ui/public/posts")).build();
        }

        Result<Post> updatedPostResult = this.getPostByCommentIdInputPort.getPostByCommentId(new GetPostByCommentIdInputPortRequest(replyResult.getData().getId().toString()));

        if(updatedPostResult.isSuccessful()) {
            return Response.status(301).location(URI.create("/ui/public/posts/" + updatedPostResult.getData().getId())).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).location(URI.create("/ui/public/posts")).build();
    }
}
