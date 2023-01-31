package de.hsos.swa.application.output.repository.dto.in;

import de.hsos.swa.domain.entity.Topic;

public record TopicOutputPortDto(
       Topic topic,
       Long numberOfPosts
){}
