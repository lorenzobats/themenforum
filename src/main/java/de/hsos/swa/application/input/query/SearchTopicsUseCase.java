package de.hsos.swa.application.input.query;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.in.SearchTopicsQuery;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.input.dto.out.TopicWithPostCountDto;


import javax.validation.Valid;
import java.util.List;

/**
 * Das Interface SearchTopicsUseCase definiert den Input Port für InteraktionsAdapter zur Nutzung des
 * SearchTopicsService im Application-Hexagon.
 *
 * @author Lorenzo Battiston
 * @author Oliver Schlüter
 * @version 1.0
 * @see de.hsos.swa.application.service.query.SearchTopicsService    Implementierender Service dieses Input Ports
 * @see SearchTopicsQuery                                            Übergebenes Request-DTO an diesen Input Port
 */
@InputPort
public interface SearchTopicsUseCase {
   ApplicationResult<List<TopicWithPostCountDto>> searchTopics(@Valid SearchTopicsQuery query);
}
