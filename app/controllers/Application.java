package controllers;

import model.*;
import play.*;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import views.html.*;
import views.html.static_pages.*;

public class Application extends Controller {

    // Static page controllers
    public static Result index() {
        User user = UserController
                        .getUserService()
                        .getUserBySessionID(session()
                                .get(SessionController.SESSION_VAR));
        if(user == null) {
            return ok(index.render(""));
        } else {
            return ok(views.html.users.user.render(user));
        }
    }

    // User must be signed in to access this page
    @Security.Authenticated(CustomAuthenticator.class)
    public static Result singlePageApp() {
        User u = CustomAuthenticator.getUser(Http.Context.current());
        return ok(views.html.spa.render(u.getUsername(), u.getEmail()));
    }

    public static Result about() {
        return ok(about.render());
    }

    public static Result help() {
        return ok(help.render());
    }

    public static Result contact() {
        return ok(contact.render());
    }
}
