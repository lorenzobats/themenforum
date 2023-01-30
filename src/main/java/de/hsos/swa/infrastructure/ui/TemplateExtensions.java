package de.hsos.swa.infrastructure.ui;

import de.hsos.swa.application.input.dto.out.TopicWithPostCountDto;
import de.hsos.swa.domain.entity.*;
import de.hsos.swa.domain.vo.VoteType;
import de.hsos.swa.infrastructure.ui.dto.out.CommentWithDepth;
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
        return topic.topic.getCreatedAt().format(formatter);
    }

    public static String parsedCreatedAtDate(Comment comment) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.uuuu");
        return comment.getCreatedAt().format(formatter);
    }

    public static Vote loggedInUserVote(Post post, String username) {
        for (Vote vote : post.getVotes()) {
            if(vote.getUser().getName().equals(username)){
                return vote;
            }
        }
        return null;
    }


    public static boolean loggedInUserCanDownvote(Post post, String username) {
        Vote vote = loggedInUserVote(post, username);
        return !post.getUser().getName().equals(username) &&
                (vote == null || vote.getVoteType() != VoteType.DOWN);

    }

    public static boolean loggedInUserCanUpvote(Post post, String username) {
        Vote vote = loggedInUserVote(post, username);
        return !post.getUser().getName().equals(username) &&
                (vote == null || vote.getVoteType() != VoteType.UP);
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
