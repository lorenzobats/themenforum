package de.hsos.swa.infrastructure.ui;

import de.hsos.swa.application.input.*;
import de.hsos.swa.application.input.dto.in.GetPostByIdInputPortRequest;
import de.hsos.swa.application.use_case_query.PostFilterParams;
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
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
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

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance posts(List<Post> allPosts);
        public static native TemplateInstance post(Post post);
        public static native TemplateInstance topics(List<Topic> allTopics);
        public static native TemplateInstance topic(Topic topic, List<Post> posts);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/posts")
    public TemplateInstance posts(@QueryParam("topic") String topic) {
        if(topic != null) {
            Map<PostFilterParams, Object> filterParams = new HashMap<>();
            filterParams.put(PostFilterParams.TOPIC, topic);
            Result<List<Post>> filteredPosts = getFilteredPostsInputPort.getFilteredPosts(new GetFilteredPostInputPortRequest(filterParams, false));
            return Templates.posts(filteredPosts.getData());
        }
        Result<List<Post>> allPosts = getAllPostsInputPort.getAllPosts(false);
        return Templates.posts(allPosts.getData());
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/posts/{id}")
    public TemplateInstance post(@PathParam("id") String id,@DefaultValue("true") @QueryParam("includeComments") boolean includeComments) {
        Result<Post> postResult = getPostByIdInputPort.getPostById(new GetPostByIdInputPortRequest(id, includeComments));
        if(postResult.isSuccessful()) {
            return Templates.post(postResult.getData());
        }

        return Templates.post(null);
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
    public TemplateInstance topic(@PathParam("id") String id) {
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
