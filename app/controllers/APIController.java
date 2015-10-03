package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import model.Post;
import model.User;
import play.mvc.*;

import java.util.List;

import play.libs.Json;

/**
 * This is the API controller for the Twatter app.
 *
 * @author Jackson Cleary
 */
public class APIController extends Controller {

    /**
     * Post message. Requester must be authenticated. Message must be in text/plain encoding.
     * Whatever is contained in POST body is added to the authenticated user's posts.
     *
     * @return HTTP 200 OK if successful
     */
    @Security.Authenticated(APIAuthenticator.class)
    public static Result postMessage() {
        String message = request().body().asText();
        User u = APIAuthenticator.getUser(Http.Context.current());
        Post post = new Post(u.getUsername(), message);
        PostController.getPostService().addPost(post);
        return ok();
    }

    /**
     * Get recent user posts(maximum previous 30 posts) in JSON format belonging to specified user.
     * Requester must be authenticated
     *
     * @param username Username of user whose posts we wish to retrieve
     * @return JSON array of users most recent 30 posts, empty array if user does not exist
     */
    @Security.Authenticated(APIAuthenticator.class)
    public static Result getRecentUserPosts(String username) {
        User u = UserController.getUserService().getUserByUsername(username);
        if (u == null) {
            // Return empty array
            return ok("[]");
        }
        List<Post> posts = PostController.getPostService().getRecentPosts(u.getUsername(), 30);
        return ok(Json.toJson(posts));
    }

    /**
     * Get recent posts(maximum previous 30 posts) in JSON format that contain specified tag.
     * Requester must be authenticated.
     *
     * @param tag Tag we are interested in.
     * @return JSON array of most recent 30 posts which are tagged with given tag
     */
    @Security.Authenticated(APIAuthenticator.class)
    public static Result getRecentTaggedPosts(String tag) {
        List<Post> posts = PostController.getPostService().getPostsByTag(tag);
        return ok(Json.toJson(posts));
    }

    /**
     * Creates a WebSocket connection to return to client.
     *
     * @param searchTerm Username or tag we are interested in
     * @param searchType 'users' or 'tags', defines what new post information the socket should check
     * @return WebSocket connection which can be used by the client to keep up to date with the server
     */
    @Security.Authenticated(APIAuthenticator.class)
    public static WebSocket<String> socket(String searchTerm, String searchType) {
        return WebSocket.<String>withActor((out) -> PostWebSocketActor.props(searchTerm, searchType, out));
    }
}
