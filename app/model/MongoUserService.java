package model;

import com.mongodb.Block;
import model.Exceptions.ApplicationException;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jackson on 24/09/15.
 */
public class MongoUserService implements UserDataInterface {
    private static final MongoUserService instance = new MongoUserService();
    private MongoProvider mongoProvider;

    private MongoUserService() {
        mongoProvider = MongoProvider.getInstance();
    }

    public static MongoUserService getInstance() {
        return instance;
    }

    @Override
    public void addUser(User user) throws ApplicationException {
        Document d = User.userToBson(user);
        mongoProvider.getUserCollection().insertOne(d);
    }

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

    @Override
    public User getUserByUsername(String username) {
        Document d = mongoProvider.getUserCollection()
                .find(new Document("username", username)).first();
        User u = User.userFromBson(d);
        return u;
    }

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

    @Override
    public User getUserBySessionID(String sessionID) {
        if(sessionID == null) {
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

    public void update(User u) {
        mongoProvider.getUserCollection()
                .replaceOne(new Document("_id", new ObjectId(u.getId())), User.userToBson(u));
    }
}
