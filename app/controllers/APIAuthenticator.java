package controllers;

import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

/**
 * Extends {@link controllers.CustomAuthenticator} which extends {@link play.mvc.Security.Authenticator}.
 * Used to provide security to the API endpoints, passed as parameter to
 * the {@link play.mvc.Security.Authenticated} annotation.
 *
 * @author Jackson Cleary
 */
public class APIAuthenticator extends CustomAuthenticator {

    /**
     * Returns HTTP 401 Unauthorized status if the user is not authenticated.
     *
     * @param ctx Current Http context
     * @return Returns 401 unauthorized
     */
    @Override
    public Result onUnauthorized(Http.Context ctx) {
        return unauthorized();
    }

}

