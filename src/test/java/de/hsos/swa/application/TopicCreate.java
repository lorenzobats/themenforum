package de.hsos.swa.application;

import de.hsos.swa.application.input.CreateTopicUseCase;
import de.hsos.swa.application.input.dto.in.CreateTopicCommand;

import javax.inject.Inject;

public class TopicCreate {
    @Inject
    CreateTopicUseCase createTopicUseCase;

//    @Given("I provide all required data to create a core router")
//            public void create_topic(){
//            topic =  this.createTopicUseCase.createTopic(
//                    new CreateTopicCommand(
//                            "Mein Titel"
//                    )
//            )
//                    createRouter(
//                            Vendor.CISCO,
//                            Model.XYZ0001,
//                            IP.fromAddress("20.0.0.1"),
//                            locationA,
//                            CORE
//                    );
//            }
}
