package controllers;

import model.Post;
import model.User;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import java.util.List;
import play.libs.Json;

/**
 * Created by jackson on 21/08/15.
 */
public class APIController extends Controller {

    // Post message. Requires user to be authenticated
    // Requires "message" field
    @Security.Authenticated(APIAuthenticator.class)
    public static Result postMessage() {
        String message = request().body().asText();
        User u = APIAuthenticator.getUser(Http.Context.current());
        Post post = new Post(u.getUsername(), message);
        PostController.getPostService().addPost(post);
        return ok();
    }

    // Get recent user posts(maximum previous 30 posts) in JSON format belonging to specified user
    // Requester must be authenticated
    // Returns 404 Not Found if username does not exist
    @Security.Authenticated(APIAuthenticator.class)
    public static Result getRecentUserPosts(String username) {
        User u = UserController.getUserService().getUserByUsername(username);
        if(u == null) {
            return notFound();
        }
        List<Post> posts = PostController.getPostService().getRecentPosts(u.getUsername(), 30);
        return ok(Json.toJson(posts));
    }

    // Get recent posts(maximum previous 30 posts) in JSON format that contain specified tag
    // Requester must be authenticated
    // Response will be empty if no posts are tagged with given tag
    @Security.Authenticated(APIAuthenticator.class)
    public static Result getRecentTaggedPosts(String tag) {
        List<Post> posts = PostController.getPostService().getPostsByTag(tag);
        return ok(Json.toJson(posts));
    }

}
