package model;

import java.util.List;

/**
 * Created by jackson on 21/08/15.
 */
public interface PostDataInterface {
    void addPost(Post post);
    List<Post> getAllPosts();
    List<Post> getAllPosts(String username);
    List<Post> getRecentPosts(String username, int numPosts);
    List<Post> getPostsByTag(String tag);
    List<Post> getPostsByTag(String tag, int numPosts);
}
