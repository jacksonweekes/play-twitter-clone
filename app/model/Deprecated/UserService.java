package model.Deprecated;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import model.Exceptions.*;
import model.User;
import model.UserDataInterface;

/**
 * In memory data store for Users
 *
 * @author Jackson Cleary
 * @deprecated - Use {@link model.MongoUserService} instead
 */
public class UserService implements UserDataInterface {
    private static final UserService instance = new UserService();
    private ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

    public static UserService getInstance() {
        return instance;
    }

    // Returns array of all registered users
    public User[] getUserArray() {
        return users.values().toArray(new User[users.size()]);
    }

    // Register new user. Throws exception if new user conflicts with already registered user
    public void addUser(User u) throws RegistrationException {
        Collection<User> userCollection = users.values();
        for(User user: userCollection) {
            if(user.getUsername().equals(u.getUsername())) {
                throw new RegistrationException(RegistrationErrorCode.DUPLICATE_USERNAME);
            }
            if(user.getEmail().equals(u.getEmail())) {
                throw new RegistrationException(RegistrationErrorCode.DUPLICATE_EMAIL);
            }
        }
        users.put(u.getUsername(), u);
    }

    // Finds user by id. Returns null if user id does not exist.
    public User getUserByUsername(String username) {
        return users.get(username.toLowerCase());
    }

    // Finds user by email and password. Returns null if email/password combination is invalid
    public User getUserByEmailAndPassword(String email, String password) {
        Collection<User> userCollection = users.values();
        for(User user: userCollection) {
            if(user.isUser(email, password)) {
                return user;
            }
        }
        return null;
    }

    // Finds user by given sessionID, returns null if ID does not belong to any user
    public User getUserBySessionID(String sessionId) {
        Collection<User> userCollection = users.values();
        for (User user : userCollection) {
            if (user.hasSession(sessionId)) {
                return user;
            }
        }
        return null;
    }
}
