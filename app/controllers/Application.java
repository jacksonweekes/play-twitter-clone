package controllers;

import model.BCryptExample;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {

    public static Result encrypt(String pw) {
        return ok(views.html.application.encrypted.render(BCryptExample.encrypt(pw)));
    }

    public static Result matches(String pw) {
        return ok(views.html.application.matches.render(BCryptExample.matchesLastEncrypted(pw)));
    }

    public static Result index() {
        return ok(views.html.application.index.render());
    }

}
