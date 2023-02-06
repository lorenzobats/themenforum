package de.hsos.swa.application.input.query;

import de.hsos.swa.application.annotations.InputPort;
import de.hsos.swa.application.input.dto.in.SearchTopicsQuery;
import de.hsos.swa.application.input.dto.out.ApplicationResult;
import de.hsos.swa.application.input.dto.out.TopicWithPostCountDto;

import javax.validation.Valid;
import java.util.List;
@InputPort
public interface SearchTopicsUseCase {
   ApplicationResult<List<TopicWithPostCountDto>> searchTopics(@Valid SearchTopicsQuery query);
}
