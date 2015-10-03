package controllers;

import model.*;
import model.Exceptions.ApplicationException;
import play.mvc.*;
import views.html.users.*;

import java.util.Map;

/**
 * User controller- handles user registration and displaying User pages
 */
public class UserController extends Controller {

    /**
     * Returns the interface to the User data store. As both MongoPostService and
     * {@link model.Deprecated.UserService} implement UserDataInterface, by changing the return to
     * {@link model.Deprecated.UserService#getInstance()} you are able to switch to the in-memory data store.
     *
     * @return The user data store to be used by Twatter
     */
    public static UserDataInterface getUserService() {
        // return UserService.instance;
        return MongoUserService.getInstance();
    }

    /**
     * GET route- register page
     *
     * @return Register page for new users
     */
    public static Result newUser() {
        if(getUserService().getUserBySessionID(session(SessionController.SESSION_VAR)) != null) {
            return redirect(routes.Application.index());
        }
        return ok(register.render());
    }

    // Takes the POST data from the register form and attempts to create new user

    /**
     * POST route- creates new user from form data
     *
     * @return If successful, creates new user and redirects to their home page.
     */
    public static Result createUser() {
        Map<String, String[]> values = request().body().asFormUrlEncoded();
        String username = values.get("username")[0];
        String email = values.get("email")[0];
        String password = values.get("password")[0];
        User u = new User(username, email, password);

        try {
            getUserService().addUser(u);
        } catch(ApplicationException e) {
                flash("error", e.getErrorCode().getDescription());
                return redirect(routes.UserController.newUser());
        }
        session(SessionController.SESSION_VAR, u.createNewSession());
        flash("success", "Hi " + u.getUsername() + ", welcome to Twatter!");
        return redirect(routes.Application.index());
    }

    /**
     * GET route- Shows user given by username. Requester must be authenticated
     *
     * @param username (Optional) username of user we are interested in
     * @return Users post page, or user index listing all users if username is not given
     */
    @Security.Authenticated(CustomAuthenticator.class)
    public static Result showUser(String username) {
        if (username == null) {
            // User index, not main index
            return userIndex();
        }
        User u = getUserService().getUserByUsername(username);
        if(u == null) {
            flash("error", "Error: " + username + " does not exist.");
            return redirect(routes.Application.index());
        }
        System.out.print(u.getUsername());
        return ok(views.html.users.user.render(u));
    }

    // Renders a User index page, showing list of all users

    /**
     * User index page
     *
     * @return Page displaying list of all registered users
     */
    public static Result userIndex() {
        User[] users = getUserService().getUserArray();
        return ok(views.html.users.user_index.render(users));
    }

    /**
     * Returns User object which has given sessionID
     *
     * @param sessionID sessionID(likely to be contained in a cookie)
     * @return User object which owns the sessionID
     */
    public static User getUserFromSessionID(String sessionID) {
        return getUserService().getUserBySessionID(sessionID);
    }

}