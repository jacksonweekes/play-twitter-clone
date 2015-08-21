package model;

import model.Exceptions.ApplicationException;

/**
 * Created by jackson on 20/08/15.
 */
public interface UserDataInterface {
    void addUser(User user) throws ApplicationException;
    User[] getUserArray();
    User getUserByUsername(String username);
    User getUserByEmailAndPassword(String email, String password);
    User getUserBySessionID(String sessionID);
}
