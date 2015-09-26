package controllers;

import model.User;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

import static play.mvc.Controller.flash;

/**
 * Created by jackson on 20/08/15.
 */
public class CustomAuthenticator extends Security.Authenticator {

    @Override
    public String getUsername(Http.Context ctx) {
        if(UserController.getUserFromSessionID(ctx.session().get(SessionController.SESSION_VAR)) != null) {
            return UserController.getUserFromSessionID(ctx.session()
                    .get(SessionController.SESSION_VAR)).getUsername();
        } else return null;
    }

    @Override
    public Result onUnauthorized(Http.Context ctx) {
        flash("error", "You must log in to access this resource");
        return redirect(routes.SessionController.newSession());
    }

    public static User getUser(Http.Context ctx) {
        String sessionID = ctx.session().get(SessionController.SESSION_VAR);
        return UserController.getUserFromSessionID(sessionID);
    }
}
