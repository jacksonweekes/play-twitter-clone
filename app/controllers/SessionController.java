package controllers;

import model.User;
import play.mvc.*;
import views.html.sessions.login;

import java.util.*;
/**
 * Session controller for Chirper, handles login/logout.
 *
 * @author Jackson Cleary
 */
public class SessionController extends Controller {
    public final static String SESSION_VAR = "session_id";

    /**
     * GET route- Renders login page
     *
     * @return login page
     */
    public static Result newSession() {
        return ok(login.render(""));
    }

    /**
     * POST route- performs the 'login' action
     *
     * @return Creates a new session which is stored by the {@link User} object, and the session
     *         id is passed to the client browser in a cookie.
     */
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

    /**
     * GET route- Performs 'logout' function
     *
     * @param sessionID (Optional) If provided as query parameter, deletes given session. If not
     *                  provided, logs out current users session
     * @return deletes session, redirects to Chirper home page
     */
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
