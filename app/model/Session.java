package model;

import com.fasterxml.jackson.databind.JsonNode;
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

    public Session(String ipAddress) {
        this.id = java.util.UUID.randomUUID().toString();
        this.ipAddress = ipAddress;
        this.since = System.currentTimeMillis();
        this.location = PPPPfindIPLocation();
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

    private String findIPLocation() {
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
}
