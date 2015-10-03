package controllers;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import model.PostHub;
import model.PostListener;
import play.libs.Json;

/**
 * Websocket actor, based on that provided in the tutorial.
 *
 * @author Jackson Cleary
 * @author William Billingsly
 */
public class PostWebSocketActor extends UntypedActor {
    /**
     * Creates a 'Props' object for Akka to use to create our actor.
     *
     * @param searchTerm
     * @param searchType
     * @param out
     * @return
     */
    public static Props props(String searchTerm, String searchType, ActorRef out) {
        // Create a Props object that says:
        // - I want a PostWebSocketActor,
        // - and pass (topic, out) as the arguments to its constructor
        return Props.create(PostWebSocketActor.class, searchTerm, searchType, out);
    }

    /** The Actor for the client (browser) */
    private final ActorRef out;

    /** The searchType we have subscribed to('users' or 'tags') */
    private final String searchType;

    /** The searchTerm we have subscribed to */
    private final String searchTerm;

    /** A listener that we will register with our PostHub */
    private final PostListener listener;

    /**
     * This constructor is called by Akka to create our actor (we don't call it ourselves).
     */
    public PostWebSocketActor(String searchTerm, String searchType, ActorRef out) {
        this.searchTerm = searchTerm;
        this.searchType = searchType;
        this.out = out;

        /*
          Our PostListener, written as a Java 8 Lambda.
          Whenever we receive a post, if it matches our topic, convert it to a JSON string, and send it to the client.
         */
        this.listener = (p) -> {
            String message = Json.toJson(p).toString();
            if ((searchType.equals("users") && searchTerm.equals(p.getUsername()))
                    || (searchType.equals("tags") && p.hasTag(searchTerm))) {
                out.tell(Json.toJson(p).toString(), self());
            }
        };

        // Register this actor with the PostHub
        PostHub.getInstance().addListener(listener);
    }

    /**
     * This is called whenever the browser sends a message to the server over the websocket
     */
    public void onReceive(Object message) throws Exception {
        // The client isn't going to send us messages down the websocket in this example, so this doesn't matter
        if (message instanceof String) {
            out.tell("I received your message: " + message, self());
        }
    }

    /**
     * This is called by Play after the WebSocket has closed
     */
    public void postStop() throws Exception {
        // De-register our listener
        PostHub.getInstance().removeListener(this.listener);
    }
}
