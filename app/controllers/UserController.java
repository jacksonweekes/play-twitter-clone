package controllers;

import model.*;
import model.Exceptions.ApplicationException;
import play.mvc.*;
import views.html.users.*;

import java.util.Map;

/**
 * Created by jackson on 13/08/15.
 */
public class UserController extends Controller {

    // In memory data-store
    public static UserDataInterface getUserService() {
        return UserService.instance;
    }

    // Returns the register page
    public static Result newUser() {
        if(getUserService().getUserBySessionID(session(SessionController.SESSION_VAR)) != null) {
            return redirect(routes.Application.index());
        }
        return ok(register.render());
    }

    // Takes the POST data from the register form and attempts to create new user
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

    // Shows user given by username. If username is null, show user index,
    // otherwise redirect to current users homepage
    @Security.Authenticated(CustomAuthenticator.class)
    public static Result showUser(String username) {
        if (username == null) {
            // User index, not main index
            return userIndex();
        }
        User u = getUserService().getUserByUsername(username);
        if(u == null) {
            flash("error", "Error: " + username + "does not exist.");
            return redirect(routes.Application.index());
        }
        return ok(views.html.users.user.render(u));
    }

    // Renders a User index page, showing list of all users
    public static Result userIndex() {
        User[] users = getUserService().getUserArray();
        return ok(views.html.users.user_index.render(users));
    }

    public static User getUserFromSessionID(String sessionID) {
        return getUserService().getUserBySessionID(sessionID);
    }

}