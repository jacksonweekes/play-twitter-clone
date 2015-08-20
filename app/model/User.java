package model;

import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;

/**
 * Simple User class, will be used to store user_index details when they sign up.
 */
public class User {

    private String username, email, passwordDigest;
    private HashMap<String, Session> sessions;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.passwordDigest = digest(password);
        this.sessions = new HashMap<>();
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordDigest() {
        return passwordDigest;
    }

    // Checks if given password is the same as stored password
    public boolean isPassword(String password) {
        if(passwordDigest.equals(digest(password))) {
            return true;
        }
        return false;
    }

    // Add new session
    public void addNewSession(Session session) {
        sessions.put(session.getId(), session);
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
}
