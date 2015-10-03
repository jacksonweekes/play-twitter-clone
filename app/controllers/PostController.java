package controllers;

import model.*;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

import java.util.Map;

/**
 * This controller is responsible for Post related functions.
 *
 * @author Jackson Cleary
 */
public class PostController extends Controller {

    /**
     * Returns the interface to the Post data store. As both MongoPostService and
     * {@link model.Deprecated.PostService} implement PostDataInterface, by changing the return to
     * {@link model.Deprecated.PostService#getInstance()} you are able to switch to the in-memory data store.
     *
     * @return The post data store to be used by Twatter
     */
    public static PostDataInterface getPostService() {
        return MongoPostService.getInstance();
    }

    /**
     * POST route, for creating new Twatter posts. Requester must be authenticated.
     *
     * @return Creates new post for authenticated user, redirects to user's home page
     */
    @Security.Authenticated(CustomAuthenticator.class)
    public static Result createPost() {
        Map<String, String[]> values = request().body().asFormUrlEncoded();
        String username = CustomAuthenticator.getUser(Http.Context.current()).getUsername();
        String message = values.get("message")[0];
        getPostService().addPost(new Post(username, message));
        return redirect(routes.UserController.showUser(username));
    }

    /**
     * GET route, displays page of posts with given tag
     *
     * @param tag tag we are interested in
     * @return Page displaying posts with given tag
     */
    @Security.Authenticated(CustomAuthenticator.class)
    public static Result getPosts(String tag) {
        return ok(views.html.posts.tag_page.render(tag));
    }

    /**
     * GET route, returns search results page.
     * @param searchTerm Username or tag we are interested in
     * @param type 'person' or 'topic', defines what we are searching for
     * @return Page displaying list of posts related to user or tag, bad request if type != 'person' or 'topic'
     */
    // Returns either user page or tag page, depending on search type
    @Security.Authenticated(CustomAuthenticator.class)
    public static Result search(String searchTerm, String type) {
        if (type.equals("person")) {
            User u = UserController.getUserService().getUserByUsername(searchTerm);
            if (u != null) {
                return redirect(routes.UserController.showUser(u.getUsername()));
            } else {
                flash("error", "User @" + searchTerm + " could not be found");
                return redirect(routes.Application.index());
            }
        } else if (type.equals("topic")) {
            return redirect(routes.PostController.getPosts(searchTerm));
        }
        return badRequest(views.html.static_pages.index.render(""));
    }

}
