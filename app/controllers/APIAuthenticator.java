package controllers;

import model.User;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

/**
 * Created by jackson on 20/08/15.
 */
public class APIAuthenticator extends CustomAuthenticator {

    @Override
    public Result onUnauthorized(Http.Context ctx) {
        return unauthorized();
    }

}

