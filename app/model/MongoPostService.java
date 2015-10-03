package model;

import com.mongodb.Block;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a MongoDB backed PostDataInterface. THIS IS A SINGLETON- do not attempt to instantiate,
 * use getInstance()
 *
 * @author Jackson Cleary
 */
public class MongoPostService implements PostDataInterface {
    private static final MongoPostService instance = new MongoPostService();
    private MongoProvider mongoProvider;

    private MongoPostService() {
        mongoProvider = MongoProvider.getInstance();
    }

    /**
     * Gets the single MongoPostService instance
     *
     * @return instance of MongoPostService
     */
    public static MongoPostService getInstance() {
        return instance;
    }

    /**
     * Use to add a new post to the database.
     *
     * @param post the {@link Post} object to add to the database
     */
    @Override
    public void addPost(Post post) {
        Document d = Post.postToBson(post);
        mongoProvider.getPostCollection().insertOne(d);

        // Let the PostHub know that a post has been added
        PostHub.getInstance().send(post);
    }

    /**
     * Gets a {@link List} of all Posts from the database
     *
     * @return a {@link List} of all {@link Post Posts} in the database
     */
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

    /**
     * Gets a {@link List} of all posts by given username, sorted by newest first
     *
     * @param username the username of the User we are interested in
     * @return a {@link List} of all {@link Post Posts} by the given user
     */
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

    /**
     * Gets a {@link List} of most recent posts by given user, up to the number specified
     *
     * @param username the user we are interested in
     * @param numPosts maximum number of posts to return
     * @return {@link List} of up to numPosts most recent {@link Post Posts} by given user
     */
    @Override
    public List<Post> getRecentPosts(String username, int numPosts) {
        List<Post> allPosts = getAllPosts(username);
        if (allPosts.size() <= numPosts) {
            return allPosts;
        }
        return allPosts.subList(0, numPosts - 1);
    }

    /**
     * Gets a {@link List} of all posts with given tag, sorted by newest first
     *
     * @param tag the tag we are interested in
     * @return a {@link List} of all {@link Post Posts} with given tag
     */
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

    /**
     * Gets a {@link List} of the most recent posts with given tag, up to the number specified
     *
     * @param tag the tag we are interested in
     * @param numPosts the maximum number of {@link Post Posts} to return
     * @return
     */
    @Override
    public List<Post> getPostsByTag(String tag, int numPosts) {
        List<Post> postList = getPostsByTag(tag);
        if (postList.size() <= numPosts) {
            return postList;
        }
        return postList.subList(0, numPosts - 1);
    }
}
