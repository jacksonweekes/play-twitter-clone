package model;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.mindrot.jbcrypt.BCrypt;
import play.mvc.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Simple User class, will be used to store user_index details when they sign up.
 */
public class User {

    private String id, username, email, passwordDigest;
    private HashMap<String, Session> sessions;

    public User(String id, String username, String email,
                String passwordDigest, HashMap<String, Session> sessions) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordDigest = passwordDigest;
        this.sessions = sessions;
    }

    public User(String username, String email, String password) {
        this.id = MongoProvider.allocateObjectID();
        this.username = username.toLowerCase();
        this.email = email.toLowerCase();
        this.passwordDigest = digest(password);
        this.sessions = new HashMap<>();
    }

    public String getId() {
        return id;
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

    // Check if email and password given match current user
    public boolean isUser(String email, String password) {
        return (email.equals(this.email.toLowerCase()) && isPassword(password));
    }

    // Creates a new Session and returns the sessionID
    public String createNewSession() {
        Session session = new Session(Controller.request().remoteAddress());
        sessions.put(session.getId(), session);
        // Save session to DB
        MongoUserService.getInstance().update(this);
        return session.getId();
    }

    public void deleteSession(String sessionID) {
        // Remove session from sessions
        sessions.remove(sessionID);
        // Remove session from DB
        MongoUserService.getInstance().update(this);
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

    // Create a BSON document from given User object
    public static Document userToBson(User u) {
        List<Document> sessions = new ArrayList<>();
        for (Session s : u.getAllSessions()) {
            sessions.add(Session.sessionToBson(s));
        }
        Document d = new Document("_id", new ObjectId(u.getId()))
                .append("username", u.getUsername())
                .append("email", u.getEmail())
                .append("passwordDigest", u.getPasswordDigest())
                .append("sessions", sessions);
        return d;
    }

    // Create a User object from a BSON document
    public static User userFromBson(Document d) {
        if(d == null) {
            return null;
        }

        String id = d.getObjectId("_id").toHexString();
        String username = d.getString("username");
        String email = d.getString("email");
        String passwordDigest = d.getString("passwordDigest");

        // Get list of Sessions
        List<Document> sessionList = d.get("sessions", List.class);

        // Create a HashMap and add each session to it
        HashMap<String, Session> sessions = new HashMap<>();
        for(Document sd : sessionList) {
            Session s = Session.sessionFromBson(sd);
            sessions.put(s.getId(), s);
        }

        return new User(id, username, email, passwordDigest, sessions);
    }
}
