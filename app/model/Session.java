package model;

import com.fasterxml.jackson.databind.JsonNode;
import org.bson.Document;
import org.bson.types.ObjectId;
import play.libs.F;
import play.libs.ws.WS;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;

/**
 * The Session model, used to store information about user sessions
 *
 * @author Jackson Cleary
 */
public class Session {
    private String id, ipAddress, location;
    private long since;

    // This constructor only used for recreating Sessions from database
    private Session(String id, String ipAddress, long since, String location) {
        this.id = id;
        this.ipAddress = ipAddress;
        this.since = since;
        this.location = location;
    }

    /**
     * Constructor
     *
     * @param ipAddress the ip address of the client being logged in
     */
    public Session(String ipAddress) {
        this.id = MongoProvider.allocateObjectID();
        this.ipAddress = ipAddress;
        this.since = System.currentTimeMillis();
        this.location = findIPLocation(ipAddress);
    }

    /**
     * Gets the ip address of the session
     *
     * @return the sessions ip address
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * Gets the id of the session
     *
     * @return id of the session, a {@link ObjectId} represented as a hex string
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the location of the session
     *
     * @return location of the session
     */
    public String getLocation() {
        return location;
    }

    /**
     * Gets the time the session was created
     *
     * @return the time the session was created in milliseconds since epoch
     */
    public long getSince() {
        return since;
    }

    /**
     * Uses http://ip-api.com to find the users location based off their ip address
     *
     * @param ipAddress the ip address to find the location of
     * @return the approximate location of the ip address, or 'unknown' if can't be determined
     */
    public static String findIPLocation(String ipAddress) {
        String city;
        WSClient ws = WS.client();
        // In production environment on localhost this will fail(return "Unknown")
        // Uncomment following line to test(Should return "San Jose") or enter another valid IP
        // String ipAddress = "104.156.228.162";
        String url = "http://ip-api.com/json/" + ipAddress;
        F.Promise<JsonNode> jsonPromise = ws.url(url).get().map(WSResponse::asJson);
        JsonNode content = jsonPromise.get(20000);
        if(content.get("status").asText().equals("fail")) {
            city = "Unknown";
        } else {
            city = content.get("city").asText();
        }
        return city;
    }

    /**
     * Creates a BSON Document from given Session object
     * @param s the session to convert into BSON
     * @return a BSON Document representing the given Session object
     */
    public static Document sessionToBson(Session s) {
        return new Document("_id", new ObjectId(s.getId()))
                .append("ipAddress", s.getIpAddress())
                .append("since", s.getSince())
                .append("location", s.getLocation());
    }

    /**
     * Creates a Session object from BSON Document
     * @param d The Document from which to recreate a Session
     * @return the Session stored in the given Document
     */
    public static Session sessionFromBson(Document d) {
        if(d == null) {
            return null;
        }

        String id = d.getObjectId("_id").toHexString();
        String ipAddress = d.getString("ipAddress");
        long since = d.getLong("since");
        String location = d.getString("location");

        return new Session(id, ipAddress, since, location);
    }
}
