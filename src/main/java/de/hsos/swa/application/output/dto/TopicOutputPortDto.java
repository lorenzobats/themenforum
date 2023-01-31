package de.hsos.swa.application.output.dto;

import de.hsos.swa.domain.entity.Topic;

public record TopicOutputPortDto(
       Topic topic,
       Long numberOfPosts
){}
