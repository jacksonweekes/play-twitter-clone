package model;

import java.util.List;

/**
 * Interface which any Post data store interfaces must implement. Any providers that
 * implement this interface should be able to provide for the applications data storage
 * needs without any other modification to the application aside from changing which
 * provider is referenced by the Post controller.
 */
public interface PostDataInterface {
    /**
     * Add {@link Post} object to the data store
     *
     * @param post the {@link Post} to add
     */
    void addPost(Post post);

    /**
     * Gets all the {@link Post} objects the data store
     *
     * @return a {@link List} of all {@link Post}
     */
    List<Post> getAllPosts();

    /**
     * Gets all the {@link Post} objects from the data store with the given username
     *
     * @param username the username of the {@link User} whose posts we wish to retrieve
     * @return a {@link List} of all {@link Post} objects by {@link User} given by username
     */
    List<Post> getAllPosts(String username);

    /**
     * Gets a {@link List} of up to numPosts {@link Post} objects by given user
     *
     * @param username the username of the {@link User} whose posts we wish to retrieve
     * @param numPosts the maximum number of {@link Post} objects to retrieve
     * @return up to numPosts {@link Post} objects created by {@link User} given by username
     */
    List<Post> getRecentPosts(String username, int numPosts);

    /**
     * Gets all the {@link Post} objects from the data store tagged with the given tag
     *
     * @param tag the tag we are interested in
     * @return a {@link List} of all {@link Post} objects with the given tag
     */
    List<Post> getPostsByTag(String tag);

    /**
     * Gets a {@link List} of up to numPosts {@link Post} objects with given tag
     *
     * @param tag the tag we are interested in
     * @param numPosts the maximum number of {@link Post} objects to retrieve
     * @return up to numPosts {@link Post} objects with given tag
     */
    List<Post> getPostsByTag(String tag, int numPosts);
}
