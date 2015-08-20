package model;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import model.Exceptions.*;

/**
 * Created by jackson on 13/08/15.
 */
public class UserService {
    public static final UserService instance = new UserService();
    private ConcurrentHashMap<String, User> users = new ConcurrentHashMap<String, User>();

    public User[] getAllUsers() {
        return users.values().toArray(new User[users.size()]);
    }

    // Register new user. Returns null if email already exists in database
    // otherwise returns new user.
    public User addUser(User u) throws ApplicationException {
        Collection<User> userCollection = users.values();
        for(User user: userCollection) {
            if(user.getUsername().equals(u.getUsername())) {
                throw new ApplicationException(ErrorCode.DUPLICATE_USERNAME);
            }
            if(user.getEmail().equals(u.getEmail())) {
                throw new ApplicationException(ErrorCode.DUPLICATE_EMAIL);
            }
        }
        users.put(u.getUsername(), u);
        return u;
    }

    // Finds user by id. Returns null if user id does not exist.
    public User getUser(String username) {
        return users.get(username);
    }

    // Finds user by email and password.
    public User getUser(String email, String password) throws ApplicationException {
        Collection<User> userCollection = users.values();
        for(User user: userCollection) {
            if(user.getEmail().equals(email)) {
                if(user.isPassword(password)) {
                    return user;
                } else {
                    throw new ApplicationException(ErrorCode.INCORRECT_PASSWORD);
                }
            }
        }
        throw new ApplicationException(ErrorCode.EMAIL_NOT_FOUND);
    }

    public User getUserBySession(String sessionId) {
        Collection<User> userCollection = users.values();
        for (User user : userCollection) {
            if (user.hasSession(sessionId)) {
                return user;
            }
        }
        return null;
    }

    public boolean isLoggedIn(String sessionId) {
        Collection<User> userCollection = users.values();
        for(User user: userCollection) {
            if(user.hasSession(sessionId)) {
                return true;
            }
        }
        return false;
    }
}
