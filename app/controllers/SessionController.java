package controllers;

import model.User;
import play.mvc.*;
import views.html.sessions.login;

import java.util.*;
/**
 * Created by jackson on 17/08/15.
 */
public class SessionController extends Controller {
    public final static String SESSION_VAR = "session_id";

    // Renders login page
    public static Result newSession() {
        return ok(login.render(""));
    }

    // Creates new session(performs the 'login' action)
    public static Result createSession() {
        Map<String, String[]> values = request().body().asFormUrlEncoded();
        String email = values.get("email")[0];
        String password = values.get("password")[0];
        User user = UserController.getUserService().getUserByEmailAndPassword(email, password);
        if(user != null) {
            session(SESSION_VAR, user.createNewSession());
            return redirect(routes.Application.index());
        }
        flash("error", "Invalid email address or password");
        return badRequest(login.render(""));
    }

    //
    public static Result deleteSession(String sessionID) {
        if(sessionID != null) {
            UserController.getUserFromSessionID(sessionID).deleteSession(sessionID);
            flash("success", "Remote session unauthorized");
            return redirect(routes.Application.index());
        }
        sessionID = session().get(SESSION_VAR);
        User u = UserController.getUserFromSessionID(sessionID);
        u.deleteSession(sessionID);
        session().clear();
        flash("success", "You have now been logged out");
        return redirect(routes.Application.index());
    }

}
