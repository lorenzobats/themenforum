package de.hsos.swa.infrastructure.ui;

import de.hsos.swa.application.input.dto.out.TopicWithPostCountDto;
import de.hsos.swa.domain.entity.Comment;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.Topic;
import de.hsos.swa.infrastructure.ui.dto.CommentWithDepth;
import io.quarkus.qute.TemplateExtension;

import java.time.format.DateTimeFormatter;
import java.util.*;

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

    public static String parsedCreatedAtDate(TopicWithPostCountDto topic) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.uuuu");
        return topic.createdAt.format(formatter);
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

    public static List<CommentWithDepth> commentsFlatWithDepth(Post post) {
        List<CommentWithDepth> flatComments = new ArrayList<>();
        Stack<CommentWithDepth> stack = new Stack<>();
        for (Comment comment : post.getComments()) {
            stack.push(new CommentWithDepth(comment, 0));
        }

        while (!stack.isEmpty()) {
            CommentWithDepth current = stack.pop();
            flatComments.add(current);

            if (current.object().getReplies() != null && !current.object().getReplies().isEmpty()) {
                for (Comment reply : current.object().getReplies()) {
                    stack.push(new CommentWithDepth(reply, current.depth() + 1));
                }
            }
        }
        return flatComments;
    }
}
