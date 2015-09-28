package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by jackson on 27/09/15.
 */
public class PostHub {
    List<PostListener> listeners;

    static final PostHub instance = new PostHub();

    public static PostHub getInstance() {
        return instance;
    }

    protected PostHub() {
        this.listeners = Collections.synchronizedList(new ArrayList<>());
    }

    public void send(Post p) {
        for(PostListener listener: listeners) {
            listener.receivePost(p);
        }
    }

    public void addListener(PostListener l) {
        this.listeners.add(l);
    }

    public void removeListener(PostListener l) {
        this.listeners.remove(l);
    }
}
