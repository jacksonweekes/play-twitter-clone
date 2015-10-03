package model;

/**
 * PostListener interface. Any objects that wish to register as listeners with
 * the {@link PostHub} must implement this. Modified from tutorial code.
 *
 * @author Jackson Cleary
 * @author William Billingsly
 */
public interface PostListener {
    /**
     * The action to perform when a {@link Post} object has been received by the listener
     *
     * @param p the {@link Post} being received
     */
    void receivePost(Post p);
}
