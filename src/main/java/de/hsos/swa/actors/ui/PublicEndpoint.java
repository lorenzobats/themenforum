package de.hsos.swa.actors.ui;
import de.hsos.swa.application.input.GetAllVotesByUsernameUseCase;
import de.hsos.swa.application.input.SearchTopicsUseCase;
import de.hsos.swa.application.input.dto.in.GetAllVotesByUsernameQuery;
import de.hsos.swa.application.input.dto.in.SearchTopicsQuery;
import de.hsos.swa.application.input.dto.out.Result;
import de.hsos.swa.application.input.dto.out.TopicInputPortDto;
import de.hsos.swa.application.input.dto.out.VoteInputPortDto;
import de.hsos.swa.domain.entity.Comment;
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
import java.util.ArrayList;
import java.util.List;


//TODO Error Templates erstellen
@Path("/ui/")
@PermitAll
public class PublicEndpoint {

    @Inject
    SearchTopicsUseCase searchTopicsUseCase;

    @Inject
    GetAllVotesByUsernameUseCase getAllVotesByUsernameUseCase;

    @CheckedTemplate
    public static class Templates {

        public static native TemplateInstance index(boolean isLoggedIn, String username, boolean isAdmin);

        public static native TemplateInstance login();

        public static native TemplateInstance register();

        public static native TemplateInstance profile(
                List<TopicInputPortDto> topics,
                List<Post> posts,
                List<Comment> comments,
                List<VoteInputPortDto> votes,
                String username,
                String selection);
    }


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
    @RolesAllowed("{member, admin}")
    public TemplateInstance profile(@Context SecurityContext securityContext, @DefaultValue("topics") @QueryParam("active") String selection) {
        String username = "";
        if (securityContext.getUserPrincipal() != null) {
            username = securityContext.getUserPrincipal().getName();
        }

        Result<List<TopicInputPortDto>> topics = searchTopicsUseCase.searchTopics(new SearchTopicsQuery(username));
        Result<List<Post>> posts = Result.success(new ArrayList<>()); // TODO: Get Topics By User (via FilteredTopicsUsecase mit include Comments=false
        Result<List<Comment>> comments = Result.success(new ArrayList<>()); // TODO: Get Comments By UsergetAllCommentsUseCase.getAllComments(false);
        Result<List<VoteInputPortDto>> votes = getAllVotesByUsernameUseCase.getAllVotesByUsername(new GetAllVotesByUsernameQuery(username), securityContext);

        return Templates.profile(topics.getData(), posts.getData(), comments.getData(), votes.getData(), username, selection);
    }

}
