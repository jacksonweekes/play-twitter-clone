package model;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * Created by jackson on 24/09/15.
 */
public class MongoProvider {
    private static final MongoProvider instance = new MongoProvider();

    private MongoClient mongoClient;

    private MongoProvider() {
        mongoClient = new MongoClient("localhost", 27017);
    }

    public static MongoProvider getInstance() {
        return instance;
    }

    protected MongoDatabase getDB() {
        return mongoClient.getDatabase("comp391_jcleary9");
    }

    protected MongoCollection<Document> getUserCollection() {
        return getDB().getCollection("twatter_users");
    }

    protected static String allocateObjectID() {
        return new ObjectId().toHexString();
    }
}
