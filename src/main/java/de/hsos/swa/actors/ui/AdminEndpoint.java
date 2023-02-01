package de.hsos.swa.actors.ui;


import de.hsos.swa.application.annotations.Adapter;
import de.hsos.swa.application.input.GetAllCommentsUseCase;
import de.hsos.swa.application.input.GetAllTopicsUseCase;
import de.hsos.swa.application.input.GetAllUsersUseCase;
import de.hsos.swa.application.input.GetAllVotesUseCase;
import de.hsos.swa.application.input.dto.out.Result;
import de.hsos.swa.application.input.dto.out.TopicInputPortDto;
import de.hsos.swa.application.input.dto.out.VoteInputPortDto;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.User;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@Path("/ui/admin") // TODO: nach Refactor via Admin laufen lassen
@RolesAllowed("admin")
@Produces(MediaType.TEXT_HTML)
@Adapter
public class AdminEndpoint {

    //@Inject
    //GetAllTopicsUseCase getAllTopicsUseCase;

    //@Inject   // TODO: GetAllPostsUseCase
    //GetAllPostsUseCase getAllPostsUseCase;

    //@Inject
    //GetAllCommentsUseCase getAllCommentsUseCase;

    @Inject
    GetAllUsersUseCase getAllUsersUseCase;
    @Inject
    GetAllVotesUseCase getAllVotesUseCase;


    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance users(List<User> users, String username);
        public static native TemplateInstance votes(List<VoteInputPortDto> votes, String username);
    }


    // USERS
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/users")
    @RolesAllowed({"admin"})
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
}
