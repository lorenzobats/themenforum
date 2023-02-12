package de.hsos.swa.actors.ui;
import de.hsos.swa.application.input.query.GetAllVotesByUsernameUseCase;
import de.hsos.swa.application.input.query.GetCommentsByUserUseCase;
import de.hsos.swa.application.input.query.GetFilteredPostsUseCase;
import de.hsos.swa.application.input.query.SearchTopicsUseCase;
import de.hsos.swa.application.input.dto.in.GetAllVotesByUsernameQuery;
import de.hsos.swa.application.input.dto.in.GetCommentsByUserQuery;
import de.hsos.swa.application.input.dto.in.GetFilteredPostsQuery;
import de.hsos.swa.application.input.dto.in.SearchTopicsQuery;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.input.dto.out.TopicWithPostCountDto;
import de.hsos.swa.application.input.dto.out.VoteWithVotedEntityReference;
import de.hsos.swa.application.service.query.params.PostFilterParams;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.Post;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import org.eclipse.microprofile.openapi.annotations.Operation;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/ui")
@PermitAll
public class PublicEndpoint {

    @Inject
    SearchTopicsUseCase searchTopicsUseCase;

    @Inject
    GetAllVotesByUsernameUseCase getAllVotesByUsernameUseCase;

    @Inject
    GetCommentsByUserUseCase getCommentsByUserUseCase;

    @Inject
    GetFilteredPostsUseCase getFilteredPostsUseCase;

    @CheckedTemplate
    public static class Templates {

        public static native TemplateInstance index(boolean isLoggedIn, String username, boolean isAdmin);

        public static native TemplateInstance login();

        public static native TemplateInstance register();

        public static native TemplateInstance profile(
                List<TopicWithPostCountDto> topics,
                List<Post> posts,
                List<Comment> comments,
                List<VoteWithVotedEntityReference> votes,
                String username,
                String selection);

        public static native TemplateInstance error_login();
    }


    @GET
    @PermitAll
    @Operation(hidden = true)
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

    @GET
    @Path("/login")
    @Produces(MediaType.TEXT_HTML)
    @PermitAll
    @Operation(hidden = true)
    public TemplateInstance login() {
        return Templates.login();
    }

    @GET
    @Path("/error")
    @Produces(MediaType.TEXT_HTML)
    @PermitAll
    @Operation(hidden = true)
    public TemplateInstance error() {
        return Templates.error_login();
    }

    @GET
    @Path("/register")
    @Produces(MediaType.TEXT_HTML)
    @PermitAll
    @Operation(hidden = true)
    public TemplateInstance register() {
        return Templates.register();
    }


    // USER-PROFILE
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/me")
    @PermitAll
    @Operation(hidden = true)
    public TemplateInstance profile(@Context SecurityContext securityContext, @DefaultValue("topics") @QueryParam("active") String selection) {
        String username = "";
        if (securityContext.getUserPrincipal() != null) {
            username = securityContext.getUserPrincipal().getName();
        }

        Map<PostFilterParams, Object> filterParams = new HashMap<>();
        if (username != null)
            filterParams.put(PostFilterParams.USERNAME, username);

        ApplicationResult<List<TopicWithPostCountDto>> topics = searchTopicsUseCase.searchTopics(new SearchTopicsQuery(username));
        ApplicationResult<List<Post>> posts = getFilteredPostsUseCase.getFilteredPosts(new GetFilteredPostsQuery(filterParams, false, "DATE", "DESC"));
        ApplicationResult<List<Comment>> comments = getCommentsByUserUseCase.getCommentsByUser(new GetCommentsByUserQuery(username));
        ApplicationResult<List<VoteWithVotedEntityReference>> votes = getAllVotesByUsernameUseCase.getAllVotesByUsername(new GetAllVotesByUsernameQuery(username), username);

        return Templates.profile(topics.data(), posts.data(), comments.data(), votes.data(), username, selection);
    }


}
