//package de.hsos.swa.application.mocks;
//
//import de.hsos.swa.application.input.dto.out.TopicWithPostCountDto;
//import de.hsos.swa.application.output.repository.TopicRepository;
//import de.hsos.swa.application.output.repository.dto.in.RepositoryResult;
//import de.hsos.swa.domain.entity.Topic;
//import io.quarkus.test.Mock;
//
//import java.util.List;
//import java.util.UUID;
//
//@Mock
//public class TopicRepositoryMock implements TopicRepository {
//    @Override
//    public RepositoryResult<Topic> saveTopic(Topic topic) {
//        return null;
//    }
//
//    @Override
//    public RepositoryResult<Topic> updateTopic(Topic topic) {
//        return null;
//    }
//
//    @Override
//    public RepositoryResult<Topic> deleteTopic(UUID topicId) {
//        return null;
//    }
//
//    @Override
//    public RepositoryResult<List<TopicWithPostCountDto>> getAllTopics() {
//        return null;
//    }
//
//    @Override
//    public RepositoryResult<List<TopicWithPostCountDto>> searchTopic(String searchString) {
//        return null;
//    }
//
//    @Override
//    public RepositoryResult<Topic> getTopicById(UUID topicId) {
//        return null;
//    }
//}
