package model;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.mindrot.jbcrypt.BCrypt;
import play.mvc.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The User model.
 *
 * @author Jackson Cleary
 */
public class User {

    private String id, username, email, passwordDigest;
    private HashMap<String, Session> sessions;

    // Only used when recreating Users from the database
    private User(String id, String username, String email,
                String passwordDigest, HashMap<String, Session> sessions) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordDigest = passwordDigest;
        this.sessions = sessions;
    }

    /**
     * Constructor
     *
     * @param username the username of the new User
     * @param email the email address of the new User
     * @param password the password of the new User's account
     */
    public User(String username, String email, String password) {
        this.id = MongoProvider.allocateObjectID();
        this.username = username.toLowerCase();
        this.email = email.toLowerCase();
        this.passwordDigest = digest(password);
        this.sessions = new HashMap<>();
    }

    /**
     * Gets the users id
     *
     * @return the users id
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the users username
     *
     * @return username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the email address of the User
     *
     * @return the email address of the User
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the hash of the password initially provided by the user
     *
     * @return the hash of the users password
     */
    public String getPasswordDigest() {
        return passwordDigest;
    }

    /**
     * Checks if email and password given match current user
     *
     * @param email the email address to match
     * @param password the password to match
     * @return true if email and password match this user email and password, else false
     */
    public boolean isUser(String email, String password) {
        return (email.equals(this.email.toLowerCase()) && isPassword(password));
    }

    /**
     * Creates a new {@link Session} and returns the sessionID
     *
     * @return the sessionID of the new {@link Session}
     */
    public String createNewSession() {
        Session session = new Session(Controller.request().remoteAddress());
        sessions.put(session.getId(), session);
        // Save session to DB
        MongoUserService.getInstance().update(this);
        return session.getId();
    }

    /**
     * Removes session given by sessionID from the users current sessions
     *
     * @param sessionID the sessionID of the session to remove
     */
    public void deleteSession(String sessionID) {
        // Remove session from sessions
        sessions.remove(sessionID);
        // Remove session from DB
        MongoUserService.getInstance().update(this);
    }

    /**
     * Gets an array of all of users current sessions
     *
     * @return an array of all the users current sessions
     */
    public Session[] getAllSessions() {
        return sessions.values().toArray(new Session[sessions.size()]);
    }

    /**
     * Checks if {@link Session} given by sessionID belongs to the user
     *
     * @param sessionID the sessionID to check for
     * @return true if user has {@link Session} given by sessionID, else false
     */
    public boolean hasSession(String sessionID) {
        return sessions.containsKey(sessionID);
    }

    /**
     * Hashes the password given by the user, using {@link BCrypt}
     *
     * @param input the plaintext password
     * @return the hashed password
     */
    private static String digest(String input) {
        return BCrypt.hashpw(input, BCrypt.gensalt());
    }

    /**
     * Checks if given password is the same as stored password
     *
     * @param password the plaintext password to test against the hashed password
     * @return true if plaintext password matches the stored hash once it has been hashed itself, else false
     */
    public boolean isPassword(String password) {
        return BCrypt.checkpw(password, passwordDigest);
    }

    /**
     * Create a BSON Document from given User object
     *
     * @param u the User object to convert into BSON
     * @return a BSON Document representing the User object
     */
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

    /**
     * Create a User object from a BSON document
     *
     * @param d the BSON document from which to retrieve the User
     * @return the User represented by the Document
     */
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
