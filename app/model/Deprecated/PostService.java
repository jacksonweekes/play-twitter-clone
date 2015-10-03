package model.Deprecated;

import model.Post;
import model.PostDataInterface;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In memory data store for Posts
 *
 * @author Jackson Cleary
 * @deprecated - Use {@link model.MongoPostService} instead
 */
public class PostService implements PostDataInterface {
    private static final PostService instance = new PostService();
    private ConcurrentHashMap<String, Post> posts = new ConcurrentHashMap<>();

    public PostService getInstance() {
        return instance;
    }

    @Override
    public void addPost(Post post) {
        posts.put(post.getPostID(), post);
    }

    // Returns list of posts from all users, sorted by time of creation
    @Override
    public List<Post> getAllPosts() {
        List<Post> postList = new ArrayList<>(posts.values());
        Collections.sort(postList);
        return postList;
    }

    @Override
    public List<Post> getAllPosts(String username) {
        List<Post> allPosts = getAllPosts();
        List<Post> userPosts = new ArrayList<>();
        for(Post post: allPosts) {
            if(post.getUsername().equals(username)) {
                userPosts.add(post);
            }
        }
        return userPosts;
    }

    @Override
    public List<Post> getRecentPosts(String username, int numPosts) {
        List<Post> allPosts = getAllPosts(username);
        if(allPosts.size() <= numPosts) {
            return allPosts;
        }
        return allPosts.subList(0, numPosts - 1);
    }

    @Override
    public List<Post> getPostsByTag(String tag) {
        List<Post> allPosts = getAllPosts();
        List<Post> tagPosts = new ArrayList<>();
        for(Post post: allPosts) {
            if(post.hasTag(tag)) {
                tagPosts.add(post);
            }
        }
        return tagPosts;
    }

    @Override
    public List<Post> getPostsByTag(String tag, int numPosts) {
        List<Post> allPosts = getPostsByTag(tag);
        if(allPosts.size() <= numPosts) {
            return allPosts;
        }
        return allPosts.subList(0, numPosts - 1);
    }
}
