package controllers;

import model.User;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import views.html.sessions.login;

/**
 * Created by jackson on 17/08/15.
 */
public class SessionsController {
    public final static String SESSION_VAR = "APP_SESSION";

    // Renders login page
    public static Result newSession() {
        return Results.ok(login.render(""));
    }

    // Creates new sessiion
    static void createSession(User user) {
        model.Session s = new model.Session(Controller.request().remoteAddress());
        user.addNewSession(s);
        Controller.session(SESSION_VAR, s.getId());
    }

    //
    public static Result login() {
        return Results.ok("Not implemented yet");
    }

}
