package de.hsos.swa.application.service;

import de.hsos.swa.application.port.input._shared.Result;
import de.hsos.swa.application.port.input.createPost.CreatePostInputPort;
import de.hsos.swa.application.port.input.createPost.CreatePostInputPortRequest;
import de.hsos.swa.application.port.input.createPost.CreatePostInputPortResponse;
import de.hsos.swa.application.port.output.getUserByName.GetUserByNameOutputPort;
import de.hsos.swa.application.port.output.getUserByName.GetUserByNameOutputPortRequest;
import de.hsos.swa.application.port.output.getUserByName.GetUserByNameOutputPortResponse;
import de.hsos.swa.application.port.output.savePost.SavePostOutputPort;
import de.hsos.swa.application.port.output.savePost.SavePostOutputPortRequest;
import de.hsos.swa.application.port.output.savePost.SavePostOutputPortResponse;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class CreatePostUseCase implements CreatePostInputPort {

    @Inject
    GetUserByNameOutputPort getUserByNameOutputPort;

    @Inject
    SavePostOutputPort savePostOutputPort;


    @Override
    public Result<CreatePostInputPortResponse> createPost(CreatePostInputPortRequest inputPortRequest) {
        // 1. Nutzer holen
        GetUserByNameOutputPortRequest getUserByNameRequest = new GetUserByNameOutputPortRequest(inputPortRequest.getUsername());
        Result<GetUserByNameOutputPortResponse> getUserByNameResponse = this.getUserByNameOutputPort.getUserByName(getUserByNameRequest);

        if(!getUserByNameResponse.isSuccessful()) {
            return Result.error("User does not exist"); // TODO: Error sinnvoll von Applicaion weiterleiten und differenzieren
        }

        // 2. Post auf Domain-Ebene bauen   // TODO: Factory
        User user = new User(getUserByNameResponse.getData().getId(), getUserByNameResponse.getData().getUsername());
        Post post = new Post(inputPortRequest.getTitle(), user);

        // 3. Post persistieren
        SavePostOutputPortRequest savePostRequest = new SavePostOutputPortRequest(post); // TODO: evtl.
        Result<SavePostOutputPortResponse> savePostResponse = this.savePostOutputPort.savePost(savePostRequest);

        return Result.success(new CreatePostInputPortResponse(savePostResponse.getData().getPostId()));
    }
}
