package controllers;

import play.*;
import play.mvc.*;
import model.BCryptExample;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

public class Application extends Controller {

    public static Result encrypt(String pw) {
        return ok(views.html.application.encrypted.render(BCryptExample.encrypt(pw)));
    }

    public static Result matches(String pw) {
        return ok(views.html.application.matches.render(BCryptExample.matchesLastEncrypted(pw)));
    }

    public static Result oldIndex() {
        return ok(views.html.application.index.render());
    }

    // Static page controllers
    public static Result index() {
        return ok(index.render(""));
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
