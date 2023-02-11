package de.hsos.swa.application.input.dto.out;

import de.hsos.swa.actors.rest.dto.out.TopicDto;
import de.hsos.swa.domain.entity.Topic;

/**
 * Die Klasse TopicWithPostCountDto ermöglicht die Rückgabe eines Topics und zusätzlich die Anzahl der Posts die
 * zu einem Topic gehören, auch wenn diese nicht im Domainen-Modell eines Topics definiert ist.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 */
public class TopicWithPostCountDto {
    public final TopicDto topic;
    public final Long numberOfPosts;
    public TopicWithPostCountDto(Topic topic, Long numberOfPosts) {
        this.topic = TopicDto.Converter.fromDomainEntity(topic);
        this.numberOfPosts = numberOfPosts;
    }
}


