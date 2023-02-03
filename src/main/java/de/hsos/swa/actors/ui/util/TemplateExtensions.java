package de.hsos.swa.actors.ui.util;

import de.hsos.swa.application.input.dto.out.TopicWithPostCountDto;
import de.hsos.swa.application.input.dto.out.VoteWithVotedEntityReference;
import de.hsos.swa.domain.entity.*;
import de.hsos.swa.domain.vo.VoteType;
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

    public static String parsedCreatedAtDate(VoteWithVotedEntityReference vote) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.uuuu");
        return vote.vote.getCreatedAt().format(formatter);
    }

    public static Vote loggedInUserVote(Post post, String username) {
        for (Vote vote : post.getVotes()) {
            if(vote.getUser().getName().equals(username)){
                return vote;
            }
        }
        return null;
    }

    public static int commentCount(Post post) {
        Deque<Comment> stack = new ArrayDeque<>(post.getComments());
        int count = 0;

        while(!stack.isEmpty()) {
            Comment comment = stack.pop();
            count++;
            stack.addAll(comment.getReplies());
        }
        return count;
    }

    public static Vote loggedInUserVote(CommentUIDto comment, String username) {
        for (Vote vote : comment.object().getVotes()) {
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

    public static boolean loggedInUserCanDownvote(CommentUIDto comment, String username) {
        Vote vote = loggedInUserVote(comment, username);
        return !comment.object().getUser().getName().equals(username) &&
                (vote == null || vote.getVoteType() != VoteType.DOWN);

    }

    public static boolean loggedInUserCanUpvote(Post post, String username) {
        Vote vote = loggedInUserVote(post, username);
        return !post.getUser().getName().equals(username) &&
                (vote == null || vote.getVoteType() != VoteType.UP);
    }

    public static boolean loggedInUserCanUpvote(CommentUIDto comment, String username) {
        Vote vote = loggedInUserVote(comment, username);
        return !comment.object().getUser().getName().equals(username) &&
                (vote == null || vote.getVoteType() != VoteType.UP);
    }

    public static String votedEntityString(VoteWithVotedEntityReference vote) {
        return String.valueOf(vote.votedEntityType);
    }

    public static List<CommentUIDto> commentsFlatWithDepth(Post post) {
        List<CommentUIDto> flatComments = new ArrayList<>();
        Stack<CommentUIDto> stack = new Stack<>();
        for (Comment comment : post.getComments()) {
            stack.push(new CommentUIDto(comment, 0));
        }

        while (!stack.isEmpty()) {
            CommentUIDto current = stack.pop();
            flatComments.add(current);

            if (current.object().getReplies() != null && !current.object().getReplies().isEmpty()) {
                for (Comment reply : current.object().getReplies()) {
                    stack.push(new CommentUIDto(reply, current.depth() + 1));
                }
            }
        }
        return flatComments;
    }
}
