package controllers;

import model.Post;
import model.PostDataInterface;
import model.PostService;
import model.User;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

import java.util.Map;

/**
 * Created by jackson on 21/08/15.
 */
public class PostController extends Controller {

    // In memory data-store
    public static PostDataInterface getPostService() {
        return PostService.instance;
    }

    @Security.Authenticated(CustomAuthenticator.class)
    public static Result createPost() {
        Map<String, String[]> values = request().body().asFormUrlEncoded();
        String username = CustomAuthenticator.getUser(Http.Context.current()).getUsername();
        String message = values.get("message")[0];
        getPostService().addPost(new Post(username, message));
        return redirect(routes.UserController.showUser(username));
    }

    // Renders page showing list of posts tagged with given tag
    @Security.Authenticated(CustomAuthenticator.class)
    public static Result getPosts(String tag) {
        return ok(views.html.posts.tag_page.render(tag));
    }

    // Returns either user page or tag page, depending on search type
    @Security.Authenticated(CustomAuthenticator.class)
    public static Result search(String searchTerm, String type) {
        if(type.equals("person")) {
            User u = UserController.getUserService().getUserByUsername(searchTerm);
            if(u != null) {
                return redirect(routes.UserController.showUser(u.getUsername()));
            } else {
                flash("error", "User @" + searchTerm + " could not be found");
                return redirect(routes.Application.index());
            }
        } else if(type.equals("topic")) {
            return redirect(routes.PostController.getPosts(searchTerm));
        }
        return badRequest(views.html.static_pages.index.render(""));
    }

}
