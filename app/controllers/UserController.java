package controllers;

import model.*;
import model.Exceptions.ApplicationException;
import model.Exceptions.ErrorCode;
import play.mvc.*;
import views.html.users.register;

import java.util.Map;

/**
 * Created by jackson on 13/08/15.
 */
public class UserController extends Controller {

    protected static UserService getUserService() {
        return UserService.instance;
    }

    public static Result newUser() {
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
        SessionsController.createSession(u);
        return redirect("/users?name=" + u.getUsername());
    }

    public static Result showUser(String name) {
        if (name == null) {
            return index();
        }
        User u = getUserService().getUser(name);
        if(u == null) {
            flash("error", name + "does not exist.");
            return redirect(routes.UserController.showUser(null));
        }
        return ok(views.html.users.user.render(u));
    }

    public static Result index() {
        User[] users = getUserService().getAllUsers();
        return ok(views.html.users.user_index.render(users));
    }

}