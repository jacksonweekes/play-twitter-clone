package model;

import com.mongodb.Block;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jackson on 25/09/15.
 */
public class MongoPostService implements PostDataInterface {
    private static final MongoPostService instance = new MongoPostService();
    private MongoProvider mongoProvider;

    private MongoPostService() {
        mongoProvider = MongoProvider.getInstance();
    }

    public static MongoPostService getInstance() {
        return instance;
    }

    @Override
    public void addPost(Post post) {
        Document d = Post.postToBson(post);
        mongoProvider.getPostCollection().insertOne(d);
    }

    @Override
    public List<Post> getAllPosts() {
        List<Post> postList = new ArrayList<>();
        mongoProvider.getPostCollection().find().sort(new Document("postTime", -1))
                .forEach(new Block<Document>() {
                    @Override
                    public void apply(Document document) {
                        postList.add(Post.postFromBson(document));
                    }
                });
        return postList;
    }

    @Override
    public List<Post> getAllPosts(String username) {
        List<Post> postList = new ArrayList<>();
        mongoProvider.getPostCollection().find(new Document("username", username))
                .sort(new Document("postTime", -1)).forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                postList.add(Post.postFromBson(document));
            }
        });
        return postList;
    }

    @Override
    public List<Post> getRecentPosts(String username, int numPosts) {
        List<Post> allPosts = getAllPosts(username);
        if (allPosts.size() <= numPosts) {
            return allPosts;
        }
        return allPosts.subList(0, numPosts - 1);
    }

    @Override
    public List<Post> getPostsByTag(String tag) {
        List<Post> postList = new ArrayList<>();
        mongoProvider.getPostCollection().find(new Document("tags", tag)).sort(new Document("postTime", -1))
                .forEach(new Block<Document>() {
                    @Override
                    public void apply(Document document) {
                        postList.add(Post.postFromBson(document));
                    }
                });
        return postList;
    }

    @Override
    public List<Post> getPostsByTag(String tag, int numPosts) {
        List<Post> postList = getPostsByTag(tag);
        if (postList.size() <= numPosts) {
            return postList;
        }
        return postList.subList(0, numPosts - 1);
    }
}
