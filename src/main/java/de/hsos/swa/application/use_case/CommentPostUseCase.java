package de.hsos.swa.application.use_case;

import de.hsos.swa.application.port.input._shared.Result;
import de.hsos.swa.application.port.input.commentPost.CommentPostInputPort;
import de.hsos.swa.application.port.input.commentPost.CommentPostInputPortRequest;
import de.hsos.swa.application.port.input.commentPost.CommentPostInputPortResponse;
import de.hsos.swa.application.port.input.createPost.CreatePostInputPort;
import de.hsos.swa.application.port.input.createPost.CreatePostInputPortRequest;
import de.hsos.swa.application.port.input.createPost.CreatePostInputPortResponse;
import de.hsos.swa.application.port.output.post.saveComment.SaveCommentOutputPort;
import de.hsos.swa.application.port.output.post.savePost.SavePostOutputPort;
import de.hsos.swa.application.port.output.post.savePost.SavePostOutputPortRequest;
import de.hsos.swa.application.port.output.post.savePost.SavePostOutputPortResponse;
import de.hsos.swa.application.port.output.user.getUserById.GetUserByIdOutputPort;
import de.hsos.swa.application.port.output.user.getUserById.GetUserByIdOutputPortRequest;
import de.hsos.swa.application.port.output.user.getUserById.GetUserByIdOutputPortResponse;
import de.hsos.swa.application.port.output.user.getUserByName.GetUserByNameOutputPort;
import de.hsos.swa.application.port.output.user.getUserByName.GetUserByNameOutputPortRequest;
import de.hsos.swa.application.port.output.user.getUserByName.GetUserByNameOutputPortResponse;
import de.hsos.swa.domain.entity.Post;
import de.hsos.swa.domain.entity.User;
import de.hsos.swa.domain.factory.PostFactory;
import de.hsos.swa.domain.factory.UserFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class CommentPostUseCase implements CommentPostInputPort {

    @Inject
    GetUserByIdOutputPort getUserByIdOutputPort;

    //@Inject
    //SaveCommentOutputPort saveCommentOutputPort;


    @Override
    public Result<CommentPostInputPortResponse> commentPost(CommentPostInputPortRequest inputPortRequest) {
//        // 1. Nutzer holen
//        GetUserByIdOutputPortRequest getUserByIdRequest = new GetUserByIdOutputPortRequest(inputPortRequest.getUserId());
//        Result<GetUserByIdOutputPortResponse> getUserByIdResponse = this.getUserByIdOutputPort.getUserById(getUserByIdRequest);
//
//        if(!getUserByIdResponse.isSuccessful()) {
//            return Result.error("Comment could not be created"); // TODO: Error sinnvoll von Applicaion weiterleiten und differenzieren
//        }
//
//        // 2. Post auf Domain-Ebene bauen   // TODO: Factory
//        User user = UserFactory.createUser(getUserByIdResponse.getData().getId(), getUserByIdResponse.getData().getUsername());
//
//        //Post post = PostFactory.createPost(inputPortRequest.getTitle(), user);
//
//        // 3. Post persistieren
//        SavePostOutputPortRequest savePostRequest = new SavePostOutputPortRequest(post); // TODO: evtl.
//        Result<SavePostOutputPortResponse> savePostResponse = this.savePostOutputPort.savePost(savePostRequest);
//
//        return Result.success(new CreatePostInputPortResponse(savePostResponse.getData().getPostId()));
        return null;
    }
}
