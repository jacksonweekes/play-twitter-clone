package controllers;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import model.PostHub;
import model.PostListener;

/**
 * Created by jackson on 27/09/15.
 */
public class PostWebSocketActor extends UntypedActor {
    /**
     * We don't create the actor ourselves. Instead, Play will ask Akka to make it for us. We have to give Akka a
     * "Props" object that tells Akka what kind of actor to create, and what constructor arguments to pass to it.
     * This method produces that Props object.
     */
    public static Props props(String topic, ActorRef out) {
        // Create a Props object that says:
        // - I want a PostWebSocketActor,
        // - and pass (topic, out) as the arguments to its constructor
        return Props.create(PostWebSocketActor.class, topic, out);
    }

    /** The Actor for the client (browser) */
    private final ActorRef out;

    /** The topic string we have subscribed to */
    private final String topic;

    /** A listener that we will register with our GibberishHub */
    private final PostListener listener;

    /**
     * This constructor is called by Akka to create our actor (we don't call it ourselves).
     */
    public PostWebSocketActor(String topic, ActorRef out) {
        this.topic = topic;
        this.out = out;

        /*
          Our GibberishListener, written as a Java 8 Lambda.
          Whenever we receive a gibberish, if it matches our topic, convert it to a JSON string, and send it to the client.
         */
        this.listener = (p) -> {
            //TODO: Listener behaviour
//            if (p.getSubject().equals(this.topic)) {
//                // Convert the Gibberish to a JSON string
//                String message = JsonExample.toJson(g).toString();
//
//                /*
//                 This asynchronously sends the message to the WebSocket client.
//                 Self is a reference to this actor (the sender)
//                 */
//                out.tell(message, self());
//            }
        };

        // Register this actor to hear gibberish
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
