package controllers;

import model.User;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

import javax.xml.ws.spi.http.HttpContext;

/**
 * Created by jackson on 20/08/15.
 */
public class CustomAuthenticator extends Security.Authenticator {

    @Override
    public String getUsername(Http.Context ctx) {
        String sessionID = ctx.session().get(SessionController.SESSION_VAR);
        return UserController.getUserFromSessionID(sessionID).getUsername();
    }

    @Override
    public Result onUnauthorized(Http.Context ctx) {
        return redirect(routes.SessionController.newSession());
    }

    public User getUser(Http.Context ctx) {
        String sessionID = ctx.session().get(SessionController.SESSION_VAR);
        return UserController.getUserFromSessionID(sessionID);
    }
}
