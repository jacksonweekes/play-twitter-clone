package controllers;

import model.*;
import model.Exceptions.ApplicationException;
import model.Exceptions.ErrorCode;
import model.Session;
import play.mvc.*;
import play.mvc.Http.*;
import views.html.users.login;
import views.html.users.register;

import java.util.Map;

/**
 * Created by jackson on 13/08/15.
 */
public class UserController extends Controller {
    public final static String SESSION_VAR = "APP_SESSION";

    protected static UserService getUserService() {
        return UserService.instance;
    }

    public static Result loginForm() {
        return ok(login.render(""));
    }

    public static Result login() {
        return ok("Not implemented yet");
    }

    public static Result registerForm() {
        return ok(register.render(""));
    }

    public static Result register() {
        Map<String, String[]> values = request().body().asFormUrlEncoded();
        String email = values.get("email")[0];
        String password = values.get("password")[0];
        User newUser = new User(email, password);

        try {
            getUserService().registerUser(newUser);
        } catch(ApplicationException e) {
            if(e.getErrorCode() == ErrorCode.DUPLICATE_EMAIL) {
                return ok(views.html.users.register.render(e.getErrorCode().getDescription()));
            }
        }
        createSession(newUser);
        return showUser(newUser.getId());
    }

    public static Result showUser(String id) {
        User u = getUserService().getUser(id);
        return ok(views.html.users.user.render(u));
    }

    public static Result showAllUsers() {
        User[] users = getUserService().getAllUsers();
        return ok(views.html.users.user_index.render(users));
    }

    private static void createSession(User user) {
        model.Session s = new model.Session(request().remoteAddress());
        user.addNewSession(s);
        session(SESSION_VAR, s.getId());
    }




}
