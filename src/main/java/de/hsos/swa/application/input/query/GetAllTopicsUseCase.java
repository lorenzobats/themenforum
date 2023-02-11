package de.hsos.swa.application.input.query;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.input.dto.out.TopicWithPostCountDto;

import java.util.List;

/**
 * Das Interface GetAllTopicsUseCase definiert den Input Port für InteraktionsAdapter zur Nutzung des
 * GetAllTopicsService im Application-Hexagon.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see de.hsos.swa.application.service.query.GetAllTopicsService    Implementierender Service dieses Input Ports
 */
@InputPort
public interface GetAllTopicsUseCase {
    ApplicationResult<List<TopicWithPostCountDto>> getAllTopics();
}
