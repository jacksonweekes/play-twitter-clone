package controllers;

import model.*;
import play.*;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import views.html.*;
import views.html.static_pages.*;

/**
 * This controller is resposible for the static pages/routes in the application, including the
 * single page app.
 *
 * @author Jackson Cleary
 */
public class Application extends Controller {

    /**
     * Index page for unauthorized users.
     *
     * @return Home page of Chirper application
     */
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

    /**
     * Single Page App. User must be authenticated to access.
     *
     * @return Single Page App version of Chirper
     */
    @Security.Authenticated(CustomAuthenticator.class)
    public static Result singlePageApp() {
        User u = CustomAuthenticator.getUser(Http.Context.current());
        return ok(views.html.spa.render(u.getUsername(), u.getEmail()));
    }

    /**
     * About page
     *
     * @return General information page about Chirper
     */
    public static Result about() {
        return ok(about.render());
    }

    /**
     * Help page
     *
     * @return General assistance for users
     */
    public static Result help() {
        return ok(help.render());
    }

    /**
     * Contact page
     *
     * @return Contact details
     */
    public static Result contact() {
        return ok(contact.render());
    }
}
