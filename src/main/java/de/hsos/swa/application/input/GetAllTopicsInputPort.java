package de.hsos.swa.application.input;

import de.hsos.swa.application.Result;
import de.hsos.swa.domain.entity.Topic;

import java.util.List;

public interface GetAllTopicsInputPort {
    Result<List<Topic>> getAllTopics();
}
