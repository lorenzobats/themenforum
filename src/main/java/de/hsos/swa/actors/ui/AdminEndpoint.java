package de.hsos.swa.actors.ui;


import de.hsos.swa.application.annotations.Adapter;
import de.hsos.swa.application.input.*;
import de.hsos.swa.application.input.dto.in.GetAllCommentsQuery;
import de.hsos.swa.application.input.dto.in.GetFilteredPostQuery;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.input.dto.out.TopicWithPostCountDto;
import de.hsos.swa.application.input.dto.out.VoteWithVotedEntityReferenceDto;
import de.hsos.swa.application.service.query.params.OrderParams;
import de.hsos.swa.application.service.query.params.PostFilterParams;
import de.hsos.swa.application.service.query.params.SortingParams;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.User;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import org.eclipse.microprofile.openapi.annotations.Operation;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO: users.html und votes.html fehlen noch
@Path("/ui/admin")
@RolesAllowed("admin")
@Produces(MediaType.TEXT_HTML)
@Adapter
public class AdminEndpoint {

    @Inject
    GetAllTopicsUseCase getAllTopicsUseCase;

    @Inject
    GetFilteredPostsUseCase getFilteredPostsUseCase;

    @Inject
    GetAllCommentsUseCase getAllCommentsUseCase;

    @Inject
    GetAllUsersUseCase getAllUsersUseCase;
    @Inject
    GetAllVotesUseCase getAllVotesUseCase;


    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance index(String adminname);
        public static native TemplateInstance topics(List<TopicWithPostCountDto> entries, String adminname);
        public static native TemplateInstance posts(List<Post> entries, String adminname);
        public static native TemplateInstance comments(List<Comment> entries, String adminname);
        public static native TemplateInstance users(List<User> entries, String adminname);
        public static native TemplateInstance votes(List<VoteWithVotedEntityReferenceDto> entries, String adminname);
    }



    @GET
    @RolesAllowed({"admin"})
    @Operation(hidden = true)
    public TemplateInstance index(@Context SecurityContext securityContext) {
        String adminName = adminName(securityContext);
        return Templates.index(adminName);
    }
    
    
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/topics")
    @RolesAllowed({"admin"})
    @Operation(hidden = true)
    public TemplateInstance topics(@Context SecurityContext securityContext) {
        String adminName = adminName(securityContext);
        ApplicationResult<List<TopicWithPostCountDto>> topics = getAllTopicsUseCase.getAllTopics();
        return Templates.topics(topics.data(), adminName);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/posts")
    @RolesAllowed({"admin"})
    @Operation(hidden = true)
    public TemplateInstance posts(@Context SecurityContext securityContext) {
        String adminName = adminName(securityContext);
        Map<PostFilterParams, Object> filterParams = new HashMap<>();
        ApplicationResult<List<Post>> posts = getFilteredPostsUseCase.getFilteredPosts(new GetFilteredPostQuery(filterParams, false, "DATE", "DESC"));
        return Templates.posts(posts.data(), adminName);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/comments")
    @RolesAllowed({"admin"})
    @Operation(hidden = true)
    public TemplateInstance comments(@Context SecurityContext securityContext) {
        String adminName = adminName(securityContext);
        GetAllCommentsQuery query = new GetAllCommentsQuery(false);
        ApplicationResult<List<Comment>> comments = getAllCommentsUseCase.getAllComments(query, adminName);
        return Templates.comments(comments.data(), adminName);
    }

    // USERS
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/users")
    @RolesAllowed({"admin"})
    @Operation(hidden = true)
    public TemplateInstance users(@Context SecurityContext securityContext) {
        String adminName = adminName(securityContext);
        ApplicationResult<List<User>> allUsers = getAllUsersUseCase.getAllUsers(securityContext);
        return Templates.users(allUsers.data(), adminName);
    }

    // VOTES
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/votes")
    @RolesAllowed({"admin"})
    @Operation(hidden = true)
    public TemplateInstance votes(@Context SecurityContext securityContext) {
        String adminName = adminName(securityContext);
        ApplicationResult<List<VoteWithVotedEntityReferenceDto>> allVotes = getAllVotesUseCase.getAllVotes(securityContext);
        return Templates.votes(allVotes.data(), adminName);
    }

    // UTILITY METHOD
    private static String adminName(SecurityContext securityContext) {
        String adminName = "";
        if (securityContext.getUserPrincipal() != null) {
            adminName = securityContext.getUserPrincipal().getName();
        }
        return adminName;
    }
}
