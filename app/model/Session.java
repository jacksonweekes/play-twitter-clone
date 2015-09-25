package model;

import com.fasterxml.jackson.databind.JsonNode;
import org.bson.Document;
import org.bson.types.ObjectId;
import play.libs.F;
import play.libs.ws.WS;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;

/**
 * Created by jackson on 15/08/15.
 */
public class Session {
    private String id, ipAddress, location;
    private long since;

    public Session(String id, String ipAddress, long since, String location) {
        this.id = id;
        this.ipAddress = ipAddress;
        this.since = since;
        this.location = location;
    }

    public Session(String ipAddress) {
        this.id = MongoProvider.allocateObjectID();
        this.ipAddress = ipAddress;
        this.since = System.currentTimeMillis();
        this.location = findIPLocation(ipAddress);
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public long getSince() {
        return since;
    }

    private static String findIPLocation(String ipAddress) {
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

    // Create a BSON document from given Session object
    public static Document sessionToBson(Session s) {
        return new Document("_id", new ObjectId(s.getId()))
                .append("ipAddress", s.getIpAddress())
                .append("since", s.getSince())
                .append("location", s.getLocation());
    }

    // Create a Session object from BSON document
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
