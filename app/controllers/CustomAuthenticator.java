package controllers;

import model.User;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

import static play.mvc.Controller.flash;

/**
 * Extends {@link play.mvc.Security.Authenticator} to provide authentication for the Twatter app.
 * Is passed as parameter to {@link play.mvc.Security.Authenticated} annotation for any
 * pages that require user to be logged in(not for API however).
 *
 * @author Jackson Cleary
 */
public class CustomAuthenticator extends Security.Authenticator {

    /**
     * Gets the username of the current user if they are logged in, returns null if not.
     *
     * @param ctx Http context of request
     * @return Username of user who made the request
     */
    @Override
    public String getUsername(Http.Context ctx) {
        if(UserController.getUserFromSessionID(ctx.session().get(SessionController.SESSION_VAR)) != null) {
            return UserController.getUserFromSessionID(ctx.session()
                    .get(SessionController.SESSION_VAR)).getUsername();
        } else return null;
    }

    /**
     * Describes behaviour in the case that the user is not logged in or does not have required privileges
     *
     * @param ctx Http context of request
     * @return Redirects user to the login screen
     */
    @Override
    public Result onUnauthorized(Http.Context ctx) {
        flash("error", "You must log in to access this resource");
        return redirect(routes.SessionController.newSession());
    }

    /**
     * Returns a user object of the current user
     *
     * @param ctx Http context of request
     * @return User object representing the current user
     */
    public static User getUser(Http.Context ctx) {
        String sessionID = ctx.session().get(SessionController.SESSION_VAR);
        return UserController.getUserFromSessionID(sessionID);
    }
}
