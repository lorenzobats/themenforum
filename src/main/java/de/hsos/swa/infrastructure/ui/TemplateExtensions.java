package de.hsos.swa.infrastructure.ui;

import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.Topic;
import io.quarkus.qute.TemplateExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@TemplateExtension
public class TemplateExtensions {
    public static Integer totalVotes(Post post) {
        return post.getUpVotes() - post.getDownVotes();
    }

    public static String parsedCreatedAtDate(Post post) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.uuuu");
        return post.getCreatedAt().format(formatter);
    }

    public static String parsedCreatedAtDate(Topic topic) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.uuuu");
        return topic.getCreatedAt().format(formatter);
    }
}
