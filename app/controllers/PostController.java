package controllers;

import model.Post;
import model.PostDataInterface;
import model.PostService;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Map;

/**
 * Created by jackson on 21/08/15.
 */
public class PostController extends Controller {

    public static PostDataInterface getPostService() {
        return PostService.instance;
    }

    public static Result createPost() {
        Map<String, String[]> values = request().body().asFormUrlEncoded();
        String username = UserController
                .getUserFromSessionID(session().get(SessionController.SESSION_VAR))
                .getUsername();
        String message = values.get("message")[0];
        getPostService().addPost(new Post(username, message));
        return redirect(routes.UserController.showUser(username));
    }

}
