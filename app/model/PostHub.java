package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The hub which listeners can register with and receive updates from when new {@link Post}
 * objects enter the system. Modified from tutorial code.
 *
 * @author Jackson Cleary
 * @author William Billlingsly
 */
public class PostHub {
    /** The listeners which have registered with the hub */
    List<PostListener> listeners;

    static final PostHub instance = new PostHub();

    /**
     * Gets the singleton instance of the PostHub
     *
     * @return the PostHub
     */
    public static PostHub getInstance() {
        return instance;
    }

    protected PostHub() {
        this.listeners = Collections.synchronizedList(new ArrayList<>());
    }

    /**
     * Forwards {@link Post} to all listeners that have registered with the hub
     *
     * @param p the {@link Post} to forward
     */
    public void send(Post p) {
        for(PostListener listener: listeners) {
            listener.receivePost(p);
        }
    }

    /**
     * Registers a new listener to the hub
     *
     * @param l the listener to add to the hub
     */
    public void addListener(PostListener l) {
        this.listeners.add(l);
    }

    /**
     * Removes a listener from the hub
     *
     * @param l the listener to remove
     */
    public void removeListener(PostListener l) {
        this.listeners.remove(l);
    }
}
