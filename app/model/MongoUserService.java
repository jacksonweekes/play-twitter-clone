package model;

import com.mongodb.Block;
import model.Exceptions.RegistrationErrorCode;
import model.Exceptions.RegistrationException;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a MongoDB backed UserDataInterface
 *
 * @author Jackson Cleary
 */
public class MongoUserService implements UserDataInterface {
    private static final MongoUserService instance = new MongoUserService();
    private MongoProvider mongoProvider;

    private MongoUserService() {
        mongoProvider = MongoProvider.getInstance();
    }

    /**
     * Gets the single MongoUserService instance
     *
     * @return instance of MongoUserService
     */
    public static MongoUserService getInstance() {
        return instance;
    }

    /**
     * Use to add a new user to the database
     *
     * @param user the {@link User} to be added to the database
     * @throws RegistrationException
     */
    @Override
    public void addUser(User user) throws RegistrationException {
        if (getUserByUsername(user.getUsername()) != null) {
            throw new RegistrationException(RegistrationErrorCode.DUPLICATE_USERNAME);
        } else if (mongoProvider.getUserCollection()
                .find(new Document("email", user.getEmail())).first() != null) {
            throw new RegistrationException(RegistrationErrorCode.DUPLICATE_EMAIL);
        }
        Document d = User.userToBson(user);
        mongoProvider.getUserCollection().insertOne(d);
    }

    /**
     * Gets an array of all users in the database
     *
     * @return Array of {@link User} objects
     */
    @Override
    public User[] getUserArray() {
        List<User> userList = new ArrayList<>();
        mongoProvider.getUserCollection().find().forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                userList.add(User.userFromBson(document));
            }
        });
        User[] userArray = userList.toArray(new User[userList.size()]);
        return userArray;
    }

    /**
     * Gets a {@link User} object by given username
     *
     * @param username the username of the User object to retrieve
     * @return the {@link User} object with given username
     */
    @Override
    public User getUserByUsername(String username) {
        Document d = mongoProvider.getUserCollection()
                .find(new Document("username", username)).first();
        User u = User.userFromBson(d);
        return u;
    }

    /**
     * Gets a {@link User} by given email and password, returns null if email or password incorrect.
     *
     * @param email    the email of the user
     * @param password the password of the user
     * @return
     */
    @Override
    public User getUserByEmailAndPassword(String email, String password) {
        Document d = mongoProvider.getUserCollection()
                .find(new Document("email", email)).first();
        User u = User.userFromBson(d);
        if (u != null && u.isPassword(password)) {
            return u;
        } else {
            return null;
        }
    }

    /**
     * Gets a {@link User} with given sessionID, returns null if none exist
     *
     * @param sessionID the sessionID to search for(likely from browser cookie)
     * @return the {@link User} object which has the given sessionID
     */
    @Override
    public User getUserBySessionID(String sessionID) {
        if (sessionID == null) {
            return null;
        }
        Document d = mongoProvider.getUserCollection()
                .find(new Document("sessions._id", new ObjectId(sessionID)))
                .first();
        // If no user found with given sessionID
        if (d == null) {
            return null;
        }

        return User.userFromBson(d);
        //return null;
    }

    /**
     * Updates the database when the current {@link User} model changes
     *
     * @param u the {@link User} model to update
     */
    public void update(User u) {
        mongoProvider.getUserCollection()
                .replaceOne(new Document("_id", new ObjectId(u.getId())), User.userToBson(u));
    }
}
