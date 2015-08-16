package model;

import java.util.Collection;
import java.util.List;
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
    public User registerUser(User u) throws ApplicationException {
        Collection<User> userCollection = users.values();
        for(User user: userCollection) {
            if(user.getEmail().equals(u.getEmail())) {
                throw new ApplicationException(ErrorCode.DUPLICATE_EMAIL);
            }
        }
        users.put(u.getId(), u);
        return u;
    }

    // Finds user by id. Returns null if user id does not exist.
    public User getUser(String id) {
        return users.get(id);
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
}
