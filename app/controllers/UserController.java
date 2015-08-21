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

    //
    public static UserDataInterface getUserService() {
        return UserService.instance;
    }

    public static Result newUser() {
        if(getUserService().getUserBySessionID(session(SessionController.SESSION_VAR)) != null) {
            return redirect(routes.Application.index());
        }
        return ok(register.render());
    }

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

    @Security.Authenticated(CustomAuthenticator.class)
    public static Result showUser(String username) {
        if (username == null) {
            return index();
        }
        User u = getUserService().getUserByUsername(username);
        if(u == null) {
            flash("error", "Error: " + username + "does not exist.");
            return redirect(routes.UserController.showUser(null));
        }
        return ok(views.html.users.user.render(u));
    }

    public static Result index() {
        User[] users = getUserService().getUserArray();
        return ok(views.html.users.user_index.render(users));
    }

    public static User getUserFromSessionID(String sessionID) {
        return getUserService().getUserBySessionID(sessionID);
    }

}