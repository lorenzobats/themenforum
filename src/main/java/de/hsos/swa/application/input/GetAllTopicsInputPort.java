package de.hsos.swa.application.input;

import de.hsos.swa.application.util.Result;
import de.hsos.swa.domain.entity.Topic;

import java.util.List;

// TODO: FilterParams
// 1. Titel, 2 Nutzer
public interface GetAllTopicsInputPort {
    Result<List<Topic>> getAllTopics();
}
