package model;

import model.Exceptions.RegistrationException;

/**
 * Interface which any User data stores must implement. Any providers that
 * implement this interface should be able to provide for the applications data storage
 * needs without any other modification to the application aside from changing which
 * provider is referenced by the User controller.
 */
public interface UserDataInterface {
    /**
     * Adds user to the datastore
     *
     * @param user the {@link User} to add to the data store
     * @throws RegistrationException
     */
    void addUser(User user) throws RegistrationException;

    /**
     * Get an array of all the {@link User} objects in the data store
     *
     * @return array of all the {@link User} objects in the data store
     */
    User[] getUserArray();

    /**
     * Get a {@link User} object by the given username, returns null if no matching user found
     *
     * @param username the username to search for
     * @return a {@link User} if one is found with the given username, else null
     */
    User getUserByUsername(String username);

    /**
     * Gets a {@link User} object by email and password, returns null if no matching email exists
     * or email and password don't match.
     *
     * @param email email address of the user
     * @param password password of the user
     * @return a {@link User} if one is found with matching email and password, else null
     */
    User getUserByEmailAndPassword(String email, String password);

    /**
     * Gets a {@link User} object by given sessionID if one exists
     *
     * @param sessionID a session id(likely from cookie)
     * @return a {@link User} object if one is found with a matching {@link Session}, else null
     */
    User getUserBySessionID(String sessionID);
}
