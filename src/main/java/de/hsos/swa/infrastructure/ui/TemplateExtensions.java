package de.hsos.swa.infrastructure.ui;

import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.Topic;
import io.quarkus.qute.TemplateExtension;
import io.quarkus.security.identity.SecurityIdentity;

import javax.inject.Inject;
import javax.ws.rs.core.SecurityContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

@TemplateExtension
public class TemplateExtensions {

    public static String parsedCreatedAtDate(Post post) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.uuuu");
        return post.getCreatedAt().format(formatter);
    }

    public static String parsedPostId(Post post) {
        return post.getId().toString();
    }

    public static String parsedCreatedAtDate(Topic topic) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.uuuu");
        return topic.getCreatedAt().format(formatter);
    }

    public static String parsedCreatedAtDate(Comment comment) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.uuuu");
        return comment.getCreatedAt().format(formatter);
    }

    public static List<Comment> commentsFlat(Post post) {
        List<Comment> flatComments = new ArrayList<>();
        Stack<Comment> stack = new Stack<>();
        stack.addAll(post.getComments());

        while (!stack.isEmpty()) {
            Comment currentComment = stack.pop();
            flatComments.add(currentComment);
            if (currentComment.getReplies() != null && !currentComment.getReplies().isEmpty()) {
                stack.addAll(currentComment.getReplies());
            }
        }
        return flatComments;
    }

}
