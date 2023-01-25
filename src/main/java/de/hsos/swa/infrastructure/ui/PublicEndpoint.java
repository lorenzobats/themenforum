package de.hsos.swa.infrastructure.ui;

import de.hsos.swa.application.use_case_query.PostFilterParams;
import de.hsos.swa.application.input.GetAllPostsInputPort;
import de.hsos.swa.application.input.GetAllTopicsInputPort;
import de.hsos.swa.application.input.GetFilteredPostsInputPort;
import de.hsos.swa.application.input.GetTopicByIdInputPort;
import de.hsos.swa.application.input.dto.in.GetFilteredPostInputPortRequest;
import de.hsos.swa.application.input.dto.in.GetTopicByIdInputPortRequest;
import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.Topic;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Path("/ui/public")
@PermitAll
@Transactional(value = Transactional.TxType.REQUIRES_NEW)
public class PublicEndpoint {

    @Inject
    GetAllPostsInputPort getAllPostsInputPort;

    @Inject
    GetFilteredPostsInputPort getFilteredPostsInputPort;

    @Inject
    GetAllTopicsInputPort getAllTopicsInputPort;

    @Inject
    GetTopicByIdInputPort getTopicByIdInputPort;

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance home(List<Post> allPosts);
        public static native TemplateInstance topics(List<Topic> allTopics);
        public static native TemplateInstance topic(Topic topic, List<Post> posts);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/posts")
    public TemplateInstance home(@QueryParam("topic") String topic) {
        if(topic != null) {
            Map<PostFilterParams, Object> filterParams = new HashMap<>();
            filterParams.put(PostFilterParams.TOPIC, topic);
            Result<List<Post>> filteredPosts = getFilteredPostsInputPort.getFilteredPosts(new GetFilteredPostInputPortRequest(filterParams, false));
            return Templates.home(filteredPosts.getData());
        }
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

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/topics/{id}")
    public TemplateInstance topics(@PathParam("id") String id) {
        Result<Topic> topicResult = getTopicByIdInputPort.getTopicById(new GetTopicByIdInputPortRequest(id));
        if(topicResult.isSuccessful()) {
            Map<PostFilterParams, Object> filterParams = new HashMap<>();
            filterParams.put(PostFilterParams.TOPIC, topicResult.getData().getTitle());
            Result<List<Post>> postsResult = getFilteredPostsInputPort.getFilteredPosts(new GetFilteredPostInputPortRequest(filterParams, false));

            if(postsResult.isSuccessful()) {
                return Templates.topic(topicResult.getData(), postsResult.getData());
            }
        }
        return Templates.topic(null, null);
    }
}
