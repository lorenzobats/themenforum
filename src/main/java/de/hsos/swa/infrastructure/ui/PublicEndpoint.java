package de.hsos.swa.infrastructure.ui;

import de.hsos.swa.application.input.GetAllPostsInputPort;
import de.hsos.swa.application.input.GetAllTopicsInputPort;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.Topic;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateExtension;
import io.quarkus.qute.TemplateInstance;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;


@Path("/ui/public")
@PermitAll
public class PublicEndpoint {

    @Inject
    GetAllPostsInputPort getAllPostsInputPort;

    @Inject
    GetAllTopicsInputPort getAllTopicsInputPort;

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance home(List<Post> allPosts);
        public static native TemplateInstance topics(List<Topic> allTopics);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/posts")
    public TemplateInstance home() {
        Result<List<Post>> allPosts = getAllPostsInputPort.getAllPosts(false);
        return Templates.home(allPosts.getData());
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/topics")
    public TemplateInstance topics() {
        Result<List<Topic>> allTopics = getAllTopicsInputPort.getAllTopics();
        return Templates.topics(allTopics.getData());
    }
}
