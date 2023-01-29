package de.hsos.swa.application.input;

import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Topic;

import java.util.List;

// TODO: Kann ggf. weg, weil nicht verwendet
public interface GetAllTopicsInputPort {
    Result<List<Topic>> getAllTopics();
}
