package de.hsos.swa.infrastructure.ui;

import de.hsos.swa.domain.entity.Post;
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
}
