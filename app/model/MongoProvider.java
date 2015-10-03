package model;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * Class responsible for connecting with MongoDB, provides instance which other interfaces can use.
 * THIS IS A SINGLETON- do not try to instantiate new instance, use getInstance()
 *
 * @author Jackson Cleary
 */
public class MongoProvider {
    private static final MongoProvider instance = new MongoProvider();

    private MongoClient mongoClient;

    private MongoProvider() {
        mongoClient = new MongoClient("localhost", 27017);
    }

    /**
     * The single instance of MongoProvider
     * @return MongoProvider instance
     */
    public static MongoProvider getInstance() {
        return instance;
    }

    private MongoDatabase getDB() {
        return mongoClient.getDatabase("comp391_jcleary9");
    }

    /**
     * Gets the Twatter user collection
     *
     * @return reference to the twatter_users collection in db
     */
    protected MongoCollection<Document> getUserCollection() {
        return getDB().getCollection("twatter_users");
    }

    /**
     * Gets the Twatter post collection
     *
     * @return reference to the twatter_posts collection in db
     */
    protected MongoCollection<Document> getPostCollection() {
        return getDB().getCollection("twatter_posts");
    }

    /**
     * Creates new MongoDB ObjectId which can be allocated to any objects which will be
     * stored in the database.
     *
     * @return a MongoDB ObjectId as a hex string
     */
    protected static String allocateObjectID() {
        return new ObjectId().toHexString();
    }
}
