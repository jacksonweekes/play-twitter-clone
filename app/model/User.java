package model;

import org.mindrot.jbcrypt.BCrypt;
import play.mvc.Controller;

import java.util.HashMap;
import java.util.List;

/**
 * Simple User class, will be used to store user_index details when they sign up.
 */
public class User {

    private String username, email, passwordDigest;
    private HashMap<String, Session> sessions;

    public User(String username, String email, String password) {
        this.username = username.toLowerCase();
        this.email = email.toLowerCase();
        this.passwordDigest = digest(password);
        this.sessions = new HashMap<>();
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    // Check if email and password given match current user
    public boolean isUser(String email, String password) {
        return (email.equals(this.email.toLowerCase()) && isPassword(password));
    }

    // Creates a new Session and returns the sessionID
    public String createNewSession() {
        Session session = new Session(Controller.request().remoteAddress());
        sessions.put(session.getId(), session);
        return session.getId();
    }

    public void deleteSession(String sessionID) {
        sessions.remove(sessionID);
    }

    public Session[] getAllSessions() {
        return sessions.values().toArray(new Session[sessions.size()]);
    }

    // Check if session belongs to user
    public boolean hasSession(String sessionID) {
        return sessions.containsKey(sessionID);
    }

    private static String digest(String input) {
        return BCrypt.hashpw(input, BCrypt.gensalt());
    }

    // Checks if given password is the same as stored password
    public boolean isPassword(String password) {
        return BCrypt.checkpw(password, passwordDigest);
    }
}
